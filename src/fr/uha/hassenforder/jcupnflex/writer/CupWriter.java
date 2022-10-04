package fr.uha.hassenforder.jcupnflex.writer;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.uha.hassenforder.jcupnflex.ErrorManager;
import fr.uha.hassenforder.jcupnflex.model.ActionPart;
import fr.uha.hassenforder.jcupnflex.model.Associativity;
import fr.uha.hassenforder.jcupnflex.model.Directive;
import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
import fr.uha.hassenforder.jcupnflex.model.GrammarSymbol;
import fr.uha.hassenforder.jcupnflex.model.ListPart;
import fr.uha.hassenforder.jcupnflex.model.NonTerminal;
import fr.uha.hassenforder.jcupnflex.model.NonTerminalProduction;
import fr.uha.hassenforder.jcupnflex.model.OperatorPart;
import fr.uha.hassenforder.jcupnflex.model.Production;
import fr.uha.hassenforder.jcupnflex.model.ProductionKind;
import fr.uha.hassenforder.jcupnflex.model.ProductionPart;
import fr.uha.hassenforder.jcupnflex.model.SimplePart;
import fr.uha.hassenforder.jcupnflex.model.SymbolPart;
import fr.uha.hassenforder.jcupnflex.model.Terminal;

public class CupWriter extends AbstractWriter {

	public CupWriter(Grammar grammar, DirectiveSet directives, File outputFile) {
		super(grammar, directives, outputFile);
	}

	private StringBuilder writeProperty(String header, String value) {
		return writeProperty (header, value, ";");
	}

	private StringBuilder writeCode(String header, String content) {
		return writeCode (header, " {:", content, ":}");
	}

	protected StringBuilder writeListSymbols(String header, String property, Collection<? extends GrammarSymbol> symbols) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(header);
		tmp.append(" ");
		if (property != null) {
			tmp.append(property);
		}
		tmp.append(" ");
		boolean first = true;
		for (GrammarSymbol symbol : symbols) {
			if ("error".equals(symbol.getName())) continue;
			if ("EOF".equals(symbol.getName())) continue;
			if (! first) tmp.append(", ");
			else first = false;
			tmp.append(symbol.getName());
		}
		tmp.append(";");
		return tmp;
	}

	private StringBuilder writePart(ProductionPart part) {
		StringBuilder tmp = new StringBuilder();
		switch (part.getKind()) {
		case ACTION:
			ActionPart action = (ActionPart) part;
			tmp.append("{: ");
			tmp.append(action.getCode());
			tmp.append(" :}");
			break;
		case LIST:
			ListPart list = (ListPart) part;
			for (ProductionPart child : list.getChildren()) {
				tmp.append(writePart(child));
				tmp.append(" ");
			}
			break;
		case OPERATOR:
			OperatorPart operator = (OperatorPart) part;
			switch (operator.getOperator()) {
			case GROUP:
				tmp.append("( ");
				tmp.append(writePart(operator.getChild()));
				tmp.append(" )");
				break;
			case MANY_0:
				tmp.append(writePart(operator.getChild()));
				tmp.append(" *");
				break;
			case MANY_1:
				tmp.append(writePart(operator.getChild()));
				tmp.append(" +");
				break;
			case OPTIONAL:
				tmp.append(writePart(operator.getChild()));
				tmp.append(" ?");
				break;
			default:
				break;
			}
			break;
		case REGEXP:
			SymbolPart regexp = (SymbolPart) part;
			tmp.append(regexp.getSymbol().getName());
			break;
		case SIMPLE:
			SimplePart simple = (SimplePart) part;
			tmp.append(writePart(simple.getChild()));
			break;
		case SYMBOL:
			SymbolPart symbol = (SymbolPart) part;
			tmp.append(symbol.getSymbol().getName());
			break;
		default:
			break;
		}
		String label = part.getLabel();
		if (label != null) {
			tmp.append(":");
			tmp.append(part.getLabel());
		}
		return tmp;
	}

	protected StringBuilder writeListProductions(int longuestLhs, String lhs, Collection<? extends Production> productions) {
		StringBuilder tmp = new StringBuilder();
		tmp.append(lhs);
		tmp.append(writeTabulation(longuestLhs, lhs.length()));
		tmp.append("::=\t");
		boolean first = true;
		for (Production production : productions) {
			if (production.getKind() != ProductionKind.NONTERMINAL) continue;
			NonTerminalProduction nonterminal = (NonTerminalProduction) production;
			if (! first) {
				tmp.append(writeTabulation(longuestLhs, 0));
				tmp.append("|\t");
			} else first = false;
			tmp.append(writePart (nonterminal.getRhs()));
			Terminal precedence = nonterminal.getPrecedence();
			if (precedence != null) {
				tmp.append(" %prec ");
				tmp.append(precedence.getName());
			}
			tmp.append("\n");
		}
		tmp.append(writeTabulation(longuestLhs, 0));
		tmp.append(";");
		tmp.append("\n");
		return tmp;
	}

	private void emitNonTerminalProductions() {
		TreeMap<String, List<Production>> ordered = orderSymbolsByName (grammar.getProductions());
		int longuestLhs = 0;
		for (Map.Entry<String, List<Production>> entry : ordered.entrySet()) {
			if (grammar.getNonTerminal(entry.getKey()) == null) continue;
			int length = entry.getKey().length();
			if (longuestLhs < length) longuestLhs = length;
		}
		longuestLhs = normalizeLength(longuestLhs);
		for (Map.Entry<String, List<Production>> entry : ordered.entrySet()) {
			if (grammar.getNonTerminal(entry.getKey()) == null) continue;
			appendLine(writeListProductions (longuestLhs, entry.getKey(), entry.getValue()));
		}
	}

	private void emitStartWith() {
		NonTerminal startWith = grammar.getStartSymbol();
		if (startWith == null) return;
		appendLine(writeProperty("start with", startWith.getName()));
	}

	private void emitExpect() {
		String expect = directives.getSingleValue(Directive.EXPECT);
		appendLine(writeProperty ("expect", expect));
	}

	private Associativity getAssociativity (List<Terminal> terminals) {
		Associativity found = null;
		for (Terminal terminal : terminals) {
			if (found == null) found = terminal.getAssociativity();
			if (found != terminal.getAssociativity()) {
				ErrorManager.getManager().emit_warning("Priority "+terminal.getPriority()+" with mixed associativity");
			}
		}
		return found;
	}

	private void emitPrecedences() {
		TreeMap<Integer, List<Terminal>> ordered = orderTerminalsByPriority (grammar.getTerminals().values());
		for (Map.Entry<Integer, List<Terminal>> entry : ordered.entrySet()) {
			Associativity associativity = getAssociativity(entry.getValue());
			String property = null;
			switch (associativity) {
			case LEFT: property = "left"; break;
			case NONE: property = "noassoc"; break;
			case RIGHT:property ="right"; break;
			default: break;
			}
			appendLine(writeListSymbols ("precedence", property, entry.getValue()));
		}
	}

	private void emitNonTerminals() {
		TreeMap<String, List<GrammarSymbol>> ordered = orderSymbolsByType(grammar.getNonTerminals().values());
		for (Map.Entry<String, List<GrammarSymbol>> entry : ordered.entrySet()) {
			appendLine(writeListSymbols ("nonterminal", entry.getKey(), entry.getValue()));
		}
	}

	private void emitTerminals() {
		TreeMap<String, List<GrammarSymbol>> ordered = orderSymbolsByType(grammar.getTerminals().values());
		for (Map.Entry<String, List<GrammarSymbol>> entry : ordered.entrySet()) {
			appendLine(writeListSymbols ("terminal", entry.getKey(), entry.getValue()));
		}
	}

	private void emitOptions() {
		// TODO Auto-generated method stub
		
	}

	private void emitCupCodes() {
		String code;
		code = directives.getSingleValue(Directive.INIT_WITH_CODE);
		appendLine( writeCode ("init with", code));
		code = directives.getSingleValue(Directive.ACTION_CODE);
		appendLine( writeCode ("action code", code));
		code = directives.getSingleValue(Directive.PARSER_CODE);
		appendLine( writeCode ("parser code", code));
		code = directives.getSingleValue(Directive.SCAN_WITH_CODE);
		appendLine( writeCode ("scan with", code));
		code = directives.getSingleValue(Directive.AFTER_REDUCE_CODE);
		appendLine( writeCode ("after reduce", code));
	}

	private void emitImports() {
		List<String> importNames = directives.getMultipleValues(Directive.IMPORT);
		if (importNames == null) return;
		for (String importName : importNames) {
			appendLine(writeProperty ("import", importName));
		}
	}

	private void emitPackage() {
		String packageName = directives.getSingleValue(Directive.PACKAGE_NAME);
		appendLine(writeProperty ("package", packageName));
	}

	public void generate() {
		if (! open()) return;
		emitPackage ();
		newLine();
		emitImports ();
		newLine ();
		emitOptions();
		newLine ();
		emitCupCodes ();
		newLine ();
		emitTerminals ();
		newLine ();
		emitNonTerminals ();
		newLine ();
		emitPrecedences ();
		newLine ();
		emitExpect ();
		newLine ();
		emitStartWith ();
		newLine ();
		emitNonTerminalProductions();
		close();
	}

}
