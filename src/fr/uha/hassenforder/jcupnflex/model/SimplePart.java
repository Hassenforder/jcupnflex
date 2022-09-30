package fr.uha.hassenforder.jcupnflex.model;

/**
 * 
 * This class represents a simple part of a production with or not a label
 *
 * @version last updated: 28/09/2022
 * @author Michel Hassenforder
 */
public class SimplePart extends ProductionPart {

	/** The symbol that this part is made up of. */
	private final ProductionPart child;

	/**
	 * Full constructor.
	 * 
	 * @param symbol the symbol that this part is made up of.
	 * @param label an optional label string for the part.
	 */
	public SimplePart(ProductionPart child, String label) {
		super (ProductionKind.SIMPLE, label);
		this.child = child;
	}

	public ProductionPart getChild() {
		return child;
	}

}
