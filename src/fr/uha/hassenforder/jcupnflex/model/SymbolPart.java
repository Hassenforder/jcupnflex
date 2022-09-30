package fr.uha.hassenforder.jcupnflex.model;

/**
 * Borrowed from JCup 
 * 
 * This class represents one part of a production with a GrammarSymbol
 *
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */
public class SymbolPart extends ProductionPart {

	/** The symbol that this part is made up of. */
	private final GrammarSymbol symbol;

	/**
	 * Full constructor.
	 * 
	 * @param symbol the symbol that this part is made up of.
	 * @param label an optional label string for the part.
	 */
	public SymbolPart(GrammarSymbol symbol) {
		super (ProductionKind.SYMBOL, null);
		assert symbol != null : "Attempt to construct a production with a null symbol";
		this.symbol = symbol;
	}

	public GrammarSymbol getSymbol() {
		return symbol;
	}

}
