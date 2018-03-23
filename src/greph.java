
public class greph {

	public String nfaName;
	public String dfaName;
	public String inputFile = null;
	public String regEx = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		greph grephy = new greph(args);
	}
	
	public greph(String[] args) {
		for (int i = 0; i < args.length; i++) {
			output(args[i]);
			try {
				if (args[i].equals("-r")) {
					output(args[i+1]);
					if ((args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The RegEx argument is missing from the function call.");
						return;
					} else {
						regEx = args[i+1];
						i++;
					}
				} else if (args[i].equals("-f")) {
					if ((args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-r")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The input file argument is missing from the function call.");
						return;
					} else {
						inputFile = args[i+1];
						i++;
					}
				} else if (args[i].equals("-n")) {
					if ((args[i+1].equals("-d")) || (args[i+1].equals("-r")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The NFA file name argument is missing from the function call.");
						return;
					} else {
						nfaName = args[i+1];
						i++;
					}
				} else if (args[i].equals("-d")) {
					if ((args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						output("ERROR: The DFA file name argument is missing from the function call.");
						return;
					} else {
						dfaName = args[i+1];
						i++;
					}
				} else if ((args[i].equals("-h")) || args[i].equals("-help")) {
					output("Grephy\n"
						+  "-r 'RegEx'         -- The input RegEx (required)\n"
						+  "-f 'input file'    -- The input file the RegEx will check (required)\n"
						+  "-n 'nfa file name' -- The name of the nfa output file (optinal)\n"
						+  "-d 'dfa file name' -- The name of the dfa output file (optinal)\n"
						+  "-h -help           -- Brings up this help menu\n");
					return;
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
	
	public void output(String text) {
		System.out.println(text);
	}

}
