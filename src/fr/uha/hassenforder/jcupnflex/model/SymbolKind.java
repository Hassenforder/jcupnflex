package fr.uha.hassenforder.jcupnflex.model;

/**
 * This class represents the kind of the GrammarSymbol
 * It helps to determine what symbol without try/catch and so on
 * 
 * NONTERMINAL : a classic terminal to express rules
 * TERMINAL    : a classic terminal linked to the lexical analyser for the regexp
 * REGEXP	   : a special inlined terminal
 * STATE	   : a future state in the lexical analyser
 *
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public enum SymbolKind {
	NONTERMINAL,
	TERMINAL,
	REGEXP,
	STATE,
}
