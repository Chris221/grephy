# Grephy

## How to run
java -jar Grephy.jar -r _"RegEx"_ -f _"Input file.txt"_

### Parameters
**-r** _"RegEx"_         -- The input RegEx (**required**)

**-f** _"input file"_    -- The input file the RegEx will check (**required**)

**-n** _"nfa file name"_ -- The name of the nfa output file (_optinal_)

**-d** _"dfa file name"_ -- The name of the dfa output file (_optinal_)

**-h** **-help**         -- Brings up this help menu

**-debug**               -- Enables debug mode


_Parameter Order is not important_

### RegEx Reserved Symbols
***** Asterisk, or _Splat_, means the proceeding character or list of characters does not have to be there but may repeat; **0 or more times**


**+** Plus means the proceeding character or list of characters must be there but may repeat; **1 or more times**


**( )** Parenthesis group characters together typically followed by a Plus or Splat.

All other characters or symbols may be used in the test file or language.

### Test Files
**simple.txt** a simple single line file that matches _(ab)*_

**multiple.txt** a multi-line file that matches _a*_ or _(a)*_

**negative.txt** a multi-line file that **does NOT** matches any particular expression _Looks like it would take (ab)*_

**t.txt** a very large and inclusive file, no set expression matches

**t2.txt** a simpler test file was used to test _(ab)*_ and _(ab)*c_

**numbers.txt** a test file for testing numbers that matches _1*01_

**fun.txt** a test file for testing symbols that matches _#*$*@_

## Online DOT viewer
https://dreampuf.github.io/GraphvizOnline/

## Project File
https://github.com/Chris221/grephy/blob/master/Project.pdf
