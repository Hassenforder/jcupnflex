package fr.uha.hassenforder.jcupnflex.model;

/**
 * 
 * This class represents a production with a terminal as LHS.
 * It inherits from Production
 * It is a lexical rule in the grammar
 * 
 * state	::=	REGEXP TILDA REGEXP						CODE_STRING?    // region from YYINITIAL
 *			|	SYMBOL_STATE REGEXP TILDA REGEXP		CODE_STRING?    // region in another state
 *			;
 *
 * State rule will never capture content it is intent
 *    to switch on the state according to a beginning regexp
 *    to switch off the state according to an ending regexp
 *    
 * The last rule is to embed regions
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public class StateProduction extends Production {

	/** The left hand side non-terminal. */
	private final Terminal lhs;
	private final String inState;
	private final String from;
	private final String to;
	private final String code;
	
	/**
	 * Full constructor for an embedded state
	 * 
	 * @param lhs the terminal as lhs 
	 * @param inState the initial state where to start the region can be null
	 * @param from the regexp to start the region
	 * @param to the regexp to end the region
	 * @param special code to execute
	 */
	public StateProduction(Terminal lhs, String inState, String from, String to, String code) {
		super(ProductionKind.STATE);
		this.lhs = lhs;
		this.inState = inState;
		this.from = from;
		this.to = to;
		this.code = code;
	}

	@Override
	public Terminal getLhs() {
		return lhs;
	}

	public String getInState() {
		return inState;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getCode() {
		return code;
	}

	public String getRegion() {
		return getLhs().getName();
	}

}
