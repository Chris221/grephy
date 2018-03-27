

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class output {

	public Boolean debug = false; 
	public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	public LocalDateTime now = LocalDateTime.now(); 
	public int error = 0;
	public int warning = 0;
	
	/*
	 * For outputting debug information to the console
	 */
	public void debug(String text) {
		if (debug) {
			output(ConsoleColors.CYAN_BOLD + "Debugger: " + ConsoleColors.RESET + text);
		}
	}
	
	/*
	 * For outputting errors to the console
	 */
	public void error(String text) {
		output(ConsoleColors.RED_BOLD + "ERROR: " + ConsoleColors.RED + text);
		error++;
	}
	
	/*
	 * For outputting warnings to the console
	 */
	public void warning(String text) {
		output(ConsoleColors.YELLOW_BOLD + "Warning: " + ConsoleColors.YELLOW + text);
		warning++;
	}
	
	/*
	 * For outputting to the console
	 */
	@SuppressWarnings("all")
	public void output(String text) {
		System.out.println(ConsoleColors.BLACK_BOLD + dtf.format(now) + ": " + ConsoleColors.RESET + text + ConsoleColors.RESET);
	}
	@SuppressWarnings("all")
	public void output(int text) {
		System.out.println(ConsoleColors.BLACK_BOLD + dtf.format(now) + ": " + ConsoleColors.RESET + text + ConsoleColors.RESET);
	}
}
