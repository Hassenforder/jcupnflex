package fr.uha.hassenforder.jcupnflex;

import java.io.File;
import java.io.FileReader;

import com.github.jhoenicke.javacup.runtime.AdvancedSymbolFactory;

import fr.uha.hassenforder.jcupnflex.model.DirectiveSet;
import fr.uha.hassenforder.jcupnflex.model.Grammar;
import fr.uha.hassenforder.jcupnflex.reader.Lexer;
import fr.uha.hassenforder.jcupnflex.reader.Parser;
import fr.uha.hassenforder.jcupnflex.writer.CupWriter;
import fr.uha.hassenforder.jcupnflex.writer.FlexWriter;

public class Main {

	private Options options = new Options();
	private Grammar grammar;
	private DirectiveSet directives;
	
	private boolean readFile(String inputFile) {
		try {
	    	AdvancedSymbolFactory csf = new AdvancedSymbolFactory();
	    	Lexer l = new Lexer(new FileReader(inputFile));
	    	l.setAdvancedSymbolFactory(csf);
	    	Parser p = new Parser(l, csf);
	    	p.parse();
	    	grammar = p.getGrammar();
	    	directives = p.getDirectiveSet();
	    	return true;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}

	private File buildOutputFile (String destDir, String inputFile, String pattern) {
		File targetDir = new File (destDir == null ? "." : destDir);
		String stripedName;
		int dotPosition = inputFile.lastIndexOf(".");
		int slashPosition = inputFile.lastIndexOf("/");
		if (slashPosition != -1 && dotPosition != -1) {
			stripedName = inputFile.substring(slashPosition+1, dotPosition);
		} else if (slashPosition != -1 && dotPosition == -1) {
			stripedName = inputFile.substring(slashPosition+1);
		} else if (slashPosition == -1 && dotPosition != -1) {
			stripedName = inputFile.substring(0, dotPosition);
		} else {
			stripedName = inputFile;
		}
		String destFile = String.format(pattern, stripedName);
		File outputFile = new File (targetDir, destFile);
		return outputFile;
	}

	private void generateJCup(String destDir, String inputFileName) {
		File outputFile = buildOutputFile(destDir, inputFileName, options.getJcupPattern());
		CupWriter writer = new CupWriter (grammar, directives, outputFile);
		writer.generate ();
	}

	private void generateFlex(String destDir, String inputFileName) {
		File outputFile = buildOutputFile(destDir, inputFileName, options.getFlexPattern());
		FlexWriter writer = new FlexWriter (grammar, directives, outputFile);
		writer.generate ();
	}

	private boolean process (String inputFileName) {
    	if (! readFile(inputFileName)) return false;
    	if (options.isJcupGeneration()) {
    		generateJCup (options.getDestDir(), inputFileName);
    	}
    	if (options.isFlexGeneration()) {
    		generateFlex (options.getDestDir(), inputFileName);
    	}
    	return true;
	}

	private boolean process(String[] argv) {
    	if (! options.parseArgs(argv)) return false;
    	for (String inputFileName : options.getInputFiles()) {
    		process (inputFileName);
    	}
    	if (options.getInputFiles().isEmpty()) {
    		process ("src/jcupnflex.cup");
    		process ("calc.cnf");
    	}
    	return true;
	}
	
	static public void main(String argv[]) {
		Main main = new Main();
		main.process(argv);
	}
	
}


