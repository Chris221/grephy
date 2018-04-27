import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DFADot extends output {
	private String fileName;
	private String[] reg;
	private String text;
	private String kind = "DFA";
	private int num = 0;
	private List<Integer> parlocation = new ArrayList<Integer>();
	private List<Integer> idlocation = new ArrayList<Integer>();
	
	public DFADot(String name, String[] regEx, boolean masterDebug) {
		debug = masterDebug;
		this.fileName = name;
		this.reg = regEx;
		this.process();
	}
	
	public void process() {
		this.text = "digraph "+this.kind+" {\n";
		this.text += "q"+this.num+";\n";
		this.text += this.start();
		
		this.text += " }";
		this.saveFile();
	}
	
	private void saveFile() {
		PrintWriter out;
		try {
			out = new PrintWriter(this.fileName);
			out.println(this.text);
			out.close();
			output("DFA was created with the name \""+this.fileName+"\"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			error("Grephy failed to write the DFA to the file.");
		}
	}

	private String start() {
		String t = "";
		for (int i = 0; i < this.reg.length; i++) {
			debug("DFA -- looking at: "+this.reg[i]);
			try {
				if (this.reg[i] == "Left_Parenthesis") {
					debug("DFA -- Left Parenthesis");
					this.parlocation.add(num);
					idlocation.add(i+1);
				} else if (this.reg[i] == "Right_Parenthesis") {
					debug("DFA -- Right Parenthesis");
					if (this.reg[i+1] == "Plus" || this.reg[i+1] == "Asterisk") {
						int oLoc = this.parlocation.get(this.parlocation.size()-1);
						int oIDLoc = this.idlocation.get(this.idlocation.size()-1);
						this.parlocation.remove(this.parlocation.size()-1);
						t += this.moveTo(this.num, (oLoc+1), this.reg[oIDLoc]);
						i++;
						if (this.reg.length-1 == i) {
							t += accepting(this.num);
						}
					} else {
						debug("DFA -- No Asterisk or Plus after the Right_Parenthesis");
					}
				} else if (this.reg[i+1] == "Plus") {
					debug("DFA -- Plus");
					t += this.moveTo(this.num, this.num+1, this.reg[i]);
					num++;
					t += this.moveTo(this.num, this.num, this.reg[i]);
					i++;
					if (this.reg.length-1 == i) {
						t += accepting(this.num);
					}
				} else if (this.reg[i+1] == "Asterisk") {
					debug("DFA -- Asterisk");
					t += this.moveTo(this.num, this.num, this.reg[i]);
					i++;
					if (this.reg.length-1 == i) {
						t += accepting(this.num);
					}
				} else {
					debug("DFA -- None");
					t += this.moveTo(this.num, this.num+1, this.reg[i]);
					num++;
					if (this.reg.length-1 == i) {
						t += accepting(this.num);
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				debug("DFA -- None");
				t += this.moveTo(this.num, this.num+1, this.reg[i]);
				num++;
				if (this.reg.length-1 == i) {
					t += accepting(this.num);
				}
			}
		}
		
		
		return t;
	}

	private String moveTo(int from, int to, String label) {
		return ("q"+from+" -> q"+to+"  [label=\""+label+"\"];\n");
	}
	
	private String accepting(int l) {
		return  ("q"+l+" [peripheries=2];\n");
	}
}