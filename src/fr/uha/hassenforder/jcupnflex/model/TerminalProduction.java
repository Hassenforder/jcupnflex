package fr.uha.hassenforder.jcupnflex.model;

import java.util.List;

/**
 * 
 * This class represents a production with a terminal as LHS.
 * It inherits from Production
 * It is a lexical rule in the grammar
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public class TerminalProduction extends Production {

	/** The left hand side non-terminal. */
	private final Terminal lhs;

	/**
	 * Full constructor.
	 * 
	 * @param lhs the terminal as lhs 
	 * @param rhs the List of ProductionParts terminal as rhs 
	 */
	public TerminalProduction(Terminal lhs, List<ProductionPart> rhs) {
		super(rhs);
		this.lhs = lhs;
	}

	@Override
	public SymbolKind getKind() {
		return lhs.getKind();
	}

	public Terminal getLhs() {
		return lhs;
	}

}
