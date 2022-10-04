package fr.uha.hassenforder.jcupnflex.model;

import java.util.List;

public interface IFactory {
	
	// all kind of Symbols
	GrammarSymbol createSymbol (SymbolKind kind, String name, String type);
	GrammarSymbol createRegexp(String content);
	
	// all kind of Productions
	public Production createNonTerminalProduction(GrammarSymbol lhs, List<ProductionPart> rhs, GrammarSymbol precedence);
	public Production createTerminalProduction(GrammarSymbol lhs, String regexp, String code);
	public Production createTerminalProduction(GrammarSymbol lhs, String from, String to, String code);

	// all kind of Parts
	ProductionPart createSimplePart (ProductionPart child, String label);
	ProductionPart createSymbolPart (GrammarSymbol symb);
	ProductionPart createActionPart (String code);
	ProductionPart createListPart (List<ProductionPart> children);
	ProductionPart createMany0Part (ProductionPart child);
	ProductionPart createMany1Part (ProductionPart child);
	ProductionPart createOptionalPart (ProductionPart child);
	ProductionPart createGroupPart (List<ProductionPart> children);

}
