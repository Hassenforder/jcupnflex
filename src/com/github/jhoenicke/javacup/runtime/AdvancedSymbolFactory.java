package com.github.jhoenicke.javacup.runtime;

/**
 * Implementation for SymbolFactory2, creates advanced Symbols based on enum and
 * with line/column information.
 *
 * For compatibility with SimpleFactory this factory pretends implements SymbolFactory
 * but throws Error to break it at runtime
 * 
 * @version last updated 12 june 2022
 * @author Michel Hassenforder
 */

public class AdvancedSymbolFactory implements SymbolFactory2, SymbolFactory {

	/**
	 * Location of a symbol based on line and column in the input file
	 * It is an unmutable location
	 */
	public static class Location {

		private int line, column;

		public Location(int line, int column) {
			this.line = line;
			this.column = column;
		}

		public int getColumn() {
			return column;
		}

		public int getLine() {
			return line;
		}

		public String toString() {
			return line + "/" + column;
		}

	}

	/**
	 * AdvancedSymbol based on the generic Enum
	 * so real enum can be Sym or ETerminal or ENonterminal
	 * with detailed left most and right most locations
	 * unmutable object
	 * 
	 * Only Locations are getable.
	 * 
	 * All ctor are protected and should only be used by the factory
	 */
	public static class AdvancedSymbol extends Symbol {

		private Enum<?> id;
		private Location left, right;

		protected AdvancedSymbol(Enum<?> id) {
			super(id.ordinal());
			this.id = id;
		}

		protected AdvancedSymbol(Enum<?> id, Object value) {
			super(id.ordinal(), value);
			this.id = id;
		}

		protected AdvancedSymbol(Enum<?> id, int state) {
			super(id.ordinal(), state);
			this.id = id;
		}

		protected AdvancedSymbol setLeft(Location left) {
			this.left = left;
			return this;
		}

		protected AdvancedSymbol setRight(Location right) {
			this.right = right;
			return this;
		}

		protected AdvancedSymbol setRight(Symbol right) {
			this.right = ((AdvancedSymbol) right).right;
			return this;
		}

		public Location getLeft() {
			return left;
		}

		public Location getRight() {
			return right;
		}

		public String toString() {
			StringBuilder tmp = new StringBuilder();
			tmp.append("Symbol: ");
			tmp.append(id.name());
			if (value != null) {
				tmp.append("==\"");
				tmp.append(value);
				tmp.append("\"");
			}
			if (left != null && right != null) {
				tmp.append("(");
				tmp.append(left);
				tmp.append(" - ");
				tmp.append(right);
				tmp.append(")");
			}
			return tmp.toString();
		}

	}

	/**
	 * @see SymbolFactory2#newSymbol()
	 */
	public Symbol newSymbol(Enum<?> id, Location left, Location right, Object value) {
		AdvancedSymbol symbol = new AdvancedSymbol(id, value);
		symbol.setLeft(left);
		symbol.setRight(right);
		return symbol;
	}

	/**
	 * @see SymbolFactory2#newSymbol()
	 */
	public Symbol newSymbol(Enum<?> id, Location left, Location right) {
		AdvancedSymbol symbol = new AdvancedSymbol(id);
		symbol.setLeft(left);
		symbol.setRight(right);
		return symbol;
	}

	/**
	 * @see SymbolFactory2#newSymbol()
	 */
	public Symbol newSymbol(Enum<?> id, Symbol left, Symbol right, Object value) {
		AdvancedSymbol symbol = new AdvancedSymbol(id, value);
		if (left != null)
			symbol.setLeft(((AdvancedSymbol) left).getLeft());
		if (right != null)
			symbol.setRight(((AdvancedSymbol) right).getRight());
		return symbol;
	}

	/**
	 * @see SymbolFactory2#newSymbol()
	 */
	public Symbol newSymbol(Enum<?> id, Symbol left, Symbol right) {
		AdvancedSymbol symbol = new AdvancedSymbol(id);
		if (left != null)
			symbol.setLeft(((AdvancedSymbol) left).getLeft());
		if (right != null)
			symbol.setRight(((AdvancedSymbol) right).getRight());
		return symbol;
	}

	/**
	 * @see SymbolFactory2#newSymbol()
	 */
	public Symbol newSymbol(Enum<?> id) {
		return new AdvancedSymbol(id);
	}

	/**
	 * @see SymbolFactory2#newSymbol()
	 */
	public Symbol newSymbol(Enum<?> id, Object value) {
		return new AdvancedSymbol(id, value);
	}

	/**
	 * internal hack to create an EOF as an Enum
	 * reply on the fact that it should ever be the first in the generated ETerminal
	 */
	private static enum SpecialTerminal {
		EOF,
	}

	/**
	 * internal hack to create START and ERROR as an Enum
	 * reply on the fact that they should ever be the two firsts in the generated ENonterminal
	 */
	private static enum SpecialNonterminal {
		START, ERROR,
	}

	/**
	 * @see SymbolFactory2#startSymbol()
	 */
	public Symbol startSymbol() {
		return new AdvancedSymbol(SpecialNonterminal.START, 0);
	}

	/**
	 * @see SymbolFactory2#endSymbol()
	 */
	public Symbol endSymbol() {
		return new AdvancedSymbol(SpecialTerminal.EOF);
	}

	/**
	 * @see SymbolFactory2#errorSymbol()
	 */
	public Symbol errorSymbol(Symbol left, Symbol right) {
		return newSymbol(SpecialNonterminal.ERROR, left, right);
	}

	/**
	 * @see SymbolFactory#newSymbol()
	 */
	@Override
	public Symbol newSymbol(String name, int id, Symbol left, Symbol right, Object value) {
		throw new Error("Old api to avoid with this factory");
	}

	/**
	 * @see SymbolFactory#newSymbol()
	 */
	@Override
	public Symbol newSymbol(String name, int id, Symbol left, Symbol right) {
		throw new Error("Old api to avoid with this factory");
	}

	/**
	 * @see SymbolFactory#newSymbol()
	 */
	@Override
	public Symbol newSymbol(String name, int id) {
		throw new Error("Old api to avoid with this factory");
	}

	/**
	 * @see SymbolFactory#newSymbol()
	 */
	@Override
	public Symbol newSymbol(String name, int id, Object value) {
		throw new Error("Old api to avoid with this factory");
	}

	/**
	 * @see SymbolFactory#newSymbol()
	 */
	@Override
	public Symbol startSymbol(String name, int id, int state) {
		throw new Error("Old api to avoid with this factory");
	}

}
