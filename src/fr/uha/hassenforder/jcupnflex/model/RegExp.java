package fr.uha.hassenforder.jcupnflex.model;

/**
 * This class represents an inlined terminal symbol in the grammar
 * It inherits from Terminal (it is a terminal) but it contains also
 * the regexp it defines
 * By default the inlined regexp could be a String which represents the scanned content
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */
public class RegExp extends Terminal {

	/** the regexp used to defined it */
	private String regexp;
	
	/**
	 * Full constructor.
	 * 
	 * @param regexp the regexp defining the terminal.
	 * @param priority the level of the priority for the terminal.
	 * @param associativity the associativity of the terminal.
	 * @param index the index of the terminal.
	 */
	public RegExp(String name, String regexp, int priority, Associativity associativity) {
		super(SymbolKind.REGEXP, name, "String", priority, associativity);
		this.regexp = regexp;
	}

	/**
	 * Constructor for terminal without precedence
     *
	 * @param regexp the regexp defining the terminal.
	 * @param index the index of the terminal.
	 */

	public RegExp(String name, String regexp) {
		this(name, regexp, -1, Associativity.NO);
	}

	public String getRegexp() {
		return regexp;
	}

}
