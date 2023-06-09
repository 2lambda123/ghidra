/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.plugin.core.navigation;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;

import generic.theme.GIcon;
import ghidra.framework.plugintool.PluginTool;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.*;
import ghidra.util.exception.CancelledException;
import ghidra.util.task.TaskMonitor;

public class NextPreviousUndefinedAction extends AbstractNextPreviousAction {

	private static final Icon ICON = new GIcon("icon.plugin.navigation.undefined");

	public NextPreviousUndefinedAction(PluginTool tool, String owner, String subGroup) {
		super(tool, "Next Undefined", owner, subGroup);
	}

	@Override
	protected Icon getIcon() {
		return ICON;
	}

	@Override
	protected KeyStroke getKeyStroke() {
		return KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_DOWN_MASK |
			InputEvent.ALT_DOWN_MASK);
	}

	@Override
	protected String getNavigationTypeName() {
		return "Undefined";
	}

	@Override
	protected Address getNextAddress(TaskMonitor monitor, Program program, Address address)
			throws CancelledException {

		if (isInverted) {
			return getNextNonUndefined(monitor, program, address);
		}

		if (isUndefinedAt(program, address)) {
			// on an undefined, find a defined before finding the next undefined
			address = getAddressOfNextDefined(program, address);
		}

		// we know address is not an undefined at this point
		return getAddressOfNextPreviousUndefined(monitor, program, address, true);
	}

	@Override
	protected Address getPreviousAddress(TaskMonitor monitor, Program program, Address address)
			throws CancelledException {

		if (isInverted) {
			return getPreviousNonUndefined(monitor, program, address);
		}

		if (isUndefinedAt(program, address)) {
			// on an undefined, find a defined before finding the previous undefined
			address = getAddressOfPreviousDefined(program, address);
		}

		// we know address is not at an undefined at this point
		return getAddressOfNextPreviousUndefined(monitor, program, address, false);
	}

	private Address getNextNonUndefined(TaskMonitor monitor, Program program, Address address)
			throws CancelledException {

		//
		// Assumptions:
		// -if on an undefined, find the next instruction or data
		// -if not on an undefined, find the next undefined, then find the next instruction or 
		//  undefined after that (this mimics the non-inverted case)
		//
		if (!isUndefinedAt(program, address)) {
			address = getAddressOfNextPreviousUndefined(monitor, program, address, true);
		}

		return getAddressOfNextDefined(program, address);
	}

	private Address getPreviousNonUndefined(TaskMonitor monitor, Program program, Address address)
			throws CancelledException {

		//
		// Assumptions:
		// -if on an undefined, find the previous data or instruction
		// -if not on an undefined, find the previous undefined, then find the previous data or 
		//  instruction before that (this mimics the non-inverted case)
		//
		if (!isUndefinedAt(program, address)) {
			address = getAddressOfNextPreviousUndefined(monitor, program, address, false);
		}

		return getAddressOfPreviousDefined(program, address);
	}

	private boolean isUndefinedAt(Program program, Address address) {
		if (address == null) {
			return false;
		}
		Data data = program.getListing().getDataAt(address);
		if (data == null) {
			return false;
		}
		return !data.isDefined();
	}

	private Address getAddressOfNextDefined(Program program, Address address) {
		CodeUnit cu = program.getListing().getDefinedCodeUnitAfter(address);
		if (cu == null) {
			return null;
		}
		return cu.getMinAddress();
	}

	private Address getAddressOfPreviousDefined(Program program, Address address) {
		CodeUnit cu = program.getListing().getDefinedCodeUnitBefore(address);
		if (cu == null) {
			return null;
		}
		return cu.getMinAddress();
	}

	private Address getAddressOfNextPreviousUndefined(TaskMonitor monitor, Program program,
			Address address, boolean forward) throws CancelledException {
		if (address == null) {
			return null;
		}

		CodeUnitIterator codeUnits = program.getListing().getCodeUnits(address, forward);
		while (codeUnits.hasNext()) {
			monitor.checkCancelled();
			CodeUnit codeUnit = codeUnits.next();
			if (codeUnit instanceof Data) {
				if (!((Data) codeUnit).isDefined()) {
					return codeUnit.getAddress();
				}
			}
		}
		return null;
	}

}
