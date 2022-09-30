package fr.uha.hassenforder.jcupnflex.model;

import java.util.List;

/**
 * Borrowed from JCup 
 * 
 * This class represents one production in the grammar.
 * It doesn't define the LHS of the production like in Jcup
 * This is dedicated to the children : TerminalProduction and NonTerminalProduction 
 * 
 * It contains an array of RHS symbols 
 * 
 * @see fr.uha.hassenforder.jcupnflex.model.TerminalProduction
 * @see fr.uha.hassenforder.jcupnflex.model.NonTerminalProduction
 * @see fr.uha.hassenforder.jcupnflex.model.ProductionPart
 * 
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */

public abstract class Production {

	/** A collection of parts for the right hand side. */
	private final List<ProductionPart> rhs;

	/**
	 * Full constructor.
	 * 
	 * This constructor accepts an array of ProductionParts
	 * (including terminals, regexp, non terminals, operators, actions)
	 * 
	 */
	protected Production(List<ProductionPart> rhs) {
		this.rhs = rhs;
	}

	public abstract SymbolKind getKind() ;

	public List<ProductionPart> getRhs() {
		return rhs;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (ProductionPart part : rhs) {
			result.append(part.toString());
			result.append(" ");
		}
		return result.toString();
	}

}
