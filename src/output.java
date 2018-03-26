

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class output {

	public Boolean debug = false; 
	public DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
	public LocalDateTime now = LocalDateTime.now(); 
	/*
	 * For outputting debug information to the console
	 */
	public void debug(String text) {
		if (debug) {
			output("Debugger: " + text);
		}
	}
	/*
	 * For outputting to the console
	 */
	public void output(String text) {
		System.out.println(dtf.format(now) + ": " + text);
	}
	public void output(int text) {
		System.out.println(dtf.format(now) + ": " + text);
	}
}
