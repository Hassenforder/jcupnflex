package fr.uha.hassenforder.jcupnflex.model;

/**
 * 
 * This class represents a production with a terminal as LHS.
 * It inherits from LexicalProduction
 * It is a lexical rule in the grammar
 * 
 * many grammar rules can produce a TerminalProduction but for the generator the main job is just to capture
 * all kind of parameters and some of them can be null 
 * 
 * rhs_t	::=	REGEXP									CODE_STRING?	// simple
 * 			|	REGEXP TILDA REGEXP						CODE_STRING?    // region
 * 			|	REGEXP TILDA SYMBOL_STATE 				CODE_STRING?    // start a state (push)
 * 			|	SYMBOL_STATE REGEXP						CODE_STRING?    // in a state and stay
 * 			|	SYMBOL_STATE REGEXP TILDA				CODE_STRING?	// in a state and leave it (pop)
 * 			|	SYMBOL_STATE REGEXP TILDA SYMBOL_STATE	CODE_STRING?	// in a state and start a new state (push)
 * 			;
 *
 * For the generator (semantic) the main purpose is to capture content and return it to the parser
 * The first rule is the common one
 * The second rule is to open a state with the first regexp and close it with the second
 *    and a String with the content gathered between the two regexps is returned
 * The third, forth and fifth rules are here to split the part of a region
 *    the third opens a region with a dedicated regexp
 *    the fifth close the region with a dedicated regexp
 *    and the forth gather content in the region between third and fifth rules
 * The sixth highly unpredictable can embed region in a previous one
 * 
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public class TerminalProduction extends LexicalProduction {

	/** The left hand side terminal. */
	private final Terminal lhs;
	
	/**
	 * Full constructor for a Terminal Production
	 * 
	 * @param lhs the terminal as lhs 
	 * @param inState the rule can only occur in the inState
	 * @param regexp the only one regexp to match
	 * @param fromRegexp the initial regexp to match
	 * @param toRegexp the final regexp to match
	 * @param enterState the state to enter now
	 * @param code some user defined code to use instead default one
	 *
	 */
	public TerminalProduction(Terminal lhs, LexicalKind sub, String inState, String regexp, String enterState, String code) {
		super(ProductionKind.TERMINAL, sub, inState, regexp, enterState, code);
		this.lhs = lhs;
	}

	@Override
	public Terminal getLhs() {
		return lhs;
	}

}
