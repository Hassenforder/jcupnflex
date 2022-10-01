package fr.uha.hassenforder.jcupnflex.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Borrowed from JCup 
 * 
 * Represents the context-free grammar for which we build a parser. An object of
 * this class is created by the JavaCUP parser which reads in the user grammar.
 * A grammar is a collection of non-terminal and terminal symbols and
 * productions.
 * 
 * @version last updated: 28/09/2022
 * @author hoenicke
 * @author Michel Hassenforder
 *
 */
public class Grammar {
	
	private IFactory factory = new Factory();
	private Map<String, Terminal> terminals;
	private Map<String, RegExp> regexps;
	private Map<String, NonTerminal> nonTerminals;
	private List<Production> productions;

	private NonTerminal startSymbol;

	public Grammar(IFactory factory) {
		this.factory = factory;
		Terminal error = new Terminal("error");
		getTerminals().put(error.getName(), error);
	}

	public Map<String, Terminal> getTerminals() {
		if (terminals == null) terminals = new TreeMap<>();
		return terminals;
	}

	public Map<String, RegExp> getRegexps() {
		if (regexps == null) regexps = new TreeMap<>();
		return regexps;
	}

	public Map<String, NonTerminal> getNonTerminals() {
		if (nonTerminals == null) nonTerminals = new TreeMap<>();
		return nonTerminals;
	}

	public List<Production> getProductions() {
		if (productions == null) productions = new ArrayList<>();
		return productions;
	}


	public NonTerminal getStartSymbol() {
		return startSymbol;
	}

	public void setStartSymbol(NonTerminal startSymbol) {
		this.startSymbol = startSymbol;
	}

	public GrammarSymbol getSymbol(String name) {
		if (getTerminals().containsKey(name)) return getTerminals().get(name);
		if (getNonTerminals().containsKey(name)) return getNonTerminals().get(name);
		return null;
	}

	public Terminal getTerminal(String name) {
		return getTerminals().get(name);
	}

	public NonTerminal getNonTerminal(String name) {
		return getNonTerminals().get(name);
	}

	public RegExp getRegexp(String name) {
		return getRegexps().get(name);
	}

	public RegExp getOrAddRegexp (String content) {
		if (getRegexps().containsKey(content)) return getRegexps().get(content);
		GrammarSymbol symbol = factory.createRegexp(content);
		RegExp regExp = (RegExp) symbol;
		getTerminals().put(regExp.getName(), regExp);
		getRegexps().put(regExp.getRegexp(), regExp);
		return regExp;
	}

	public void addUniqueSymbol(GrammarSymbol symbol) {
		assert getSymbol(symbol.getName()) == null : "Attempt to construct a allready known terminal";
		switch (symbol.getKind()) {
		case NONTERMINAL:
			NonTerminal nonTerminal = (NonTerminal) symbol;
			getNonTerminals().put(nonTerminal.getName(), nonTerminal);
			break;
		case TERMINAL 	:
			Terminal terminal = (Terminal) symbol;
			getTerminals().put(terminal.getName(), terminal);
			break;
		case REGEXP		:
			RegExp regExp = (RegExp) symbol;
			getTerminals().put(regExp.getName(), regExp);
			getRegexps().put(regExp.getRegexp(), regExp);
			break;
		}
	}

	public void addProduction (Production production) {
		getProductions().add(production);
	}

	public NonTerminal getStarSymbol(GrammarSymbol sym) {
		return startSymbol;
	}

}
