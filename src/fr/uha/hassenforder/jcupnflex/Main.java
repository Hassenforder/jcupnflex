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

	private File buildOutputFile (String destDir, String destfile) {
		File targetDir = new File (destDir == null ? "." : destDir);
		File outputFile = new File (targetDir, destfile);
		return outputFile;
	}

	private void generateJCup(String destDir, String jcupFileName) {
		File outputFile = buildOutputFile(destDir, jcupFileName);
		CupWriter writer = new CupWriter (grammar, directives, outputFile);
		writer.generate ();
	}

	private void generateFlex(String destDir, String flexFileName) {
		File outputFile = buildOutputFile(destDir, flexFileName);
		FlexWriter writer = new FlexWriter (grammar, directives, outputFile);
		writer.generate ();
	}

    private boolean process(String[] argv) {
    	if (! options.parseArgs(argv)) return false;
    	if (! readFile(options.getInputFile())) return false;
    	if (options.isJcupGeneration()) {
    		generateJCup (options.getDestDir(), options.getJcupFileName());
    	}
    	if (options.isFlexGeneration()) {
    		generateFlex (options.getDestDir(), options.getFlexFileName());
    	}
    	return true;
	}
	
	static public void main(String argv[]) {
		Main main = new Main();
		main.process(argv);
	}
	
}


