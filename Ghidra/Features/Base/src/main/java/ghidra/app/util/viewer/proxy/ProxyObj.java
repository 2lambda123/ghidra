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
 * Implementing objects of this interface hold an object from a program (e.g.,  CodeUnit, Function,
 * etc.) in such a way as to be robust against changes to the program.   In other words, it protects 
 * against holding on to "stale" objects.  The getObject() method will return the represented object
 * (refreshed if it was stale) or null if it no longer exists.
 * 
 * @param <T> the proxy object type
 */
public abstract class ProxyObj<T> {

	private ListingModel model;

	ProxyObj(ListingModel model) {
		this.model = model;
	}

	/**
	 * Returns the layout model which corresponds to this field proxy.
	 * @return the model
	 */
	public ListingModel getListingLayoutModel() {
		return model;
	}

	/**
	 * Returns the object that this proxy represents or null if the object no longer exists.
	 * @return the object that this proxy represents or null if the object no longer exists.
	 */
	public abstract T getObject();

	/**
	 * Returns true if the proxy object of this class contains the given address.
	 * @param a the address
	 * @return true if the proxy object of this class contains the given address.
	 */
	public abstract boolean contains(Address a);
}
