package fr.uha.hassenforder.jcupnflex.model;

/**
 * This class represents the kind of a Production or a ProductionPart
 * It helps to determine what part without try/catch and so on
 * 
 * LIST		: a list of parts
 * SYMBOL   : a symbol of the grammar
 * REGEXP	: a special inlined terminal
 * ACTION	: action code 
 * OPERATOR : one of the EBNF operators like *, +, ?, (, )
 *
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */

public enum ProductionKind {
// Production
	NONTERMINAL,
	TERMINAL,
	STATE,

// ProductionPart
	LIST,
	SIMPLE,
	SYMBOL,
	REGEXP,
	ACTION,
	OPERATOR,
}
