package fr.uha.hassenforder.jcupnflex.model;

/**
 * Borrowed from JCup 
 * 
 * This based class represents one part of a production with an optional label
 *
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */
public abstract class ProductionPart {

	/** The kind of the production. */
	private final ProductionKind kind;

	/**
	 * Optional label for referring to the part within an action (null for no
	 * label).
	 */
	private final String label;

	/**
	 * Full constructor.
	 * 
	 * @param kind the kind that part.
	 * @param label an optional label string for the part.
	 */
	public ProductionPart(ProductionKind kind, String label) {
		this.kind = kind;
		this.label = label;
	}

	public ProductionKind getKind() {
		return kind;
	}

	public String getLabel() {
		return label;
	}

	public String toString() {
		StringBuilder tmp = new StringBuilder();
		if (label != null) {
			tmp.append(":");
			tmp.append(label);
		}
		return tmp.toString();
	}

}
