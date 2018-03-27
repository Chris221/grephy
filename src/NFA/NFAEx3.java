package NFA;
import java.io.*;

/**
 * A nondeterministic finite-state automaton that 
 * accepts odd-length strings of as and odd-length
 * strings of bs.  We use a backtracking-search
 * approach.
 */
public class NFAEx3 {

  /*
   * The transition function represented as two arrays.  The
   * entry at delta[s,c-'a'] is an array of 0 or more ints,
   * one for each possible move from state s on character c.
   * The entry at deltae[s] is an array of 0 or more ints,
   * one for each possible epsilon-transition from state s.
   */
  private static int[][][] delta = 
     {{{},{}},   // delta[q0,a], delta[q0,b]
      {{2},{}},  // delta[q1,a], delta[q1,b]
      {{1},{}},  // delta[q2,a], delta[q2,b]
      {{},{4}},  // delta[q3,a], delta[q3,b]
      {{},{3}}}; // delta[q4,a], delta[q4,b]
  private static int[][] deltae =
    {{1,3}, // delta[q0,eps]
     {},    // delta[q1,eps]
     {},    // delta[q2,eps]
     {},    // delta[q3,eps]
     {}};   // delta[q4,eps]


  /**
   * Test whether there is some path for the NFA to reach
   * an accepting state, from the given state and reading
   * the given string at the given character position.
   * @param s the current state
   * @param in the input string
   * @param pos the index of the next character in the string
   * @return true iff the NFA accepts on some path
   */
  private static boolean accepts(int s, String in, int pos) {
    if (pos==in.length()) { // if no more symbols to read
      return (s==2 || s==4); // accept iff q2 or q4
    }

    char c = in.charAt(pos); // get char
    int[] nextStates;
    try {
      nextStates = delta[s][c-'a'];
    }
    catch (ArrayIndexOutOfBoundsException ex) {
      return false; // no transition, just reject
    }

    // At this point, nextStates is an array of 0 or
    // more next states.  Try each move recursively;
    // if it leads to an accepting state return true.

    for (int i=0; i < nextStates.length; i++) {
      if (accepts(nextStates[i], in, pos+1)) return true;
    }

    // Try the same thing for any epsilon transitions.

    nextStates = deltae[s];    
    for (int i=0; i < nextStates.length; i++) {
      if (accepts(nextStates[i], in, pos)) return true;
    }

    return false; // all moves fail, return false
  }


  /**
   * Test whether the NFA accepts the string.
   * @param in the String to test
   * @return true iff the NFA accepts on some path
   */
  public static boolean accepts(String in) {
    return accepts(0, in, 0); // start in q0 at char 0
  }


  /**
   * main method.  Until EOF, read lines, and echo 
   * only those that the NFA accepts.
   */
  public static void main(String[] args) 
        throws IOException {

    BufferedReader in =  // standard input
      new BufferedReader(new InputStreamReader(System.in));

    // Read and echo lines until EOF.

    String s = in.readLine();
    while (s!=null) {
      if (NFAEx3.accepts(s)) System.out.println(s);
      s = in.readLine();
    }
  }
}