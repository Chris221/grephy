import java.util.Arrays;

public class RegEx extends output {
	//Defines the required variables 
	public String regEx;
	public char[] language;
	public String[] tokenList = new String[0];

	public RegEx(String reg, Boolean masterDebug, char[] lang) {
		//sets the required elements
		debug = masterDebug;
		regEx = reg;
		language = lang;
		int parenthesis = 0;
		
		output("Checking the RegEx..");
		for (int i = 0; i < regEx.length(); i++){
			//gets the char
			char c = regEx.charAt(i);
			//compares the char
			if (c == '(') {
	    		//calls add to
		    	addTo("Left_Parenthesis");
		    	//increases parenthesis
		    	parenthesis++;
		    } else if (c == ')') {
		    	if (i == 0) {
		    		//error
		    		error("There cannot be a Right Parenthesis ')' at the start of the RegEx");
		    		return;
		    	} else if (parenthesis > 0) {
			    	if (regEx.charAt(i-1) == '(') {
			    		//warning
			    		warning("A Right Parenthesis was found right after the Left Parenthesis in the RegEx");
			    		//calls add to
				    	addTo("Right_Parenthesis");
			    	} else {
			    		//calls add to
				    	addTo("Right_Parenthesis");
			    	}
			    	//decreases parenthesis
			    	parenthesis--;
		    	} else {
		    		//error
					error("Parenthesis Missmatch in the RegEx");
					return;
		    	}
		    }else if (c == '+') {
		    	if (i == 0) {
		    		//error
		    		error("There cannot be a Plus '+' at the start of the RegEx");
		    		return;
		    	} else if (regEx.charAt(i-1) == '+') {
		    		//error
		    		error("A Plus '+' cannot follow a Plus '+' in the RegEx");
		    		return;
		    	} else if (regEx.charAt(i-1) == '*') {
		    		//error
		    		error("A Plus '+' cannot follow an Asterisk '*' in the RegEx");
		    		return;
		    	} else if (regEx.charAt(i-1) == '(') {
		    		//error
		    		error("A Plus '+' cannot follow a Left Parenthesis '(' in the RegEx");
		    		return;
		    	} else {
		    		//calls add to
			    	addTo("Plus");
		    	}
		    } else if (c == '*') {
		    	if (i == 0) {
		    		//error
		    		error("There cannot be an Asterisk '*' at the start of the RegEx");
		    		return;
		    	} else if (regEx.charAt(i-1) == '+') {
		    		//error
		    		error("An Asterisk '*' cannot follow a Plus '+' in the RegEx");
		    		return;
		    	} else if (regEx.charAt(i-1) == '*') {
		    		//error
		    		error("An Asterisk '*' cannot follow an Asterisk '*' in the RegEx");
		    		return;
		    	} else if (regEx.charAt(i-1) == '(') {
		    		//error
		    		error("An Asterisk '*' cannot follow a Left Parenthesis '(' in the RegEx");
		    		return;
		    	} else {
		    		//calls add to
			    	addTo("Asterisk");
		    	}
		    } else {
		    	//calls add to
		    	addTo("" + c);
			}
		}
		//if the parens dont equal 0
		if (parenthesis != 0) {
			//error
			error("Parenthesis Missmatch in the RegEx");
			return;
		}
		//debug
    	debug("Token Array from RegEx: " + Arrays.toString(tokenList));
    	
    	//output success
		output("RegEx processed successfully!");
	}

	public void addTo(String newVal){
        //define the new array
        String[] newArray = new String[tokenList.length + 1];
 
        //copy values into new array
        for(int i=0;i < tokenList.length;i++)
            newArray[i] = tokenList[i];
        //another solution is to use 
        //System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
 
        //add new value to the new array
        newArray[newArray.length-1] = newVal;
 
        //copy the address to the old reference 
        //the old array values will be deleted by the Garbage Collector
        tokenList = newArray;
	}
}
