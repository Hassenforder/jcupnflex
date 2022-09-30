package fr.uha.hassenforder.jcupnflex.model;

/**
 * Borrowed from JCup 
 * 
 * This class represents a part of a production which contains an action.
 *
 * @version last updated: 28/09/2022
 * @author Frank Flannery
 * @author Michel Hassenforder
 */

public class ActionPart extends ProductionPart {

	/** String containing code for the action in question. */
	private String code;

	/**
	 * Simple constructor.
	 * 
	 * @param code string containing the actual user code.
	 */
	public ActionPart(String code) {
		super(ProductionKind.ACTION, null);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void addCode(String moreCode) {
		code += moreCode;
	}

	public String toString() {
		return super.toString() + "{" + getCode() + "}";
	}

}
