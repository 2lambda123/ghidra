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
package ghidra.program.model.data;

import ghidra.util.classfinder.ClassTranslator;

/**
 * A Signed Little Endian Base 128 integer data type.
 */
public class SignedLeb128DataType extends AbstractLeb128DataType {
	static {
		ClassTranslator.put("ghidra.app.plugin.exceptionhandlers.gcc.datatype.SignedLeb128DataType",
			SignedLeb128DataType.class.getName());
	}

	/** A statically defined SignedLeb128DataType instance.*/
	public final static SignedLeb128DataType dataType = new SignedLeb128DataType();

	/**
	 * Creates a signed little endian base 128 integer data type.
	 */
	public SignedLeb128DataType() {
		this(null);
	}

	/**
	 * Creates a signed little endian base 128 integer data type.
	 * @param dtm the data type manager to associate with this data type.
	 */
	public SignedLeb128DataType(DataTypeManager dtm) {
		super("sleb128", true, dtm);
	}

	@Override
	public DataType clone(DataTypeManager dtm) {
		if (dtm == getDataTypeManager()) {
			return this;
		}
		return new SignedLeb128DataType(dtm);
	}

	@Override
	public String getDescription() {
		return "Signed LEB128-Encoded Number";
	}
}
