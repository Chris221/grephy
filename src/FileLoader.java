import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileLoader extends output {
	//Defines the required variables 
	public char[] language;
	public String[] file;
	
	public FileLoader(String fileLocation, Boolean masterDebug) {
		//sets debug
		debug = masterDebug;
		try {
			//tries to load the file
			//debug
			debug("Attempting to load the file " + fileLocation);
			//opens the file
			BufferedReader input = new BufferedReader(new FileReader(fileLocation));
			//outputs file found
			output("Input file found!");
			//gets the first line
			String temp = input.readLine();
			//defines required variables 
			String lString = "";
			int numberOfLines = 0;
			//while temp is not null
			while (temp != null) {
				//debug
				debug("Processing Line: " + temp);
				//loops through the line
				for (char c : temp.toCharArray()){
					//if the character is not in the language string
				    if (lString.indexOf(c) < 0) {
				    	//adds the char to the sting
				    	lString += c;
				    }
				}
				//gets the next line
				temp = input.readLine();
				//increases the line number
				numberOfLines++;
			}
			//debug
			debug("language as a String: " + lString);
			//redefines the language array
			language = new char [lString.length()];
			//defines a temp int counter
			int t = 0;
			//loops through language string
			for (char i : lString.toCharArray()) {
				//adds the language to an array
				language[t] = i;
				//increases the temp int
				t++;
			}
			//debug
			debug("Language Learned: " + Arrays.toString(language));
			//closes the input file
			input.close();
			//opens a new file feed
			BufferedReader input2 = new BufferedReader(new FileReader(fileLocation));
			//redefines the file array
			file = new String[numberOfLines];
			//loops through the file
			for (int i = 0; i < numberOfLines; i++) {
				//adds the file to an array
				file[i] = input2.readLine();
			}
			//debug
			debug("File: " + Arrays.toString(file));
			//outputs passed
			output("File processed");
			//closes the input file
			input2.close();
		} catch (FileNotFoundException e) {
			//if could not find the file
			//error
			error("Failed to load the file.");
			return;
		} catch (IOException e) {
			//if there is an input/output error
			//erorr
			error("IOException occured..\nPrinting Stack..");
			e.printStackTrace();
		}
		
	}
}
