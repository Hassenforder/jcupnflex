package fr.uha.hassenforder.jcupnflex.writer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.uha.hassenforder.jcupnflex.model.Directive;
import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
import fr.uha.hassenforder.jcupnflex.model.Production;
import fr.uha.hassenforder.jcupnflex.model.ProductionKind;
import fr.uha.hassenforder.jcupnflex.model.RegExp;
import fr.uha.hassenforder.jcupnflex.model.TerminalProduction;

public class FlexWriter extends AbstractWriter {

	private Map<ProductionKind, Map<Integer, List<TerminalProduction>>> ordered;

	public FlexWriter(Grammar grammar, DirectiveSet directives, File outputFile) {
		super(grammar, directives, outputFile);
	}

	private StringBuilder writeProperty(String header, String value) {
		return writeProperty (header, value, "");
	}

	private StringBuilder writeCode(String content) {
		return writeCode ("", "%{", content, "%}");
	}

	private void emitFreeCode() {
		// TODO Auto-generated method stub
		
	}

	private void emitHeader() {
		String packageName = directives.getSingleValue(Directive.PACKAGE_NAME);
		appendLine(writeProperty ("%package", packageName));
		appendLine(writeProperty ("%class", "Lexer"));
		appendLine(writeProperty ("%public", ""));
		appendLine(writeProperty ("%cupJHMH", ""));
	}

	/**
	 * we have to emit the code proposed by the user but also all buffers used by region
	 * 
	 * private StringBuilder $content;
     * private int $line, $column;
     *
     * where $ represents the region name
	 */
	private void emitFlexCodes() {
		String code = directives.getSingleValue(Directive.SCAN_CODE);
		appendLine(writeCode (code));
		if (ordered.containsKey(ProductionKind.TERMINAL_REGION)) {
			for (List<TerminalProduction> terminals : ordered.get(ProductionKind.TERMINAL_REGION).values()) {
				for (TerminalProduction terminal : terminals) {
					StringBuilder content = new StringBuilder ();
					content.append("\tprivate StringBuilder ");
					content.append(terminal.getRegion());
					content.append("$Content;\n");
					content.append("\tprivate int ");
					content.append(terminal.getRegion());
					content.append("$Line, ");
					content.append(terminal.getRegion());
					content.append("$Column;\n");
					appendLine(writeCode (content.toString()));
				}
			}
		}
	}

	private StringBuilder writeMacro(String name, String regexp) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(name);
		tmp.append(" = ");
		switch (regexp.charAt(0)) {
		case '\'' :
			tmp.append(regexp);
			break;
		case '\"' :
			tmp.append(regexp);
			break;
		case '`' :
			tmp.append(regexp.substring(1, regexp.length()-1));
			break;
		default :
			break;
		}
		return tmp;
	}

	private void emitMacros() {
		for (List<TerminalProduction> terminals : ordered.get(ProductionKind.TERMINAL_SIMPLE).values()) {
			for (TerminalProduction terminal : terminals) {
				if (! "void".equals(terminal.getLhs().getType())) continue;
				appendLine(writeMacro (terminal.getLhs().getName(), terminal.getRegexp()));
			}
		}
	}

	private void emitStates() {
		if (ordered.containsKey(ProductionKind.TERMINAL_REGION)) {
			for (List<TerminalProduction> terminals : ordered.get(ProductionKind.TERMINAL_REGION).values()) {
				for (TerminalProduction terminal : terminals) {
					StringBuilder content = new StringBuilder ();
					content.append("%state ");
					content.append(terminal.getRegion());
					content.append("$State");
					appendLine(content);
				}
			}
		}
	}

	private StringBuilder writeSimpleRegExp(String type, String name, String regexp, String code) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		switch (regexp.charAt(0)) {
		case '\'' :
			tmp.append(regexp);
			break;
		case '\"' :
			tmp.append(regexp);
			break;
		case '`' :
			tmp.append(regexp.substring(1, regexp.length()-1));
			break;
		default :
			break;
		}
		tmp.append("\t\t");
		tmp.append("{ ");
		if (code != null) {
			tmp.append(code);
		} else {
			tmp.append("return symbol(ETerminal.");
			tmp.append(name);
			if (type != null) {
				if ("String".equals(type)) {
					tmp.append(", yytext()");
				}
				if ("Integer".equals(type)) {
					tmp.append(", Integer.parseInt(yytext())");
				}
				if ("Long".equals(type)) {
					tmp.append(", Long.parseLong(yytext())");
				}
				if ("Float".equals(type)) {
					tmp.append(", Float.parseFloat(yytext())");
				}
				if ("Double".equals(type)) {
					tmp.append(", Double.parseDouble(yytext())");
				}
			}
			tmp.append(");");
		}
		tmp.append(" }");
		return tmp;
	}

	/*
	 * we have to build a line like :
	 * regexp 	{ $content = new StringBuilder(); $line=yyline; $column=yycolumn; yybegin($state); }
	 * 
	 * where $ represent a unique name base on the terminalname with a dollar sign and the remaining text
	 */
	private StringBuilder writeRegionStartRegExp(String regexp, String name) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		switch (regexp.charAt(0)) {
		case '\'' :
			tmp.append(regexp);
			break;
		case '\"' :
			tmp.append(regexp);
			break;
		case '`' :
			tmp.append(regexp.substring(1, regexp.length()-1));
			break;
		default :
			break;
		}
		tmp.append("\t\t");
		tmp.append("{ ");
		tmp.append(name);
		tmp.append("$Content");
		tmp.append(" = new StringBuilder(); ");
		tmp.append(name);
		tmp.append("$Line");
		tmp.append(" = yyline; ");
		tmp.append(name);
		tmp.append("$column");
		tmp.append(" = yycolumn; ");
		tmp.append("yybegin (");
		tmp.append(name);
		tmp.append("$State");
		tmp.append("); ");
		tmp.append("}");
		return tmp;
	}

	/*
	 * we have to build a line like :
     *   regexp { yybegin(YYINITIAL); return symbol(ETerminal.$, $line+1, $column+1, yyline+1, yycolumn+1, $content.toString()); }
	 * 
	 * where $ represent a unique name base on the terminal name with a dollar sign and the remaining text
	 */
	private StringBuilder writeRegionEndRegExp(String regexp, String name) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  ");
		switch (regexp.charAt(0)) {
		case '\'' :
			tmp.append(regexp);
			break;
		case '\"' :
			tmp.append(regexp);
			break;
		case '`' :
			tmp.append(regexp.substring(1, regexp.length()-1));
			break;
		default :
			break;
		}
		tmp.append("\t\t");
		tmp.append("{ ");
		tmp.append("yybegin (YYINITIAL); ");
		tmp.append("return symbol(ETerminal.");
		tmp.append(name);
		tmp.append(", ");
		tmp.append(name);
		tmp.append("$Line");
		tmp.append("+1 ");
		tmp.append(", ");
		tmp.append(name);
		tmp.append("$Column");
		tmp.append("+1 ");
		tmp.append(", yyline+1 ");
		tmp.append(", yycolumn+1 ");
		tmp.append("$Content.toString()");
		tmp.append("); ");
		tmp.append("}");
		return tmp;
	}

	/*
	 * we have to build a line like :
	 * [^]			{ $content.append(yytext()); }
	 * 
	 * where $ represent a unique name base on the terminal name with a dollar sign and the remaining text
	 */
	private StringBuilder writeRegionCollectRegExp(String regexp, String name) {
		StringBuilder tmp = new StringBuilder();
		tmp.append("  [^]");
		tmp.append("\t\t");
		tmp.append("{ ");
		tmp.append("$Content.append(yytext()); ");
		tmp.append("}");
		return tmp;
	}

	private int count(String regexp, char c) {
		int count = 0;
		for (int i = 0; i < regexp.length(); i++) {			
			if (regexp.charAt(i) == c) ++count;
		}
		return count;
	}

	private int computeComplexity(String regexp) {
		int starCount = count(regexp, '*');
		int plusCount = count(regexp, '+');
		int charCount = regexp.length();
		return starCount*100+plusCount*50+charCount;
	}

	/*
	 * first map is keyed by region name :
	 *    for simple it will be YYINITIAL
	 *    for region it will be the name of the region
	 *    for fallback it will be empty string
	 * second map is keyed by complexity of the production
	 * list is all productions with the same complexity
	 */
	protected Map<ProductionKind, Map<Integer, List<TerminalProduction>>> sortAndOrderProductionByComplexity(Collection<? extends Production> original) {
		Map<ProductionKind, Map<Integer, List<TerminalProduction>>> ordered = new TreeMap<>();
		for (Production production : original) {
			TerminalProduction terminal = null;
			if (production instanceof TerminalProduction) terminal = (TerminalProduction) production;
			if (terminal == null) continue;
			int complexity = 0;
			switch (production.getKind()) {
			case TERMINAL_REGION:
				complexity = computeComplexity (terminal.getFrom());
				break;
			case TERMINAL_SIMPLE:
				complexity = computeComplexity (terminal.getRegexp());
				break;
			default:
				break;
			}
			Map<Integer, List<TerminalProduction>> map = ordered.get(production.getKind());
			if (map == null) {
				map = new TreeMap<>();
				ordered.put (production.getKind(), map);
			}
			List<TerminalProduction> list = map.get(complexity);
			if (list == null) {
				list = new ArrayList<>();
				map.put (complexity, list);
			}
			list.add(terminal);
		}
		return ordered;
	}

	/**
	 * 
	 * first we sort all TerminalProductions by key/complexity
	 * 
	 */
	private void emitRules() {
		appendLine("<YYINITIAL> {");
		for (RegExp regexp : grammar.getRegexps().values()) {
			appendLine (writeSimpleRegExp(null, regexp.getName(), regexp.getRegexp(), null));
		}
		if (ordered.containsKey(ProductionKind.TERMINAL_SIMPLE)) {
			for (List<TerminalProduction> terminals : ordered.get(ProductionKind.TERMINAL_SIMPLE).values()) {
				for (TerminalProduction terminal : terminals) {
					if ("void".equals(terminal.getLhs().getType())) continue;
					appendLine(writeSimpleRegExp (terminal.getLhs().getType(), terminal.getLhs().getName(), terminal.getRegexp(), terminal.getCode()));
				}
			}
		}
		if (ordered.containsKey(ProductionKind.TERMINAL_REGION)) {
			for (List<TerminalProduction> terminals : ordered.get(ProductionKind.TERMINAL_REGION).values()) {
				for (TerminalProduction terminal : terminals) {
					if ("void".equals(terminal.getLhs().getType())) continue;
					appendLine(writeRegionStartRegExp (terminal.getFrom(), terminal.getRegion()));
				}
			}
		}
		appendLine("}");
		newLine();
		if (ordered.containsKey(ProductionKind.TERMINAL_REGION)) {
			for (List<TerminalProduction> terminals : ordered.get(ProductionKind.TERMINAL_REGION).values()) {
				for (TerminalProduction terminal : terminals) {
					StringBuilder tmp = new StringBuilder();
					tmp.append("<");
					tmp.append(terminal.getRegion());
					tmp.append(">");
					tmp.append(" {");
					appendLine(tmp);
					appendLine(writeRegionEndRegExp (terminal.getTo(), terminal.getRegion()));
					appendLine(writeRegionCollectRegExp (null, terminal.getRegion()));
					appendLine("}");
					newLine();
				}
			}
		}
		newLine();
		appendLine("[^]\t\t\t { fallback(); }");
	}

	public void generate() {
		if (! open()) return;
		ordered = sortAndOrderProductionByComplexity (grammar.getProductions());
		emitFreeCode ();
		newLine();
		appendLine("%%");
		newLine ();
		emitHeader ();
		newLine ();
		emitFlexCodes ();
		newLine ();
		emitMacros ();
		newLine ();
		emitStates();
		newLine ();
		appendLine("%%");
		newLine ();
		emitRules();
		newLine ();
		close();
	}

}
