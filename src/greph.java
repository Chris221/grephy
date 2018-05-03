import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class greph extends output {

	public String nfaName;
	public String dfaName;
	public String completedName;
	public String inputFile = null;
	public String regEx = null;
	public String[] processedFile;

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
			NFADot nfa = new NFADot(grephy.nfaName, reg.tokenList, grephy.debug);
			DFADot dfa = new DFADot(grephy.dfaName, reg.tokenList, grephy.debug);
		} else {
			return;
		}
		if (reg.error == 0) {
			grephy.debug("Checking the language");
			for (int r = 0; r < reg.tokenList.length; r++) {
				if (reg.tokenList[r] != "Asterisk" && reg.tokenList[r] != "Plus" 
						&& reg.tokenList[r] != "Left_Parenthesis" && reg.tokenList[r] != "Right_Parenthesis") {
					grephy.debug("Checking the langauge for: " + reg.tokenList[r]);
					if (!(new String(input.language).contains(reg.tokenList[r]))) {
						reg.error("The RegEx contains a character [" + reg.tokenList[r] + "] not in the language");
					}
				} 
			}
		}
		if (reg.error == 0) {
			grephy.processedFile = new String[input.file.length];
			String passText;
			for (int i = 0; i < input.file.length; i++) {
				boolean pass = grephy.process(input.file[i]+" ", reg.tokenList);
					//set the pass text to green and sets the pass text for the array
					passText = "" + ConsoleColors.GREEN + "Passed" + ConsoleColors.RESET;
					passTextA = "Passed";
					//set the failed text to red and sets the failed text for the array
					passText = "" + ConsoleColors.RED + "Failed" + ConsoleColors.RESET;
					passTextA = "Failed";
				grephy.processedFile[i] = passTextA;
				if (pass) {
					//output
					grephy.output("Line [" + input.file[i] + "] " + passText);
				} else {
					//debug
					grephy.debug("Line [" + input.file[i] + "] " + passText);
				}
			}
			grephy.output("Grephy finished processing the file");
			WriteFile passFail = new WriteFile(grephy.completedName, grephy.processedFile, input.file, grephy.regEx, grephy.debug);
			grephy.output("Processed File: " + Arrays.toString(grephy.processedFile));
		} else {
			return;
		}
	}
	
	public boolean process(String line, String[] reg) {
		debug("Evaluating line: " + line);
		char tempID = ' ';
		String next = "none";
		boolean skip;
		int p;
		char c = ' ';
		List<Integer> posStore = new ArrayList<Integer>();
		List<String> parStore = new ArrayList<String>();
		if (line.length() > 0) {
			debug("Line: " + line);
			c = line.charAt(0);
			line = line.substring(1);
			debug("Checking: " + c);
		}
		for (p = 0; p < reg.length; p++) {
			String cT = reg[p];
			try {
				next = reg[p+1];
			} catch (ArrayIndexOutOfBoundsException e) {
				next = "none";
			}
			debug("Token Array location: " + reg[p]);
			debug("Next token: " + next);
			if (cT == "Asterisk") {
				skip = true;
			} else if (cT == "Plus") {
				skip = true;
			} else if (cT == "Left_Parenthesis") {
				int count = 0;
				posStore.add(p);
				for (int n = p; n < reg.length; n++) {
					if (reg[n] == "Left_Parenthesis") {
						count++;
					} else if (reg[n] == "Right_Parenthesis") {
						count--;
						try {
							if ((reg[n+1] == "Asterisk" || reg[n+1] == "Plus") && count == 0) {
								parStore.add(""+(n+1));
								parStore.add(reg[n+1]);
							}
						} catch (ArrayIndexOutOfBoundsException e) {
						}
					}
				}
				skip = true;
			} else if (cT == "Right_Parenthesis") {
				if (parStore.get(parStore.size()-1) == "Plus") {
					parStore.remove(parStore.size()-1);
					parStore.add("Asterisk");
				}
				if (next == "Asterisk" || next == "Plus") {
					//parStore.remove(parStore.size()-1);
					try {
						if (line.isEmpty()) {
							try {
								next = reg[p+1];
							} catch (ArrayIndexOutOfBoundsException e) {
								next = "none";
							}
						} else {
							p = posStore.get(posStore.size()-1);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						try {
							posStore.remove(posStore.size()-1);
						} catch (ArrayIndexOutOfBoundsException e2) {
						}
					}
				} else {
					posStore.remove(posStore.size()-1);
				}
				skip = true;
			} else {
				tempID = cT.charAt(0);
				skip = false;
				debug("ID to check was set to " + tempID);
			}
			if (!skip) {
				if (next == "Asterisk") {
					while (tempID == c) {
						debug("Found: " + tempID);
						if (line.length() > 0) {
							debug("Line: " + line);
							c = line.charAt(0);
							line = line.substring(1);
							debug("Checking: " + c);
						} else {
							c = ' ';
							break;
						}
					}
				} else if (next == "Plus") {
					if (tempID == c) {
						while (tempID == c) {
							debug("Found: " + tempID);
							if (line.length() > 0) {
								debug("Line: " + line);
								c = line.charAt(0);
								line = line.substring(1);
								debug("Checking: " + c);
							} else {
								c = ' ';
								break;
							}
						}
					} else {
						debug("Expected " + tempID + " found " + c);
						next = "failed";
						break;
					}
				} else if (tempID == c) {
					debug("Found: " + tempID);
					if (line.length() > 0) {
						debug("Line: " + line);
						c = line.charAt(0);
						line = line.substring(1);
						debug("Checking: " + c);
					} else {
						c = ' ';
					}
				} else if (parStore.size() > 0) {
					if (parStore.get(parStore.size()-1) == "Asterisk") {
						parStore.remove(parStore.size()-1);
						p = Integer.parseInt(parStore.get(parStore.size()-1));
						parStore.remove(parStore.size()-1);
					} else {
						debug("Expected " + tempID + " found " + c);
						next = "failed";
						break;
					}
				} else {
					debug("Expected " + tempID + " found " + c);
					next = "failed";
					break;
				}
			} else {
				debug("Skipping the check becuase of the " + cT);
			}
		}
		debug("Currnet Line: " + line);
		debug("RegEx position: " + (p) + "/" + reg.length);
		debug("next: " + next);
		if (next.equals("none") && (line.length() < 1) && (c == ' ')) {
			return true;
		} else {
			return false;
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
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The RegEx argument is missing from the function call.");
					} else {
						debug("Setting RegEx to " + args[i+1]);
						regEx = args[i+1];
						if (regEx.contains(" ")) {
							error("The RegEx cannot contain spaces.");
						}
						i++;
					}
				} else if (args[i].equals("-f")) {
					if ((i+1) == args.length) {
						error("The input file argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-r")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The input file argument is missing from the function call.");
					} else {
						debug("Setting the input file to " + args[i+1]);
						inputFile = args[i+1];
						i++;
					}
				} else if (args[i].equals("-n")) {
					if ((i+1) == args.length) {
						error("The NFA file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-r")) || (args[i+1].equals("-f")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The NFA file name argument is missing from the function call.");
					} else {
						debug("Setting NFA output file name to " + args[i+1]);
						nfaName = args[i+1] + ".txt";
						i++;
					}
				} else if (args[i].equals("-d")) {
					if ((i+1) == args.length) {
						error("The DFA file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The DFA file name argument is missing from the function call.");
					} else {
						debug("Setting DFA output file name to " + args[i+1]);
						dfaName = args[i+1] + ".txt";
						i++;
					}
				} else if (args[i].equals("-c")) {
					if ((i+1) == args.length) {
						error("The completed test file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-d")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						error("The completed test file name argument is missing from the function call.");
					} else {
						debug("Setting completed test file name to " + args[i+1]);
						completedName = args[i+1] + ".txt";
						i++;
					}
				} else if (args[i].equals("-debug")) {
					//holds the debug parameter slot
				} else if ((args[i].equals("-h")) || args[i].equals("-help")) {
					output(ConsoleColors.BLACK_BOLD + "Grephy\n"
						+  ConsoleColors.CYAN_BOLD + "-r " +  ConsoleColors.BLACK_BOLD + "\"RegEx\"               " + ConsoleColors.RESET + "-- The input RegEx (" +  ConsoleColors.BLACK_BOLD + "required" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-f " +  ConsoleColors.BLACK_BOLD + "\"input file\"          " + ConsoleColors.RESET + "-- The input file the RegEx will check (" +  ConsoleColors.BLACK_BOLD + "required" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-n " +  ConsoleColors.BLACK_BOLD + "\"nfa file name\"       " + ConsoleColors.RESET + "-- The name of the nfa output file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-d " +  ConsoleColors.BLACK_BOLD + "\"dfa file name\"       " + ConsoleColors.RESET + "-- The name of the dfa output file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-c " +  ConsoleColors.BLACK_BOLD + "\"completed file name\" " + ConsoleColors.RESET + "-- The name of the completed test file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-h -help                 " + ConsoleColors.RESET + "-- Brings up this help menu\n"
						+  ConsoleColors.CYAN_BOLD + "-debug                   " + ConsoleColors.RESET + "-- Enables debug mode\n");
					error++;
					return;
				} else {
					error("Parameter '" + args[i] + "' does not excist.");
				}
			} catch (Exception e) {
				
			}
		}
		if (dfaName == null) {
			dfaName = "DFA.txt";
		} else if (dfaName.isEmpty()) {
			dfaName = "DFA.txt";
		}
		if (nfaName == null) {
			nfaName = "NFA.txt";
		} else if (nfaName.isEmpty()) {
			nfaName = "NFA.txt";
		}
		if (completedName == null) {
			completedName = "Completed File.txt";
		} else if (completedName.isEmpty()) {
			completedName = "Completed File.txt";
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
