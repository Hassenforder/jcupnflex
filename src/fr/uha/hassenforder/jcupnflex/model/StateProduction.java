package fr.uha.hassenforder.jcupnflex.model;

/**
 * 
 * This class represents a production with a state as LHS.
 * It inherits from TerminalProduction as they share a lot
 * It is a lexical rule in the grammar
 * 
 * state	::=	REGEXP TILDA REGEXP						CODE_STRING?    // region from YYINITIAL
 *			|	SYMBOL_STATE REGEXP TILDA REGEXP		CODE_STRING?    // region in another state
 *			;
 *
 * State rule will never capture content its intent is
 *    to switch on the state according to a beginning regexp
 *    to switch off the state according to an ending regexp
 *    
 * The last rule is to embed regions
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public class StateProduction extends LexicalProduction {

	/** The left hand side state. */
	private final State lhs;
	
	/**
	 * Full constructor for an embedded state
	 * 
	 * @param lhs the state as lhs 
	 * @param inState the initial state where to start the region can be null
	 * @param from the regexp to start the region
	 * @param to the regexp to end the region
	 * @param special code to execute
	 */
	public StateProduction(State lhs, LexicalKind sub, String inState, String regexp, String nextState, String code) {
		super(ProductionKind.STATE, sub, inState, regexp, nextState, code);
		this.lhs = lhs;
	}

	@Override
	public State getLhs() {
		return lhs;
	}

}
