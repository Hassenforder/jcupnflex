package fr.uha.hassenforder.jcupnflex;

import java.util.ArrayList;
import java.util.List;

public class Options {

	/** Directory were the resulting code goes into (null is used for local). */
	private String destDir = null;

	/** Inputfile to process. */
	private List<String> inputFiles = new ArrayList<>();

	/** Pattern of the name of the generated jcup file. */
	private String jcupPattern = "Parser-%s.cup";

	/** Pattern of the name of the generated jflex file. */
	private String flexPattern = "Scanner-%s.flex";

	/** Generate a jcup file. */
	private boolean jcupGeneration = true;

	/** Generate a flex file. */
	private boolean flexGeneration = true;

	public String getDestDir() {
		return destDir;
	}

	public List<String> getInputFiles() {
		return inputFiles;
	}

	public String getJcupPattern() {
		return jcupPattern;
	}

	public String getFlexPattern() {
		return flexPattern;
	}

	public boolean isJcupGeneration() {
		return jcupGeneration;
	}

	public boolean isFlexGeneration() {
		return flexGeneration;
	}

	/**
	 * process one option and returns the count of args used
	 * 
	 * @param option the name of the option 
	 * @param arg1	 the next argument as parameter
	 * @return the count of arg used
	 */
	private int setOption(String option, String arg1) {
		if (option.equals("-destdir")) {
			if (arg1 != null) {
				destDir = arg1;
				return 2;
			} else {
				ErrorManager.getManager().emit_fatal("destdir must have a name argument");
				return 0;
			}
		}
		if (option.equals("-cup")) {
			if (arg1 != null) {
				jcupPattern = arg1;
				return 2;
			} else {
				ErrorManager.getManager().emit_fatal("cup must have a name argument");
				return 0;
			}
		}
		if (option.equals("-flex")) {
			if (arg1 != null) {
				flexPattern = arg1;
				return 2;
			} else {
				ErrorManager.getManager().emit_fatal("flex must have a name argument");
				return 0;
			}
		}
		if (option.equals("-nocup")) {
			jcupGeneration = false;
			return 1;
		}
		if (option.equals("-noflex")) {
			flexGeneration = false;
			return 1;
		}
		if (option.endsWith(".cnf")) {
			inputFiles.add(option);
			return 1;
		}
		ErrorManager.getManager().emit_fatal("Unrecognized option \"" + option + "\"");
		return 0;
	}

	/**
	 * Parse command line options and arguments to set various user-option flags and
	 * variables.
	 * 
	 * @param argv the command line arguments to be parsed.
	 * @return if it works well or fail
	 */
	public boolean parseArgs (String argv[]) {
		int i=0;
		while (i < argv.length-1) {
			String option = argv[i];
			String argument = argv[i+1];
			int used = setOption(option, argument);
			switch (used) {
			case 0 : return false;
			case 1 : 
			case 2 : i += used; break;
			default : return false;
			}
		}
		return true;
	}
		
}
