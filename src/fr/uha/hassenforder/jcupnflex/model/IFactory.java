package fr.uha.hassenforder.jcupnflex.model;

import java.util.List;

public interface IFactory {
	
	// all kind of Symbols
	GrammarSymbol createSymbol (SymbolKind kind, String name, String type);
	GrammarSymbol createRegexp(String content);
	
	// all kind of NonTerminal Productions
	public Production createNonTerminalProduction(GrammarSymbol lhs, List<ProductionPart> rhs, GrammarSymbol precedence);

	// all kind of Terminal Productions
	public Production createSimpleTerminalProduction(GrammarSymbol lhs, String regexp, String code);
	public Production createEnterStateTerminalProduction(GrammarSymbol lhs, String regexp, String code);
	public Production createEnterStateTerminalProduction(GrammarSymbol lhs, String regexp, String state, String code);
	public Production createEnterStateTerminalProduction(GrammarSymbol lhs, String inState, String regexp, String nextState, String code);
	public Production createInStateTerminalProduction(GrammarSymbol lhs, String code);
	public Production createInStateTerminalProduction(GrammarSymbol lhs, String inState, String regexp, String code);
	public Production createLeaveStateTerminalProduction(GrammarSymbol lhs, String regexp, String code);
	public Production createLeaveStateTerminalProduction(GrammarSymbol lhs, String inState, String regexp, String code);

	// all kind of State Productions
	public Production createStateProduction(GrammarSymbol lhs, String from, String to, String code);
	public Production createStateProduction(GrammarSymbol lhs, String in, String from, String to, String code);

	// all kind of Nonterminal Parts
	ProductionPart createSimplePart (ProductionPart child, String label);
	ProductionPart createSymbolPart (GrammarSymbol symb);
	ProductionPart createActionPart (String code);
	ProductionPart createListPart (List<ProductionPart> children);
	ProductionPart createMany0Part (ProductionPart child);
	ProductionPart createMany1Part (ProductionPart child);
	ProductionPart createOptionalPart (ProductionPart child);
	ProductionPart createGroupPart (List<ProductionPart> children);


}
