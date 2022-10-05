package fr.uha.hassenforder.jcupnflex.model;

import java.util.List;

import fr.uha.hassenforder.jcupnflex.model.OperatorPart.OperatorKind;

public class Factory implements IFactory {
	
	@Override
	public GrammarSymbol createSymbol(SymbolKind kind, String name, String type) {
		switch (kind) {
		case NONTERMINAL:	return new NonTerminal(name, type);
		case TERMINAL:		return new Terminal(name, type);
		case REGEXP:		return null;
		default:			return null;
		}
	}

	static private int instance = 0;

	static private String buildRegExpName () {
		StringBuilder tmp = new StringBuilder ();
		tmp.append("__REGEXP_");
		tmp.append(++instance);
		tmp.append("__");
		return tmp.toString();
	}

	@Override
	public GrammarSymbol createRegexp(String content) {
		return new RegExp (buildRegExpName(), content);
	}

	@Override
	public Production createNonTerminalProduction(GrammarSymbol lhs, List<ProductionPart> rhs, GrammarSymbol precedence) {
		ProductionPart list = createListPart(rhs);
		return new NonTerminalProduction((NonTerminal) lhs, list, (Terminal) precedence);
	}

	@Override
	public Production createTerminalProduction(GrammarSymbol lhs, String regexp, String code) {
		return new TerminalProduction((Terminal) lhs, regexp, code);
	}

	@Override
	public Production createTerminalProduction(GrammarSymbol lhs, String from, String to, String code) {
		return new TerminalProduction((Terminal) lhs, from, to, code);
	}

	@Override
	public ProductionPart createSimplePart(ProductionPart child, String label) {
		return new SimplePart(child, label);
	}

	@Override
	public ProductionPart createSymbolPart(GrammarSymbol symbol) {
		return new SymbolPart(symbol);
	}

	@Override
	public ProductionPart createActionPart(String code) {
		return new ActionPart(code);
	}

	@Override
	public ProductionPart createListPart(List<ProductionPart> children) {
		return new ListPart(children);
	}

	@Override
	public ProductionPart createMany0Part(ProductionPart child) {
		return new OperatorPart(OperatorKind.MANY_0, child);
	}

	@Override
	public ProductionPart createMany1Part(ProductionPart child) {
		return new OperatorPart(OperatorKind.MANY_1, child);
	}

	@Override
	public ProductionPart createOptionalPart(ProductionPart child) {
		return new OperatorPart(OperatorKind.OPTIONAL, child);
	}

	@Override
	public ProductionPart createGroupPart(List<ProductionPart> children) {
		return new OperatorPart(OperatorKind.GROUP, new ListPart(children));
	}

}
