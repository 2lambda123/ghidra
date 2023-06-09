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

/**
 * Basic implementation for a Short Integer dataType 
 */
public class UnsignedShortDataType extends AbstractUnsignedIntegerDataType {

	/** A statically defined UnsignedShortDataType instance.*/
	public final static UnsignedShortDataType dataType = new UnsignedShortDataType();

	public UnsignedShortDataType() {
		this(null);
	}

	public UnsignedShortDataType(DataTypeManager dtm) {
		super("ushort", dtm);
	}

	@Override
	public int getLength() {
		return getDataOrganization().getShortSize();
	}

	@Override
	public boolean hasLanguageDependantLength() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Unsigned Short Integer (compiler-specific size)";
	}

	@Override
	public String getCDeclaration() {
		return C_UNSIGNED_SHORT;
	}

	@Override
	public ShortDataType getOppositeSignednessDataType() {
		return ShortDataType.dataType.clone(getDataTypeManager());
	}

	@Override
	public UnsignedShortDataType clone(DataTypeManager dtm) {
		if (dtm == getDataTypeManager()) {
			return this;
		}
		return new UnsignedShortDataType(dtm);
	}

	@Override
	public String getCTypeDeclaration(DataOrganization dataOrganization) {
		return getCTypeDeclaration(getName(), "unsigned short", false); // standard C-primitive type with modified name
	}

}
