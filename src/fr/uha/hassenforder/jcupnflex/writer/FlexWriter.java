package fr.uha.hassenforder.jcupnflex.writer;

import java.io.File;

import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;

public class FlexWriter {

	private Grammar grammar;
	private DirectiveSet directives;
	private File outputFile;
	
	public FlexWriter(Grammar grammar, DirectiveSet directives, File outputFile) {
		this.grammar = grammar;
		this.directives = directives;
		this.outputFile = outputFile;
	}

	public void generate() {
		// TODO Auto-generated method stub
		
	}

}
