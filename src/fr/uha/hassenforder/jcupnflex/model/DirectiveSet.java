package fr.uha.hassenforder.jcupnflex.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DirectiveSet {

	enum DirectiveKind {
		SINGLE,
		MULTIPLE,
		KEYED,
	}

	private Map<Directive, String> singleValues;
	private Map<Directive, List<String>> multipleValues;
	private Map<Directive, Map<String, String>> keyedValues;
	
	private DirectiveKind map (Directive directive) {
		switch (directive) {
		case ACTION_CODE:		return DirectiveKind.SINGLE;
		case AFTER_REDUCE_CODE:	return DirectiveKind.SINGLE;
		case EXPECT:			return DirectiveKind.SINGLE;
		case IMPORT:			return DirectiveKind.MULTIPLE;
		case INIT_WITH_CODE:	return DirectiveKind.SINGLE;
		case OPTION:			return DirectiveKind.KEYED;
		case PACKAGE_NAME:		return DirectiveKind.SINGLE;
		case PARSER_CODE:		return DirectiveKind.SINGLE;
		case PARSER_NAME:		return DirectiveKind.SINGLE;
		case SCAN_WITH_CODE:	return DirectiveKind.SINGLE;
		default:				return DirectiveKind.SINGLE;
		}
	}

	private Map<Directive, String> getSingleValues() {
		if (singleValues == null) singleValues = new TreeMap<>();
		return singleValues;
	}

	private Map<Directive, List<String>> getMultipleValues() {
		if (multipleValues == null) multipleValues = new TreeMap<>();
		return multipleValues;
	}

	private Map<Directive, Map<String, String>> getKeyedValues() {
		if (keyedValues == null) keyedValues = new TreeMap<>();
		return keyedValues;
	}


	public boolean setDirective(Directive directive, String code) {
		if (map(directive) != DirectiveKind.SINGLE) return false;
		if (getSingleValues().containsKey(directive)) return false;
		getSingleValues().put(directive, code);
		return true;
	}

	public boolean addDirective(Directive directive, String value) {
		if (map(directive) != DirectiveKind.MULTIPLE) return false;
		List<String> values = getMultipleValues().get(directive);
		if (values == null) {
			values = new ArrayList<>();
			getMultipleValues().put(directive, values);
		}
		values.add(value);
		return true;
	}

	public boolean addDirective(Directive directive, String key, String value) {
		if (map(directive) != DirectiveKind.KEYED) return false;
		Map<String, String> values = getKeyedValues().get(directive);
		if (values == null) {
			values = new TreeMap<>();
			getKeyedValues().put(directive, values);
		}
		if (values.containsKey(key)) return false;
		values.put(key, value);
		return true;
	}

	public String getSingleValue(Directive directive) {
		if (map(directive) != DirectiveKind.SINGLE) return null;
		return getSingleValues().get(directive);
	}

	public List<String> getMultipleValues(Directive directive) {
		if (map(directive) != DirectiveKind.MULTIPLE) return null;
		return getMultipleValues().get(directive);
	}

	public Map<String, String> getKeyedValues(Directive directive) {
		if (map(directive) != DirectiveKind.KEYED) return null;
		return getKeyedValues().get(directive);
	}

}
