package fr.uha.hassenforder.jcupnflex.model;

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

	protected Production() {
	}

	public abstract ProductionKind getKind() ;

	public abstract GrammarSymbol getLhs();
	
}
