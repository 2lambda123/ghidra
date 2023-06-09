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

import java.util.*;

import java.io.IOException;

import ghidra.app.util.bin.*;
import ghidra.program.model.data.*;
import ghidra.util.exception.DuplicateNameException;

public class ClassDataItem implements StructConverter {

	private int staticFieldsSize;
	private int instanceFieldsSize;
	private int directMethodsSize;
	private int virtualMethodsSize;

	private int staticFieldsSizeLength;// in bytes
	private int instanceFieldsSizeLength;// in bytes
	private int directMethodsSizeLength;// in bytes
	private int virtualMethodsSizeLength;// in bytes

	private List<EncodedField> staticFields = new ArrayList<>();
	private List<EncodedField> instancesFields = new ArrayList<>();
	private List<EncodedMethod> directMethods = new ArrayList<>();
	private List<EncodedMethod> virtualMethods = new ArrayList<>();

	public ClassDataItem(BinaryReader reader, DexHeader dexHeader) throws IOException {
		LEB128Info leb128 = reader.readNext(LEB128Info::unsigned);
		staticFieldsSize = leb128.asUInt32();
		staticFieldsSizeLength = leb128.getLength();

		leb128 = reader.readNext(LEB128Info::unsigned);
		instanceFieldsSize = leb128.asUInt32();
		instanceFieldsSizeLength = leb128.getLength();

		leb128 = reader.readNext(LEB128Info::unsigned);
		directMethodsSize = leb128.asUInt32();
		directMethodsSizeLength = leb128.getLength();

		leb128 = reader.readNext(LEB128Info::unsigned);
		virtualMethodsSize = leb128.asUInt32();
		virtualMethodsSizeLength = leb128.getLength();

		for (int i = 0; i < staticFieldsSize; ++i) {
			staticFields.add(new EncodedField(reader));
		}
		for (int i = 0; i < instanceFieldsSize; ++i) {
			instancesFields.add(new EncodedField(reader));
		}
		int methodIndex = 0;
		for (int i = 0; i < directMethodsSize; ++i) {
			EncodedMethod encodedMethod = new EncodedMethod(reader, dexHeader);
			directMethods.add(encodedMethod);
			methodIndex += encodedMethod.getMethodIndexDifference();
			encodedMethod.setMethodIndex(methodIndex);
		}
		methodIndex = 0;
		for (int i = 0; i < virtualMethodsSize; ++i) {
			EncodedMethod encodedMethod = new EncodedMethod(reader, dexHeader);
			virtualMethods.add(encodedMethod);
			methodIndex += encodedMethod.getMethodIndexDifference();
			encodedMethod.setMethodIndex(methodIndex);
		}
	}

	public List<EncodedField> getInstancesFields() {
		return Collections.unmodifiableList(instancesFields);
	}

	public List<EncodedField> getStaticFields() {
		return Collections.unmodifiableList(staticFields);
	}

	public List<EncodedMethod> getDirectMethods() {
		return Collections.unmodifiableList(directMethods);
	}

	public List<EncodedMethod> getVirtualMethods() {
		return Collections.unmodifiableList(virtualMethods);
	}

	public int getStaticFieldsSize() {
		return staticFieldsSize;
	}

	public int getInstanceFieldsSize() {
		return instanceFieldsSize;
	}

	public int getDirectMethodsSize() {
		return directMethodsSize;
	}

	public int getVirtualMethodsSize() {
		return virtualMethodsSize;
	}

	public EncodedMethod getMethodByIndex(int index) {
		for (int i = 0; i < directMethods.size(); ++i) {
			EncodedMethod encodedMethod = directMethods.get(i);
			if (encodedMethod.getMethodIndex() == index) {
				return encodedMethod;
			}
		}
		for (int i = 0; i < virtualMethods.size(); ++i) {
			EncodedMethod encodedMethod = virtualMethods.get(i);
			if (encodedMethod.getMethodIndex() == index) {
				return encodedMethod;
			}
		}
		return null;
	}

	@Override
	public DataType toDataType() throws DuplicateNameException, IOException {
//		int unique = 0;
		String name = "class_data_item_%d_%d_%d_%d".formatted(staticFieldsSizeLength,
			instanceFieldsSizeLength, directMethodsSizeLength, virtualMethodsSizeLength);
		Structure structure = new StructureDataType(name, 0);
		structure.add(ULEB128, staticFieldsSizeLength, "static_fields", null);
		structure.add(ULEB128, instanceFieldsSizeLength, "instance_fields", null);
		structure.add(ULEB128, directMethodsSizeLength, "direct_methods", null);
		structure.add(ULEB128, virtualMethodsSizeLength, "virtual_methods", null);
//		int index = 0;
//		for ( EncodedField field : staticFields ) {
//			DataType dataType = field.toDataType( );
//			structure.add( dataType, "staticField" + index, null );
//			++index;
//		}
//		index = 0;
//		for ( EncodedField field : instancesFields ) {
//			DataType dataType = field.toDataType( );
//			structure.add( dataType, "instancesField" + index, null );
//			++index;
//		}
//		index = 0;
//		for ( EncodedMethod method : directMethods ) {
//			DataType dataType = method.toDataType( );
//			structure.add( dataType, "directMethod" + index, null );
//			++index;
//		}
//		index = 0;
//		for ( EncodedMethod method : virtualMethods ) {
//			DataType dataType = method.toDataType( );
//			structure.add( dataType, "virtualMethod" + index, null );
//			++index;
//		}
		structure.setCategoryPath(new CategoryPath("/dex/class_data_item"));
//		try {
//			structure.setName( name + "_" + Integer.toHexString( unique ) );
//		}
//		catch ( Exception e ) {
//			// ignore
//		}
		return structure;
	}
}
