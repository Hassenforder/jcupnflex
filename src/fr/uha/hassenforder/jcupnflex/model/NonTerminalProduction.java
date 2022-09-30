package fr.uha.hassenforder.jcupnflex.model;

import java.util.List;

/**
 * 
 * This class represents a production with a nonterminal as LHS.
 * It inherits from Production
 * It is the common syntax rule in grammar
 * 
 * The precedence of the rule is determined by an external terminal
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public class NonTerminalProduction extends Production {

	/** The left hand side non-terminal. */
	private final NonTerminal lhs;

	/** The precedence of the rule */
	private Terminal precedence;

	/**
	 * Full constructor.
	 * 
	 * @param lhs the terminal as lhs 
	 * @param rhs the List of ProductionParts terminal as rhs 
	 * @param precedence the precedence of the production 
	 */
	public NonTerminalProduction(NonTerminal lhs, List<ProductionPart> rhs, Terminal precedence) {
		super(rhs);
		this.lhs = lhs;
		this.precedence = precedence;
	}

	@Override
	public SymbolKind getKind() {
		return lhs.getKind();
	}

	public NonTerminal getLhs() {
		return lhs;
	}

	public Terminal getPrecedence() {
		return precedence;
	}

}
