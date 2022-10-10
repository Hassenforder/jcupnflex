package fr.uha.hassenforder.jcupnflex.model;

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

	/** A part for the right hand side. Mainly a ListPart but could be null for empty */
	private final ProductionPart rhs;

	/** The precedence of the rule */
	private Terminal precedence;

	/**
	 * Full constructor.
	 * 
	 * @param lhs the terminal as lhs 
	 * @param rhs the List of ProductionParts terminal as rhs 
	 * @param precedence the precedence of the production 
	 */
	public NonTerminalProduction(NonTerminal lhs, ProductionPart rhs, Terminal precedence) {
		super(ProductionKind.NONTERMINAL);
		this.lhs = lhs;
		this.rhs = rhs;
		this.precedence = precedence;
	}

	@Override
	public NonTerminal getLhs() {
		return lhs;
	}

	public ProductionPart getRhs() {
		return rhs;
	}

	public Terminal getPrecedence() {
		return precedence;
	}

}
