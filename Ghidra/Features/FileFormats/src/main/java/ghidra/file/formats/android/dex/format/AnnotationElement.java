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
package ghidra.file.formats.android.dex.format;

import java.io.IOException;

import ghidra.app.util.bin.*;
import ghidra.program.model.data.*;
import ghidra.util.exception.DuplicateNameException;

public class AnnotationElement implements StructConverter {

	private int nameIndex;
	private int nameIndexLength;// in bytes
	private EncodedValue value;

	public AnnotationElement(BinaryReader reader) throws IOException {
		LEB128Info leb128 = reader.readNext(LEB128Info::unsigned);
		nameIndex = leb128.asUInt32();
		nameIndexLength = leb128.getLength();

		value = new EncodedValue(reader);
	}

	public int getNameIndex() {
		return nameIndex;
	}

	public EncodedValue getValue() {
		return value;
	}

	@Override
	public DataType toDataType() throws DuplicateNameException, IOException {
		DataType encodeValueDataType = value.toDataType();

		String name =
			"annotation_element_%d_%s".formatted(nameIndexLength, encodeValueDataType.getName());

		Structure structure = new StructureDataType(name, 0);

		structure.add(ULEB128, nameIndexLength, "nameIndex", null);

		structure.add(encodeValueDataType, "value", null);

		structure.setCategoryPath(new CategoryPath("/dex/annotation_element"));

		return structure;
	}
}
