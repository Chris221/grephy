
public class greph {
import java.util.Arrays;


	public String nfaName;
	public String dfaName;
	public String inputFile = null;
	public String regEx = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		greph grephy = new greph(args);
	}
	
	public greph(String[] args) {
		int error = 0;
		if (Arrays.asList(args).contains("-debug")) {
			debug = true;
			debug("Enabling Debug mode...");
		}
		for (int i = 0; i < args.length; i++) {
			debug("Current parameter: " + args[i]);
			try {
				if (args[i].equals("-r")) {
					if ((i+1) == args.length) {
						output("ERROR: The RegEx argument is missing from the function call.");
						error++;
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The RegEx argument is missing from the function call.");
						error++;
					} else {
						debug("Setting RegEx to " + args[i+1]);
						regEx = args[i+1];
						i++;
					}
				} else if (args[i].equals("-f")) {
					if ((i+1) == args.length) {
						output("ERROR: The input file argument is missing from the function call.");
						error++;
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-r")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The input file argument is missing from the function call.");
						error++;
					} else {
						debug("Setting the input file to " + args[i+1]);
						inputFile = args[i+1];
						i++;
					}
				} else if (args[i].equals("-n")) {
					if ((i+1) == args.length) {
						output("ERROR: The NFA file name argument is missing from the function call.");
						error++;
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-r")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The NFA file name argument is missing from the function call.");
						error++;
					} else {
						debug("Setting NFA output file name to " + args[i+1]);
						nfaName = args[i+1];
						i++;
					}
				} else if (args[i].equals("-d")) {
					if ((i+1) == args.length) {
						output("ERROR: The DFA file name argument is missing from the function call.");
						error++;
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The DFA file name argument is missing from the function call.");
						error++;
					} else {
						debug("Setting DFA output file name to " + args[i+1]);
						dfaName = args[i+1];
						i++;
					}
				} else if (args[i].equals("-debug")) {
					//holds the debug parameter slot
				} else if ((args[i].equals("-h")) || args[i].equals("-help")) {
					output("Grephy\n"
						+  "-r 'RegEx'         -- The input RegEx (required)\n"
						+  "-f 'input file'    -- The input file the RegEx will check (required)\n"
						+  "-n 'nfa file name' -- The name of the nfa output file (optinal)\n"
						+  "-d 'dfa file name' -- The name of the dfa output file (optinal)\n"
						+  "-h -help           -- Brings up this help menu\n"
						+  "-debug             -- Enables debug mode\n");
					return;
				} else {
					output("ERROR: Parameter '" + args[i] + "' does not excist.");
					error++;
				}
			} catch (Exception e) {
				
			}
		}
		boolean pass = true;
		if (regEx == null) {
			output("ERROR: A RegEx is required. -r 'RegEx'");
			pass = false;
		}
		if (inputFile == null) {
			output("ERROR: An input file is required. -f 'input file'");
			pass = false;
		}
		if (!pass) {
			return;
		}
	}
}
