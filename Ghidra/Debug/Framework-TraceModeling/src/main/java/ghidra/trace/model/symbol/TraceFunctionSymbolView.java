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
package ghidra.trace.model.symbol;

import java.util.Collection;

import ghidra.program.database.function.OverlappingFunctionException;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.lang.PrototypeModel;
import ghidra.program.model.symbol.SourceType;
import ghidra.trace.model.Lifespan;
import ghidra.util.exception.InvalidInputException;

public interface TraceFunctionSymbolView extends TraceSymbolWithLocationView<TraceFunctionSymbol> {

	TraceFunctionSymbol add(Lifespan lifespan, Address entryPoint, AddressSetView body,
			String name, TraceFunctionSymbol thunked, TraceNamespaceSymbol parent,
			SourceType source) throws InvalidInputException, OverlappingFunctionException;

	default TraceFunctionSymbol create(long snap, Address entryPoint, AddressSetView body,
			String name, TraceFunctionSymbol thunked, TraceNamespaceSymbol parent,
			SourceType source) throws InvalidInputException, OverlappingFunctionException {
		return add(Lifespan.nowOn(snap), entryPoint, body, name, thunked, parent, source);
	}

	/**
	 * Get the ordered unmodifiable set of defined calling convention names.  The reserved names 
	 * "unknown" and "default" are not included.  The returned collection may not include all names 
	 * referenced by various functions and function-definitions.  This set is limited to 
	 * those defined by the associated compiler specification.
	 *
	 * @return the set of defined calling convention names.
	 */
	Collection<String> getCallingConventionNames();

	/**
	 * Get the default calling convention's prototype model.
	 *
	 * @return the default calling convention prototype model.
	 */
	PrototypeModel getDefaultCallingConvention();

	/**
	 * Get the prototype model of the calling convention with the specified name from the 
	 * associated compiler specification.  If {@link Function#DEFAULT_CALLING_CONVENTION_STRING}
	 * is specified {@link #getDefaultCallingConvention()} will be returned.
	 * 
	 * @param name the calling convention name
	 * @return the named function calling convention prototype model or null if not defined.
	 */
	PrototypeModel getCallingConvention(String name);
}
