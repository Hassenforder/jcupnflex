package fr.uha.hassenforder.jcupnflex.model;

/**
 * Borrowed from JCup 
 * 
 * This class represents a non-terminal symbol in the grammar.
 * It inherits from GrammarSymbol
 *
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */

public class NonTerminal extends GrammarSymbol {

	/**
	 * Full constructor.
	 * 
	 * @param name the name of the non terminal.
	 * @param type the type string for the non terminal.
	 */
	public NonTerminal(String name, String type) {
		super(SymbolKind.NONTERMINAL, name, type);
	}

	/**
	 * Constructor for typeless nonterminal.
	 * 
	 * @param name the name of the non terminal.
	 */
	public NonTerminal(String name) {
		this(name, null);
	}

}
