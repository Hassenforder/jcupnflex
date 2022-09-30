package fr.uha.hassenforder.jcupnflex.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents one part of a production which combines a GrammarSymbol and a label
 *
 *
 */
public class ListPart extends ProductionPart {

	/** A collection of parts children of this one. */
	private List<ProductionPart> children;

	/**
	 * Full constructor.
	 * 
	 * @param children the children parts.
	 * @param label an optional label string for the part.
	 */
	public ListPart(List<ProductionPart> children, String label) {
		super (ProductionKind.LIST, label);
		this.children = children;
	}

	/**
	 * Constructor with no label.
	 * 
	 * @param sym the symbol that this part is made up of.
	 */
	public ListPart(List<ProductionPart> children) {
		super (ProductionKind.LIST, null);
		this.children = children;
	}

	/**
	 * Constructor with no children yet.
	 * 
	 * @param sym the symbol that this part is made up of.
	 */
	public ListPart(String label) {
		super (ProductionKind.LIST, label);
		this.children = null;
	}

	/**
	 * Constructor with nothing.
	 * 
	 */
	public ListPart() {
		super (ProductionKind.LIST, null);
		this.children = null;
	}

	public List<ProductionPart> getChildren() {
		if (children == null) children = new ArrayList<>();
		return children;
	}

	public void addChild (ProductionPart child) {
		getChildren().add(child);
	}

}
