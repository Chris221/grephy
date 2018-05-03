import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class greph extends output {
	//Defines the required variables 
	public String nfaName;
	public String dfaName;
	public String completedName;
	public String inputFile = null;
	public String regEx = null;
	public String[] processedFile;

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		//makes instance of self
		greph grephy = new greph(args);
		//defines new instance variables
		FileLoader input;
		RegEx reg;
		//if grephy loading doesn't fail
		if (grephy.error == 0) {
			//load the file
			input = new FileLoader(grephy.inputFile, grephy.debug);
			//input.file
		} else {
			//failed
			return;
		}
		//if file loading doesn't fail
		if (input.error == 0) {
			//load the regex
			reg = new RegEx(grephy.regEx, grephy.debug, input.language);
			//creates the nfa and dfa graph in the DOT format
			NFADot nfa = new NFADot(grephy.nfaName, reg.tokenList, grephy.debug);
			DFADot dfa = new DFADot(grephy.dfaName, reg.tokenList, grephy.debug);
		} else {
			//failed
			return;
		}
		//if regex loading doesn't fail
		if (reg.error == 0) {
			//debug
			grephy.debug("Checking the language");
			//loops through the regex
			for (int r = 0; r < reg.tokenList.length; r++) {
				//If not a character of the regex
				if (reg.tokenList[r] != "Asterisk" && reg.tokenList[r] != "Plus" 
						&& reg.tokenList[r] != "Left_Parenthesis" && reg.tokenList[r] != "Right_Parenthesis") {
					//debug
					grephy.debug("Checking the langauge for: " + reg.tokenList[r]);
					//if regex contains a character not in the files language
					if (!(new String(input.language).contains(reg.tokenList[r]))) {
						//error
						reg.error("The RegEx contains a character [" + reg.tokenList[r] + "] not in the language");
					}
				} 
			}
		}
		//if regex loading doesn't fail
		if (reg.error == 0) {
			//redefines the processed file array
			grephy.processedFile = new String[input.file.length];
			//defines the strings for outputting pass/fail
			String passText;
			String passTextA;
			//loops through the file
			for (int i = 0; i < input.file.length; i++) {
				//calls the function that processes the line
				boolean pass = grephy.process(input.file[i]+" ", reg.tokenList);
				//if pass
				if (pass) {
					//set the pass text to green and sets the pass text for the array
					passText = "" + ConsoleColors.GREEN + "Passed" + ConsoleColors.RESET;
					passTextA = "Passed";
				} else {
					//set the failed text to red and sets the failed text for the array
					passText = "" + ConsoleColors.RED + "Failed" + ConsoleColors.RESET;
					passTextA = "Failed";
				}
				//adds the result to the array
				grephy.processedFile[i] = passTextA;
				//if pass
				if (pass) {
					//output
					grephy.output("Line [" + input.file[i] + "] " + passText);
				} else {
					//debug
					grephy.debug("Line [" + input.file[i] + "] " + passText);
				}
			}
			//outputs finished 
			grephy.output("Grephy finished processing the file");
			//writes the passFail file
			WriteFile passFail = new WriteFile(grephy.completedName, grephy.processedFile, input.file, grephy.regEx, grephy.debug);
			//debug, outputs the completed file
			grephy.debug("Processed File: " + Arrays.toString(grephy.processedFile));
		} else {
			//failed
			return;
		}
	}
	
	public boolean process(String line, String[] reg) {
		//debug
		debug("Evaluating line: " + line);
		//defines the required variables
		char tempID = ' ';
		String next = "none";
		boolean skip;
		int p;
		char c = ' ';
		List<Integer> posStore = new ArrayList<Integer>();
		List<String> parStore = new ArrayList<String>();
		//if line is not empty
		if (line.length() > 0) {
			//debug
			debug("Line: " + line);
			//gets the char
			c = line.charAt(0);
			//removes the char from the line
			line = line.substring(1);
			//debug
			debug("Checking: " + c);
		}
		//loops through the reg
		for (p = 0; p < reg.length; p++) {
			//gets the string of the current reg
			String cT = reg[p];
			try {
				//tries to see if there is something next
				next = reg[p+1];
			} catch (ArrayIndexOutOfBoundsException e) {
				//if array is out of bounds then there is no next
				next = "none";
			}
			//debug
			debug("Token Array location: " + reg[p]);
			//debug
			debug("Next token: " + next);
			//if current is an *
			if (cT == "Asterisk") {
				//sets skip
				skip = true;
			//if current is a +
			} else if (cT == "Plus") {
				//sets skip
				skip = true;
			//if current is a (
			} else if (cT == "Left_Parenthesis") {
				//defines count
				int count = 0;
				//adds p to the position store
				posStore.add(p);
				//loops through the reg from that point
				for (int n = p; n < reg.length; n++) {
					//if left paren
					if (reg[n] == "Left_Parenthesis") {
						//increase count
						count++;
					//if right paren
					} else if (reg[n] == "Right_Parenthesis") {
						//decrease count
						count--;
						try {
							//tries to see the next
							if ((reg[n+1] == "Asterisk" || reg[n+1] == "Plus") && count == 0) {
								//adds to par store
								parStore.add(""+(n+1));
								parStore.add(reg[n+1]);
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							//if array out of bounds
						}
					}
				}
				//sets skip
				skip = true;
			//if current is a )
			} else if (cT == "Right_Parenthesis") {
				//if the par store is a +
				if (parStore.get(parStore.size()-1) == "Plus") {
					//remove it and add an *
					parStore.remove(parStore.size()-1);
					parStore.add("Asterisk");
				}
				if (next == "Asterisk" || next == "Plus") {
					//parStore.remove(parStore.size()-1);
					try {
						//if the line is empty
						if (line.isEmpty()) {
							try {
								//sets the next
								next = reg[p+1];
							} catch (ArrayIndexOutOfBoundsException e) {
								//if array out of bounds
								//sets next to none
								next = "none";
							}
						} else {
							//gets the last pos
							p = posStore.get(posStore.size()-1);
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						//if array out of bounds
						try {
							//removes from the pos store
							posStore.remove(posStore.size()-1);
						} catch (ArrayIndexOutOfBoundsException e2) {
							//if array out of bounds
						}
					}
				} else {
					//removes from the pos store
					posStore.remove(posStore.size()-1);
				}
				//sets skip
				skip = true;
			} else {
				//sets the temp id
				tempID = cT.charAt(0);
				//sets skip
				skip = false;
				//debug
				debug("ID to check was set to " + tempID);
			}
			//if not skipped
			if (!skip) {
				//if next is an *
				if (next == "Asterisk") {
					//while the stings next char equals the temp id
					while (tempID == c) {
						//debug
						debug("Found: " + tempID);
						//if the line is not empty
						if (line.length() > 0) {
							//debug
							debug("Line: " + line);
							//gets the next char
							c = line.charAt(0);
							//removes it from the string
							line = line.substring(1);
							//debug
							debug("Checking: " + c);
						} else {
							//sets c to a blank
							c = ' ';
							//breaks out
							break;
						}
					}
				//if next is an +
				} else if (next == "Plus") {
					//if next char equals the temp id
					if (tempID == c) {
						//while the stings next char equals the temp id
						while (tempID == c) {
							//debug
							debug("Found: " + tempID);
							if (line.length() > 0) {
								//debug
								debug("Line: " + line);
								//gets the next char
								c = line.charAt(0);
								//removes it from the string
								line = line.substring(1);
								//debug
								debug("Checking: " + c);
							} else {
								//sets c to a blank
								c = ' ';
								//breaks out
								break;
							}
						}
					} else {
						//debug
						debug("Expected " + tempID + " found " + c);
						//failed, next is set to failed
						next = "failed";
						//breaks out
						break;
					}
				//if next char equals the temp id
				} else if (tempID == c) {
					//debug
					debug("Found: " + tempID);
					//if the line is not empty
					if (line.length() > 0) {
						//debug
						debug("Line: " + line);
						//gets the next char
						c = line.charAt(0);
						//removes it from the string
						line = line.substring(1);
						//debug
						debug("Checking: " + c);
					} else {
						//sets c to a blank
						c = ' ';
					}
				//if parstore contains something
				} else if (parStore.size() > 0) {
					//if the last element is an *
					if (parStore.get(parStore.size()-1) == "Asterisk") {
						//removes the *
						parStore.remove(parStore.size()-1);
						//gets the last p from the parstore
						p = Integer.parseInt(parStore.get(parStore.size()-1));
						//removes the p
						parStore.remove(parStore.size()-1);
					} else {
						//debug
						debug("Expected " + tempID + " found " + c);
						//failed, next is set to failed
						next = "failed";
						//breaks out
						break;
					}
				} else {
					//debug
					debug("Expected " + tempID + " found " + c);
					//failed, next is set to failed
					next = "failed";
					//breaks out
					break;
				}
			} else {
				//debug
				debug("Skipping the check becuase of the " + cT);
			}
		}
		//debug
		debug("Currnet Line: " + line);
		//debug
		debug("RegEx position: " + (p) + "/" + reg.length);
		//debug
		debug("next: " + next);
		//if the end of the reg, the line is empty, and there is no char being checked
		if (next.equals("none") && (line.length() < 1) && (c == ' ')) {
			//passed
			return true;
		} else {
			//failed
			return false;
		}
	}

	public greph(String[] args) {
		//if debug is found in the parameters
		if (Arrays.asList(args).contains("-debug")) {
			//set debug true
			debug = true;
			//debug
			debug("Enabling Debug mode...");
		}
		for (int i = 0; i < args.length; i++) {
			//debug
			debug("Current parameter: " + args[i]);
			try {
				//tries to check the parameters
				if (args[i].equals("-r")) {
					//checks for issues
					if ((i+1) == args.length) {
						//error
						error("The RegEx argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						//error
						error("The RegEx argument is missing from the function call.");
					} else {
						//debug
						debug("Setting RegEx to " + args[i+1]);
						//adds the regex
						regEx = args[i+1];
						//checks for regex spaces
						if (regEx.contains(" ")) {
							//error
							error("The RegEx cannot contain spaces.");
						}
						//increases i passed reg
						i++;
					}
				} else if (args[i].equals("-f")) {
					//checks for issues
					if ((i+1) == args.length) {
						//error
						error("The input file argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-n")) || (args[i+1].equals("-r")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						//error
						error("The input file argument is missing from the function call.");
					} else {
						//debug
						debug("Setting the input file to " + args[i+1]);
						//adds the input file
						inputFile = args[i+1];
						//increases i passed the file
						i++;
					}
				} else if (args[i].equals("-n")) {
					//checks for issues
					if ((i+1) == args.length) {
						//error
						error("The NFA file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-d")) || (args[i+1].equals("-r")) || (args[i+1].equals("-f")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						//error
						error("The NFA file name argument is missing from the function call.");
					} else {
						//debug
						debug("Setting NFA output file name to " + args[i+1]);
						//adds the nfa name
						nfaName = args[i+1] + ".txt";
						//increases i passed name
						i++;
					}
				} else if (args[i].equals("-d")) {
					//checks for issues
					if ((i+1) == args.length) {
						//error
						error("The DFA file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-c")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						//error
						error("The DFA file name argument is missing from the function call.");
					} else {
						//debug
						debug("Setting DFA output file name to " + args[i+1]);
						//adds the dfa name
						dfaName = args[i+1] + ".txt";
						i++;
					}
				} else if (args[i].equals("-c")) {
					//checks for issues
					if ((i+1) == args.length) {
						//error
						error("The completed test file name argument is missing from the function call.");
					} else if ((args[i+1].equals("-debug")) || (args[i+1].equals("-r")) || (args[i+1].equals("-n")) || (args[i+1].equals("-f")) || (args[i+1].equals("-d")) || (args[i+1].equals("-h")) || (args[i+1].equals("-help"))) {
						//error
						error("The completed test file name argument is missing from the function call.");
					} else {
						//debug
						debug("Setting completed test file name to " + args[i+1]);
						//adds the completed file name
						completedName = args[i+1] + ".txt";
						i++;
					}
				} else if (args[i].equals("-debug")) {
					//holds the debug parameter slot
				} else if ((args[i].equals("-h")) || args[i].equals("-help")) {
					//outputs the help menu
					output(ConsoleColors.BLACK_BOLD + "Grephy\n"
						+  ConsoleColors.CYAN_BOLD + "-r " +  ConsoleColors.BLACK_BOLD + "\"RegEx\"                   " + ConsoleColors.RESET + "-- The input RegEx (" +  ConsoleColors.BLACK_BOLD + "required" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-f " +  ConsoleColors.BLACK_BOLD + "\"input file.txt\"          " + ConsoleColors.RESET + "-- The input file the RegEx will check (" +  ConsoleColors.BLACK_BOLD + "required" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-n " +  ConsoleColors.BLACK_BOLD + "\"nfa file name\"           " + ConsoleColors.RESET + "-- The name of the nfa output file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-d " +  ConsoleColors.BLACK_BOLD + "\"dfa file name\"           " + ConsoleColors.RESET + "-- The name of the dfa output file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-c " +  ConsoleColors.BLACK_BOLD + "\"completed file name\"     " + ConsoleColors.RESET + "-- The name of the completed test file (" +  ConsoleColors.YELLOW_BOLD + "optinal" + ConsoleColors.RESET + ")\n"
						+  ConsoleColors.CYAN_BOLD + "-h -help                     " + ConsoleColors.RESET + "-- Brings up this help menu\n"
						+  ConsoleColors.CYAN_BOLD + "-debug                       " + ConsoleColors.RESET + "-- Enables debug mode\n");
					//increases error
					error++;
					return;
				} else {
					//error, parameter failure
					error("Parameter '" + args[i] + "' does not excist.");
				}
			} catch (Exception e) {
				//catches..
			}
		}
		if (dfaName == null) {
			//sets default name
			dfaName = "DFA.txt";
		} else if (dfaName.isEmpty()) {
			//sets default name
			dfaName = "DFA.txt";
		}
		if (nfaName == null) {
			//sets default name
			nfaName = "NFA.txt";
		} else if (nfaName.isEmpty()) {
			//sets default name
			nfaName = "NFA.txt";
		}
		if (completedName == null) {
			//sets default name
			completedName = "Completed File.txt";
		} else if (completedName.isEmpty()) {
			//sets default name
			completedName = "Completed File.txt";
		}
		if (regEx == null) {
			//error for no regex
			error("A RegEx is required. -r 'RegEx'");
		}
		if (inputFile == null) {
			//error for no input file
			error("An input file is required. -f 'input file'");
		}
		if (error > 0) {
			//if errors output failed to start
			output(ConsoleColors.BLACK_BOLD + "Grephy failed to start");
			return;
		}
		//outputs graph loaded
		output(ConsoleColors.BLACK_BOLD + "Grephy loaded...");
	}
}
