import java.util.Arrays;

public class greph extends output{

	public String nfaName;
	public String dfaName;
	public String inputFile = null;
	public String regEx = null;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		greph grephy = new greph(args);
		FileLoader input;
		RegEx reg;
		if (grephy.error == 0) {
			input = new FileLoader(grephy.inputFile, grephy.debug);
			//input.file
		} else {
			return;
		}
		if (input.error == 0) {
			reg = new RegEx(grephy.regEx, grephy.debug, input.language);
		} else {
			return;
		}
	}
	
	public greph(String[] args) {
		if (Arrays.asList(args).contains("-debug")) {
			debug = true;
			debug("Enabling Debug mode...");
		}
		for (int i = 0; i < args.length; i++) {
			debug("Current parameter: " + args[i]);
			try {
				if (args[i].equals("-r")) {
					if ((i+1) == args.length) {
						error("The RegEx argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The RegEx argument is missing from the function call.");
					} else {
						debug("Setting RegEx to " + args[i+1]);
						regEx = args[i+1];
						i++;
					}
				} else if (args[i].equals("-f")) {
					if ((i+1) == args.length) {
						error("The input file argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-r")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The input file argument is missing from the function call.");
					} else {
						debug("Setting the input file to " + args[i+1]);
						inputFile = args[i+1];
						i++;
					}
				} else if (args[i].equals("-n")) {
					if ((i+1) == args.length) {
						error("The NFA file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-r")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The NFA file name argument is missing from the function call.");
					} else {
						debug("Setting NFA output file name to " + args[i+1]);
						nfaName = args[i+1];
						i++;
					}
				} else if (args[i].equals("-d")) {
					if ((i+1) == args.length) {
						error("The DFA file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The DFA file name argument is missing from the function call.");
					} else {
						debug("Setting DFA output file name to " + args[i+1]);
						dfaName = args[i+1];
						i++;
					}
				} else if (args[i].equals("-debug")) {
					//holds the debug parameter slot
				} else if ((args[i].equals("-h")) || args[i].equals("-help")) {
					output(ConsoleColors.BLACK_BOLD + "Grephy\n"
						+  ConsoleColors.CYAN_BOLD + "-r " +  ConsoleColors.BLACK_BOLD + "\"RegEx\"         " + ConsoleColors.RESET + "-- The input RegEx (" +  ConsoleColors.BLACK_BOLD + "required" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-f " +  ConsoleColors.BLACK_BOLD + "\"input file\"    " + ConsoleColors.RESET + "-- The input file the RegEx will check (" +  ConsoleColors.BLACK_BOLD + "required" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-n " +  ConsoleColors.BLACK_BOLD + "\"nfa file name\" " + ConsoleColors.RESET + "-- The name of the nfa output file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-d " +  ConsoleColors.BLACK_BOLD + "\"dfa file name\" " + ConsoleColors.RESET + "-- The name of the dfa output file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-h -help           " + ConsoleColors.RESET + "-- Brings up this help menu\n"
						+  ConsoleColors.CYAN_BOLD + "-debug             " + ConsoleColors.RESET + "-- Enables debug mode\n");
					error++;
					return;
				} else {
					error("Parameter '" + args[i] + "' does not excist.");
				}
			} catch (Exception e) {
				
			}
		}
		if (regEx == null) {
			error("A RegEx is required. -r 'RegEx'");
		}
		if (inputFile == null) {
			error("An input file is required. -f 'input file'");
		}
		if (error > 0) {
			output(ConsoleColors.BLACK_BOLD + "Grephy failed to start");
			return;
		}
		output(ConsoleColors.BLACK_BOLD + "Grephy loaded...");
	}
}
