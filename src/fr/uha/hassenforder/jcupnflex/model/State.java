package fr.uha.hassenforder.jcupnflex.model;

/**
 * 
 * This class represents a state in the lexical scanner.
 * It inherits from GrammarSymbol
 *
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public class State extends GrammarSymbol {

	/**
	 * Full constructor.
	 * 
	 * @param name the name of the state.
	 * @param type the type string for the state.
	 */
	public State(String name, String type) {
		super(SymbolKind.STATE, name, type);
	}

	/**
	 * Constructor for typeless state.
	 * 
	 * @param name the name of the non terminal.
	 */
	public State(String name) {
		this(name, null);
	}

}
