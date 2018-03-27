import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileLoader extends output {

	public char[] language;
	public String[] file;
	
	public FileLoader(String fileLocation, Boolean masterDebug) {
		debug = masterDebug;
		try {
			debug("Attempting to load the file " + fileLocation);
			BufferedReader input = new BufferedReader(new FileReader(fileLocation));
			output("Input file found!");
			String temp = input.readLine();
			String lString = "";
			int numberOfLines = 0;
			while (temp != null) {
				debug("Processing Line: " + temp);
				for (char c : temp.toCharArray()){
				    if (lString.indexOf(c) < 0) {
				    	lString += c;
				    }
				}
				temp = input.readLine();
				numberOfLines++;
			}
			debug("language as a String: " + lString);
			language = new char [lString.length()];
			int t = 0;
			for (char i : lString.toCharArray()) {
				language[t] = i;
				t++;
			}
			debug("Language Learned: " + Arrays.toString(language));
			input.close();
			BufferedReader input2 = new BufferedReader(new FileReader(fileLocation));
			file = new String[numberOfLines];
			for (int i = 0; i < numberOfLines; i++) {
				file[i] = input2.readLine();
			}
			debug("File: " + Arrays.toString(file));
			output("File processed");
			input2.close();
		} catch (FileNotFoundException e) {
			error("Failed to load the file.");
			return;
		} catch (IOException e) {
			error("IOException occured..\nPrinting Stack..");
			e.printStackTrace();
		}
		
	}
}
