package fr.uha.hassenforder.jcupnflex.model;

/**
 * This class represents an EBNF operator in the grammar
 * it could be either * or + or ? or ( or ).
 * 
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */
public class OperatorPart extends ProductionPart {

	public enum OperatorKind {
		MANY_0,
		MANY_1,
		OPTIONAL,
		GROUP,
	}

	/** the used operator in an abstract representation */
	private OperatorKind operator;
	
	/** the child used by the operator */
	private ProductionPart child;
	
	/**
	 * Full constructor.
	 * 
	 * @param text the text about the operator.
	 */
	public OperatorPart(OperatorKind operator, ProductionPart child) {
		super(ProductionKind.OPERATOR, null);
		assert operator != null : "Attempt to construct an unknown operator";
		this.operator = operator;
		this.child = child;
	}

	public OperatorKind getOperator() {
		return operator;
	}

	public ProductionPart getChild() {
		return child;
	}

}
