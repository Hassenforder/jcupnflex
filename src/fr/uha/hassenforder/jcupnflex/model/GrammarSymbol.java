package fr.uha.hassenforder.jcupnflex.model;

/**
 * Borrowed from JCup 
 * 
 * This abstract class serves as the base class for grammar symbols (i.e., both
 * terminals non-terminals but also new like regexp and action).
 * Each symbol has a name, and a string giving the type of object that
 * the symbol will be represented by on the runtime parse stack.
 * 
 * In addition, each symbol maintains a use count in order to detect symbols
 * that are declared but never used.
 *
 * @see fr.uha.hassenforder.jcupnflex.model.Terminal
 * @see fr.uha.hassenforder.jcupnflex.model.RegExp
 * @see fr.uha.hassenforder.jcupnflex.model.NonTerminal
 * @see fr.uha.hassenforder.jcupnflex.model.ActionPart
 * @see fr.uha.hassenforder.jcupnflex.model.OperatorPart
 * 
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */
public abstract class GrammarSymbol { //implements Comparable<GrammarSymbol> {

	/** kind to helps to understand what is really the symbol*/
	private SymbolKind kind;
	
	/** String for the human readable name of the symbol. */
	private String name;

	/** String for the type of object used for the symbol on the parse stack. */
	private String type;

	/** Count of how many times the symbol appears in productions. */
	private int useCount;

	/**
	 * Full constructor.
	 * 
	 * @param name		the name of the symbol.
	 * @param type		a string with the type name.
	 * @param index		the index of the symbol.
	 */
	protected GrammarSymbol(SymbolKind kind, String name, String type) {
		this.kind = kind;
		if (name == null) name = "";
		this.name = name;
		this.type = type;
		this.useCount = 0;
	}

	public SymbolKind getKind() {
		return kind;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getUseCount() {
		return useCount;
	}

	/** Increment the use count. */
	public void incrementUseCount() {
		useCount++;
	}

	public String toString() {
		StringBuilder tmp = new StringBuilder();
		tmp.append(getName());
		if (getType() != null) {
			tmp.append("<");
			tmp.append(getType());
			tmp.append(">");			
		}
		return tmp.toString();
	}

}
