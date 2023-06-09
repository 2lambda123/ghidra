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
/*
 * Created on Aug 11, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ghidra.app.util.viewer.proxy;

import java.util.ConcurrentModificationException;

import ghidra.app.util.viewer.listingpanel.ListingModel;
import ghidra.program.model.address.Address;
import ghidra.program.model.listing.CodeUnit;
import ghidra.program.model.listing.Program;

/**
 * Stores information about a code unit in a program.
 */
public class CodeUnitProxy extends ProxyObj<CodeUnit> {
	private Program program;
	private CodeUnit cu;
	private Address addr;

	/**
	 * Construct a proxy for a code unit
	 * @param model the model
	 * @param program the program containing the code unit
	 * @param cu the code unit to proxy.
	 */
	public CodeUnitProxy(ListingModel model, Program program, CodeUnit cu) {
		super(model);
		this.program = program;
		this.cu = cu;
		this.addr = cu.getMinAddress();
	}

	@Override
	public CodeUnit getObject() {
		if (cu != null) {
			try {
				cu.getMinAddress();
				return cu;
			}
			catch (ConcurrentModificationException e) {
			}
		}
		cu = program.getListing().getCodeUnitAt(addr);
		return cu;
	}

	@Override
	public boolean contains(Address a) {
		CodeUnit c = getObject();
		if (c == null) {
			return false;
		}
		return c.contains(a);
	}
}
