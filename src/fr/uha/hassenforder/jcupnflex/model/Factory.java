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
		case STATE:			return new State (name, type);
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
	public Production createSimpleTerminalProduction(GrammarSymbol lhs, String regexp, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.SIMPLE, "", regexp, regexp, code);
	}

	@Override
	public Production createEnterStateTerminalProduction(GrammarSymbol lhs, String regexp, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.ENTER_STATE, "", regexp, null, code);
	}

	@Override
	public Production createEnterStateTerminalProduction(GrammarSymbol lhs, String regexp, String state, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.ENTER_STATE, "", regexp, state, code);
	}

	@Override
	public Production createEnterStateTerminalProduction(GrammarSymbol lhs, String inState, String regexp, String state, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.ENTER_STATE, inState, regexp, state, code);
	}

	@Override
	public Production createInStateTerminalProduction(GrammarSymbol lhs, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.IN_STATE, lhs.getName(), null, null, code);
	}

	@Override
	public Production createInStateTerminalProduction(GrammarSymbol lhs, String inState, String regexp, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.IN_STATE, inState, regexp, null, code);
	}

	@Override
	public Production createLeaveStateTerminalProduction(GrammarSymbol lhs, String regexp, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.LEAVE_STATE, lhs.getName(), regexp, null, code);
	}
	
	@Override
	public Production createLeaveStateTerminalProduction(GrammarSymbol lhs, String inState, String regexp, String code) {
		Terminal terminal = (Terminal) lhs;
		return new TerminalProduction(terminal, TerminalKind.LEAVE_STATE, inState, regexp, null, code);
	}

	@Override
	public Production createStateProduction(GrammarSymbol lhs, String from, String to, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Production createStateProduction(GrammarSymbol lhs, String in, String from, String to, String code) {
		// TODO Auto-generated method stub
		return null;
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
