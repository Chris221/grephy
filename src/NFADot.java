import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NFADot extends output {
	//Defines the required variables 
	private String fileName;
	private String[] reg;
	private String text;
	private String kind = "NFA";
	private int num = 0;
	private List<Integer> parlocation = new ArrayList<Integer>();
	
	public NFADot(String name, String[] regEx, boolean masterDebug) {
		//Sets the required variables
		debug = masterDebug;
		this.fileName = name;
		this.reg = regEx;
		//calls the start function
		this.process();
	}
	
	public void process() {
		//start if the file
		this.text = "digraph "+this.kind+" {\n";
		this.text += "q"+this.num+";\n";
		//calls the start of the loop
		this.text += this.start();
		
		this.text += " }";
		//calls the save file function
		this.saveFile();
	}
	
	private void saveFile() {
		//defines the printer
		PrintWriter out;
		try {
			//tries to write to the file
			out = new PrintWriter(this.fileName);
			out.println(this.text);
			out.close();
			//outputs the success text
			output("NFA was created with the name \""+this.fileName+"\"");
		} catch (FileNotFoundException e) {
			//if it failed  to write to the file
			e.printStackTrace();
			//outputs failed text
			error("Grephy failed to write the NFA to the file.");
		}
	}

	private String start() {
		//sets the string
		String t = "";
		//loops through regex
		for (int i = 0; i < this.reg.length; i++) {
			//outputs a debug of which element its looking at
			debug("NFA -- looking at: "+this.reg[i]);
			try {
				//tries to react based on the regex or its one look ahead
				if (this.reg[i] == "Left_Parenthesis") {
					//debug text
					debug("NFA -- Left Parenthesis");
					//adds a par location
					this.parlocation.add(num);
				} else if (this.reg[i] == "Right_Parenthesis") {
					//debug text
					debug("NFA -- Right Parenthesis");
					//if the next is a + or *
					if (this.reg[i+1] == "Plus" || this.reg[i+1] == "Asterisk") {
						//moves back from the regex
						int oLoc = this.parlocation.get(this.parlocation.size()-1);
						this.parlocation.remove(this.parlocation.size()-1);
						//moves
						t += this.moveTo(this.num, oLoc, "e");
						//increase i past the +/*
						i++;
						if (this.reg.length-1 == i) {
							//if end of regex its accepting
							t += accepting(this.num);
						}
					} else {
						//debug text
						debug("NFA -- No Asterisk or Plus after the Right_Parenthesis");
					}
				} else if (this.reg[i+1] == "Plus") {
					//debug text
					debug("NFA -- Plus");
					//moves
					t += this.moveTo(this.num, this.num+1, this.reg[i]);
					//increases num
					num++;
					//moves
					t += this.moveTo(this.num, this.num, this.reg[i]);
					//increase i past the +
					i++;
					if (this.reg.length-1 == i) {
						//if end of regex its accepting
						t += accepting(this.num);
					}
				} else if (this.reg[i+1] == "Asterisk") {
					//debug text
					debug("NFA -- Asterisk");
					//moves
					t += this.moveTo(this.num, this.num, this.reg[i]);
					//increase i past the *
					i++;
					if (this.reg.length-1 == i) {
						//if end of regex its accepting
						t += accepting(this.num);
					}
				} else {
					//debug text
					debug("NFA -- None");
					//moves
					t += this.moveTo(this.num, this.num+1, this.reg[i]);
					//increases num
					num++;
					if (this.reg.length-1 == i) {
						//if end of regex its accepting
						t += accepting(this.num);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				//debug text
				debug("NFA -- None");
				//moves
				t += this.moveTo(this.num, this.num+1, this.reg[i]);
				//increases num
				num++;
				if (this.reg.length-1 == i) {
					//if end of regex its accepting
					t += accepting(this.num);
				}
			}
		}
		
		//returns the text
		return t;
	}

	private String moveTo(int from, int to, String label) {
		//returns the move string
		return ("q"+from+" -> q"+to+"  [label=\""+label+"\"];\n");
	}
	
	private String accepting(int l) {
		//returns the accepted string
		return  ("q"+l+" [peripheries=2];\n");
	}
}
