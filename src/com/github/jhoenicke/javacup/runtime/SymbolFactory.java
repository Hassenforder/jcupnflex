package com.github.jhoenicke.javacup.runtime;

/**
 * Creates the Symbols interface, which CUP uses as default
 *
 * @version last updated 12-03-2022
 * @author Michel Hassenforder
 * @version last updated 27-03-2006
 * @author Michael Petter
 */

/*
 * *************************************************
 * Interface SymbolFactory
 * 
 * An interface for creating new symbols You can also use this interface for your
 * own callback hooks Declare Your own factory methods for creation of Objects
 * in Your scanner!
 ***************************************************/
public interface SymbolFactory {

	/**
	 * Construction with left/right propagation switched on and can carry a value
	 */
	Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value);

	/**
	 * Construction with left/right propagation switched on without value
	 */
	Symbol newSymbol(String name, int id, Symbol left, Symbol right);

	/**
	 * Construction with left/right propagation switched off
	 */
	Symbol newSymbol(String name, int id);

	/**
	 * Construction with left/right propagation switched off with a value
	 */
	Symbol newSymbol(String name, int id, Object value);

	/**
	 * Construction of start symbol
	 */
	Symbol startSymbol(String name, int id, int state);

}
