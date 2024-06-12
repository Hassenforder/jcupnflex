package com.github.jhoenicke.javacup.runtime;

import java.util.List;

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
 * 
 * Interface SymbolFactory2
 * 
 * an interface for creating new symbols based on Enum (12 june 2022)
 * extended to store the lust of child symbols to propagate the whole concrete syntax tree (25 march 2024)
 *
 ***************************************************/
public interface SymbolFactory2 {

	/*
	 * new API for all Factories
	 */

	/**
	 * Construction with child propagation switched on
	 * Strongly used by the generated Parser to build nonterminals during reduce actions
	 * 
	 * @param token an enum to represent the symbol (mainly Nonterminal)
	 * @param children  the child symbols of the rule
	 * 
	 * @return a symbol
	 */
	public Symbol newSymbol(Enum<?> token, List<Symbol> children);

	/**
	 * Construction with left/right propagation switched on and a value
	 * Strongly used by the generated Parser to build nonterminals during reduce actions
	 * 
	 * @param token an enum to represent the symbol (mainly Nonterminal)
	 * @param left  the leftmost symbol of the rule
	 * @param right	the rightmost symbol of the rule
	 * @param value a semantic value an object of an arbitrary Class
	 * 
	 * @return a symbol
	 */
	public Symbol newSymbol(Enum<?> token, Symbol left, Symbol right, Object value);

	/**
	 * Construction with left/right propagation switched on without a value
	 * Strongly used by the generated Parser to build nonterminals during reduce actions
	 * 
	 * @param token an enum to represent the symbol (mainly Nonterminal)
	 * @param left  the leftmost symbol of the rule
	 * @param right	the rightmost symbol of the rule
	 * 
	 * @return a symbol
	 */
	public Symbol newSymbol(Enum<?> token, Symbol left, Symbol right);

	/**
	 * Construction without left/right propagation but with a value
	 * Strongly used by the scanner to build terminals
	 * 
	 * @param token an enum to represent the symbol (mainly Terminal)
	 * @param value a semantic value an object of an arbitrary Class
	 * 
	 * @return a symbol
	 */
	public Symbol newSymbol(Enum<?> token, Object value);

	/**
	 * Construction without left/right propagation and value
	 * Strongly used by the scanner to build terminals
	 * 
	 * @param token an enum to represent the symbol (mainly Terminal)
	 * 
	 * @return a symbol
	 */
	public Symbol newSymbol(Enum<?> token);

	/**
	 * Construction of a start symbol
	 * 
	 * @return a symbol
	 */
	public Symbol startSymbol();

	/**
	 * Construction of an end symbol
	 * 
	 * @return the symbol of the grammar
	 */
	public Symbol endSymbol();

	/**
	 * Construction of an error symbol
	 * 
	 * @param left the leftmost symbol of the rule
	 * @param right the rightmost symbol of the rule
	 * 
	 * @return the symbol of the grammar
	 */
	public Symbol errorSymbol(Symbol left, Symbol right);

}
