package fr.uha.hassenforder.jcupnflex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.github.jhoenicke.javacup.runtime.Symbol;

/**
 * Error manager based on a singleton (sad)
 * 4 severities (info, warning, error, fatal)
 * We count severity messages
 * 
 * User emit a message using a dedicated method
 * An option symbol can be associated to obtain a precise location
 * If the symbol is known it is registered in a filter
 * If two messages about the same symbol must be emitted only the first is printed
 * 
 * @author michel
 *
 */
public class ErrorManager {

	/** singleton */
	private static ErrorManager errorManager;
	
	/** severity of the message */
	static enum Severity {
		Info,
		Warning,
		Error,
		Fatal,
	}

	/** PrintWriter where all messages go */
	private PrintWriter output = new PrintWriter (System.out);
	
	/** counter of emitted infos */
	private int infos = 0;
	/** counter of emitted warnings */
	private int warnings = 0;
	/** counter of emitted errors */
	private int errors = 0;
	/** counter of emitted fatals */
	private int fatals = 0;

	/** the list of already emitted symbol */
	private Set<String> filter;

	/** register all message to allow reprint in an other destination */
	private List<String> messages;
	
	private ErrorManager() {
	}

	/** singleton */
	public static ErrorManager getManager() {
		if (errorManager == null)
			errorManager = new ErrorManager();
		return errorManager;
	}

	/**
	 * change the output for all messages
	 * by default it is System.out
	 */
	
	public boolean setOutput(String filename) {
		try {
			output = new PrintWriter (new BufferedWriter (new FileWriter(filename)));
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void flush() {
		output.flush();
	}

	/** clear every things just by forgetting previous singleton */
	public static void clear() {
		errorManager = null;
	}

	/** lazy accessor on the filter */
	private Set<String> getFilter() {
		if (filter == null)
			filter = new TreeSet<>();
		return filter;
	}

	public int getInfoCount() {
		return infos;
	}

	public int getFatalCount() {
		return fatals;
	}

	public int getErrorCount() {
		return errors;
	}

	public int getWarningCount() {
		return warnings;
	}

	/** lazy accessor on the list of messages */
	public List<String> getMessages() {
		if (messages == null) messages = new ArrayList<>();
		return messages;
	}

	/**
	 * Error message format: ERRORLEVEL : MESSAGE @ Symbol:
	 * name#token=="value"(line/column) ERRORLEVEL : MESSAGE
	 **/

	/**
	 * check about redundant message about same symbol
	 * build and emit an error message
	 * 
	 * format: ERRORLEVEL : MESSAGE @ Symbol:
	 * name#token=="value"(line/column) ERRORLEVEL : MESSAGE
	 * 
	 * @param severity	severity of the message
	 * @param message 	text of the message
	 * @param sym		symbol optional to give precisely the location
	 */
	private void emit(Severity severity, String message, Symbol sym) {
		if (sym != null && sym.value != null) {
			if (getFilter().contains(sym.value.toString()))
				return;
			getFilter().add(sym.value.toString());
		}
		StringBuilder tmp = new StringBuilder();
		tmp.append(severity.name());
		tmp.append(": ");
		tmp.append(message);
		if (sym != null) {
			tmp.append(" @ ");
			tmp.append(sym);
		}
		String text = tmp.toString();
		getMessages().add(text);
		System.err.println(text);
	}

	/**
	 * Emit an info without the symbol, should be avoided
	 * 
	 * @param message	the text of the message
	 */
	public void emit_info(String message) {
		emit_info(message, null);
	}

	/**
	 * Emit an info with the symbol
	 * 
	 * @param message	the text of the message
	 * @param sym		the symbol where the message is about
	 */
	public void emit_info(String message, Symbol sym) {
		emit(Severity.Info, message, sym);
		infos++;
	}

	/**
	 * Emit a warning without the symbol, should be avoided
	 * 
	 * @param message	the text of the message
	 */
	public void emit_warning(String message) {
		emit_warning(message, null);
	}

	/**
	 * Emit a warning with the symbol
	 * 
	 * @param message	the text of the message
	 * @param sym		the symbol where the message is about
	 */
	public void emit_warning(String message, Symbol sym) {
		emit(Severity.Warning, message, sym);
		warnings++;
	}

	/**
	 * Emit an error without the symbol, should be avoided
	 * 
	 * @param message	the text of the message
	 */
	public void emit_error(String message) {
		emit_error(message, null);
	}

	/**
	 * Emit an error with the symbol
	 * 
	 * @param message	the text of the message
	 * @param sym		the symbol where the message is about
	 */
	public void emit_error(String message, Symbol sym) {
		emit(Severity.Error, message, sym);
		errors++;
	}

	/**
	 * Emit a fatal error without the symbol, should be avoided
	 * 
	 * @param message	the text of the message
	 */
	public void emit_fatal(String message) {
		emit_fatal(message, null);
	}

	/**
	 * Emit a fatal with the symbol
	 * 
	 * @param message	the text of the message
	 * @param sym		the symbol where the message is about
	 */
	public void emit_fatal(String message, Symbol sym) {
		emit(Severity.Fatal, message, sym);
		fatals++;
	}

}
