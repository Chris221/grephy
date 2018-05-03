import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriteFile extends output {
	//Defines the required variables 
	private String fileName;
	private String text;
	private String[] processedFile;
	private String[] file;
	private String RegEx;
	
	public WriteFile(String fileName, String[] processedFile, String[] file, String RegEx, boolean masterDebug) {
		//sets the required elements
		debug = masterDebug;
		this.fileName = fileName;
		this.file = file;
		this.processedFile = processedFile;
		//processes the file
		this.processFile();
		//saves the file
		this.saveFile();
	}
	
	private void processFile() {
		//adds the regex at the top
		this.text = "RegEx: "+ this.RegEx + "\n";
		//loops through the file
		for (int i = 0; i < this.file.length; i++) {
			//adds the line and if it pass/failed
			this.text += this.processedFile[i].toUpperCase() + ": " + this.file[i] + "\n";
		}
	}

	private void saveFile() {
		//defines the printer
		PrintWriter out;
		try {
			//tries to write to the file
			out = new PrintWriter(this.fileName);
			out.println(this.text);
			out.close();
			//outputs success
			output("A file explaining which passed/failed was created with the name \""+this.fileName+"\"");
		} catch (FileNotFoundException e) {
			//if it failed  to write to the file
			e.printStackTrace();
			//outputs failed text
			error("Grephy failed to write the pass/fail file.");
		}
	}
}
