package fr.uha.hassenforder.jcupnflex.model;

/**
 * Borrowed from JCup 
 * 
 * This class represents a terminal symbol in the grammar.
 * It inherits from GrammarSymbol
 * 
 * Precedence gives the priority level
 * Associativity gives the allowed associativity
 * Helps to solve shift/reduce or reduce/reduce conflicts
 * 
 * Special Terminals such as EOF and error are defined here
 * 
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */
public class Terminal extends GrammarSymbol {

	private int priority = -1;
	private Associativity associativity = Associativity.NO;

	/**
	 * Full constructor.
	 * 
	 * @param name the name of the terminal.
	 * @param type the type of the terminal.
	 * @param associativity the associativity of the terminal.
	 * @param priority the level of the priority for the terminal.
	 * @param index the index of the terminal.
	 */
	public Terminal(String name, String type, int priority, Associativity associativity) {
		this(SymbolKind.TERMINAL, name, type, priority, associativity);
	}

	/**
	 * Constructor for terminal without precedence
     *
	 * @param name the name of the terminal.
	 * @param type the type of the terminal.
	 * @param index the index of the terminal.
	 */

	public Terminal(String name, String type) {
		this(name, type, -1, Associativity.NO);
	}

	/**
	 * Constructor with type less terminal.
	 * 
	 * @param name the name of the terminal.
	 * @param index the index of the terminal.
	 */
	public Terminal(String name) {
		this(name, null);
	}

	/**
	 * Full constructor for child regexp
	 * 
	 * @param name the name of the terminal.
	 * @param type the type of the terminal.
	 * @param priority the level of the priority for the terminal.
	 * @param associativity the associativity of the terminal.
	 * @param index the index of the terminal.
	 */
	protected Terminal(SymbolKind kind, String name, String type, int priority, Associativity associativity) {
		super(kind, name, type);
		this.priority = priority;
		this.associativity = associativity;
	}

	public int getPriority() {
		return priority;
	}

	public Associativity getAssociativity() {
		return associativity;
	}

	public void setPrecedence(int priority, Associativity associativity) {
		this.priority = priority;
		this.associativity = associativity;
	}

}
