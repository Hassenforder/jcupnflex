package fr.uha.hassenforder.jcupnflex.model;

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
	private final String regexp;
	private final String from;
	private final String to;
	private final String code;
	
	/**
	 * Full constructor for a simple regexp
	 * 
	 * @param lhs the terminal as lhs 
	 * @param rhs the List of ProductionParts terminal as rhs 
	 */
	public TerminalProduction(Terminal lhs, String regexp, String code) {
		super();
		this.lhs = lhs;
		this.regexp = regexp;
		this.from = null;
		this.to = null;
		this.code = code;
	}

	/**
	 * Full constructor for a region regexp
	 * 
	 * @param lhs the terminal as lhs 
	 * @param rhs the List of ProductionParts terminal as rhs 
	 */
	public TerminalProduction(Terminal lhs, String from, String to, String code) {
		super();
		this.lhs = lhs;
		this.regexp = null;
		this.from = from;
		this.to = to;
		this.code = code;
	}

	@Override
	public ProductionKind getKind() {
		if (this.regexp != null) return ProductionKind.TERMINAL_SIMPLE;
		if (this.from != null && this.to != null) return ProductionKind.TERMINAL_REGION;
		return null;
	}

	@Override
	public Terminal getLhs() {
		return lhs;
	}

	public String getRegexp() {
		return regexp;
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
