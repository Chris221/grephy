import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class WriteFile extends output {
	private String fileName;
	private String text;
	private String[] processedFile;
	private String[] file;
	private String RegEx;
	
	public WriteFile(String fileName, String[] processedFile, String[] file, String RegEx, boolean masterDebug) {
		debug = masterDebug;
		this.fileName = fileName;
		this.file = file;
		this.processedFile = processedFile;
		this.processFile();
		this.saveFile();
	}
	
	private void processFile() {
		this.text = "RegEx: "+ this.RegEx + "\n";
		for (int i = 0; i < this.file.length; i++) {
			this.text += this.processedFile[i].toUpperCase() + ": " + this.file[i] + "\n";
		}
	}

	private void saveFile() {
		PrintWriter out;
		try {
			out = new PrintWriter(this.fileName);
			out.println(this.text);
			out.close();
			output("A file explaining which passed/failed was created with the name \""+this.fileName+"\"");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			error("Grephy failed to write the pass/fail file.");
		}
	}
}
