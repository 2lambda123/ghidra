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
package ghidra.app.util.viewer.proxy;

import ghidra.app.util.viewer.listingpanel.ListingModel;
import ghidra.program.model.address.Address;

/**
 * Stores information about an address in a program.
 */
public class AddressProxy extends ProxyObj<Address> {

	private Address addr;

	/**
	 * Construct a address proxy
	 * @param model the model
	 * @param addr the address to proxy
	 */
	public AddressProxy(ListingModel model, Address addr) {
		super(model);
		this.addr = addr;
	}

	@Override
	public Address getObject() {
		return addr;
	}

	@Override
	public boolean contains(Address a) {
		return addr.equals(a);
	}
}
