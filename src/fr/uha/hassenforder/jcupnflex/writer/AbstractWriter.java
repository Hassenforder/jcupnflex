package fr.uha.hassenforder.jcupnflex.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeMap;

import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
import fr.uha.hassenforder.jcupnflex.model.GrammarSymbol;
import fr.uha.hassenforder.jcupnflex.model.Production;
import fr.uha.hassenforder.jcupnflex.model.Terminal;

public class AbstractWriter {

	protected Grammar grammar;
	protected DirectiveSet directives;
	private File outputFile;
	private BufferedWriter output;
	private static final int TAB_SIZE = 4;

	public AbstractWriter(Grammar grammar, DirectiveSet directives, File outputFile) {
		super();
		this.grammar = grammar;
		this.directives = directives;
		this.outputFile = outputFile;
	}

	protected boolean open() {
		try {
			output = new BufferedWriter(new FileWriter(outputFile));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			output = null;
			return false;
		}
	}

	protected boolean close() {
		try {
			if (output != null) output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected void appendLine(StringBuilder content) {
		if (output == null) return;
		if (content == null) return;
		if (content.isEmpty()) return;
		try {
			output.append(content);
			output.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void appendLine(String content) {
		if (output == null) return;
		if (content == null) return;
		if (content.isEmpty()) return;
		try {
			output.append(content);
			output.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void newLine() {
		if (output == null) return;
		try {
			output.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected StringBuilder writeProperty(String header, String value, String closer) {
		if (output == null) return null;
		if (value == null) return null;
		if (header == null) return null;
		StringBuilder tmp = new StringBuilder();
		tmp.append(header);
		tmp.append(" ");
		tmp.append(value);
		tmp.append(closer);
		return tmp;
	}

	protected StringBuilder writeCode(String header, String opener, String content, String closer) {
		if (output == null) return null;
		if (header == null) return null;
		if (content == null) return null;
		StringBuilder tmp = new StringBuilder();
		tmp.append(header);
		tmp.append(opener);
		tmp.append("\n");
		tmp.append(content);
		tmp.append("\n");
		tmp.append(closer);
		return tmp;
	}

	protected int normalizeLength(int length) {
		return ((length / TAB_SIZE) + 1) * TAB_SIZE;
	}

	protected StringBuilder writeTabulation(int longuestLhs, int length) {
		StringBuilder tmp = new StringBuilder();
		for (int i = length; i < longuestLhs; i += TAB_SIZE) {
			tmp.append("\t");
		}
		return tmp;
	}

	protected TreeMap<String, List<Production>> orderSymbolsByName(Collection<? extends Production> original) {
		TreeMap<String, List<Production>> ordered = new TreeMap<>();
		for (Production production : original) {
			String name = production.getLhs ().getName();
			List<Production> list = ordered.get(name);
			if (list == null) {
				list = new ArrayList<>();
				ordered.put (name, list);
			}
			list.add(production);
		}
		return ordered;
	}

	protected TreeMap<String, List<GrammarSymbol>> orderSymbolsByType(Collection<? extends GrammarSymbol> original) {
		TreeMap<String, List<GrammarSymbol>> ordered = new TreeMap<>();
		for (GrammarSymbol symbol : original) {
			String type = symbol.getType();
			if (type == null) type = "";
			if (type.equals("void")) continue;
			List<GrammarSymbol> list = ordered.get(type);
			if (list == null) {
				list = new ArrayList<>();
				ordered.put (type, list);
			}
			list.add(symbol);
		}
		return ordered;
	}

	protected TreeMap<Integer, List<Terminal>> orderTerminalsByPriority(Collection<Terminal> original) {
		TreeMap<Integer, List<Terminal>> ordered = new TreeMap<>();
		for (Terminal terminal : original) {
			int priority = terminal.getPriority();
			if (priority == -1) continue;
			List<Terminal> list = ordered.get(priority);
			if (list == null) {
				list = new ArrayList<>();
				ordered.put (priority, list);
			}
			list.add(terminal);
		}
		return ordered;
	}

}