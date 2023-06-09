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
package ghidra.util.table;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import docking.widgets.table.GTableCellRenderer;
import ghidra.app.util.viewer.field.ListingColors;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.Memory;
import ghidra.program.model.symbol.ExternalLocation;
import ghidra.program.model.symbol.Symbol;

public class GhidraTableCellRenderer extends GTableCellRenderer {

	public GhidraTableCellRenderer() {
		// default constructor
	}

	/**
	 * Constructs a new GhidraTableCellRenderer using the specified font.
	 * @param f the font to use when rendering text in the table cells
	 */
	public GhidraTableCellRenderer(Font f) {
		super(f);
	}

	@Override
	protected String getText(Object value) {
		if (value == null) {
			return "";
		}
		if (isExternalAdress(value)) {
			return "<External>";
		}
		return value.toString();
	}

	@Override
	protected void setForegroundColor(JTable table, TableModel model, Object value) {
		if (isExternalAdress(value)) {
			if (isResolvedExternalAddress(model, (Address) value)) {
				setForeground(ListingColors.EXT_REF_RESOLVED);
			}
			else {
				setForeground(ListingColors.EXT_REF_UNRESOLVED);
			}
		}
		else if (isValueOutOfMemoryAddress(model, value)) {
			setForeground(ListingColors.REF_BAD);
		}
		else {
			setForeground(table.getForeground());
		}
	}

	private boolean isExternalAdress(Object value) {
		if (!(value instanceof Address)) {
			return false;
		}
		return ((Address) value).isExternalAddress();
	}

	private boolean isResolvedExternalAddress(TableModel model, Address extAddr) {

		if (!(model instanceof ProgramTableModel)) {
			return false;
		}
		ProgramTableModel programTableModel = (ProgramTableModel) model;

		Program program = programTableModel.getProgram();
		if (program == null) {
			return false; // can happen when program closed
		}

		Symbol s = program.getSymbolTable().getPrimarySymbol(extAddr);
		ExternalLocation extLoc = program.getExternalManager().getExternalLocation(s);
		String path = program.getExternalManager().getExternalLibraryPath(extLoc.getLibraryName());
		return (path != null && path.length() > 0);
	}

	private boolean isValueOutOfMemoryAddress(TableModel model, Object value) {
		if (!(value instanceof Address)) {
			return false;
		}

		if (!(model instanceof ProgramTableModel)) {
			return false;
		}
		ProgramTableModel programTableModel = (ProgramTableModel) model;

		Program program = programTableModel.getProgram();
		if (program == null) {
			return false; // can happen when program closed
		}

		Address address = (Address) value;
		Memory memory = program.getMemory();
		return !memory.contains(address);
	}
}
