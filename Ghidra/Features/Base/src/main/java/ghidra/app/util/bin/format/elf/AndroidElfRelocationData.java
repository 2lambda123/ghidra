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
package ghidra.app.util.bin.format.elf;

import javax.help.UnsupportedOperationException;

import ghidra.docking.settings.Settings;
import ghidra.docking.settings.SettingsDefinition;
import ghidra.program.model.address.Address;
import ghidra.program.model.data.*;

/**
 * <code>AndroidElfRelocationData</code> provides a dynamic LEB128 data 
 * component for packed Android ELF Relocation Table.
 * See {@link AndroidElfRelocationTableDataType}.
 * <br>
 * Secondary purpose is to retain the relocation offset associated with a 
 * component instance.  This functionality relies on the 1:1 relationship
 * between this dynamic datatype and the single component which references it.
 */
class AndroidElfRelocationData extends SignedLeb128DataType {

	private final long relocationOffset;

	/**
	 * Creates a packed relocation offset data type based upon a signed LEB128
	 * value.
	 * @param dtm the data type manager to associate with this data type.
	 * @param relocationOffset relocation offset associated with component.
	 */
	AndroidElfRelocationData(DataTypeManager dtm, long relocationOffset) {
		super(dtm);
		this.relocationOffset = relocationOffset;
	}

	@Override
	public DataType clone(DataTypeManager dtm) {
		// specific instances are used by AndroidElfRelocationTableDataType
		throw new UnsupportedOperationException("may not be cloned");
	}

	@Override
	public String getDescription() {
		return "Android Packed Relocation Data for ELF";
	}

	@Override
	protected SettingsDefinition[] getBuiltInSettingsDefinitions() {
		return null;
	}

	@Override
	public Class<?> getValueClass(Settings settings) {
		return Address.class;
	}

	/**
	 * Get the relocation offset associated with this data item
	 * @return the relocation offset associated with this data item
	 */
	long getRelocationOffset() {
		return relocationOffset;
	}
}
