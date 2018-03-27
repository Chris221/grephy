package NFA;
import java.io.*;

/**
 * A nondeterministic finite-state automaton that 
 * recognizes strings of as and bs with at least
 * two consecutive as.  We use a parallel-search
 * approach.
 */
public class NFAEx2 {

  /*
   * The current set of states, encoded bitwise: state i is 
   * represented by the bit 1<<i.
   */
  private int stateSet;

  /**
   * Reset the current state set to {the start state}.
   */
  public void reset() {
    stateSet = 1<<0; // {q0}
  }

  /*
   * The transition function represented as an array.
   * The set of next states from a given state s and character c
   * is at delta[s,c-'a'].
   */
  static private int[][] delta = 
     {{1<<0|1<<1, 1<<0}, // delta[q0,a] = {q0,q1}
                         // delta[q0,b] = {q0}
      {1<<2, 0},     // delta[q1,a] = {q2}, delta[q1,b] = {}
      {1<<2, 1<<2}}; // delta[q2,a] = {q2}, delta[q2,b] = {q2}

  /**
   * Make one transition on each char in the given
   * string.
   * @param in the String to use
   */
  public void process(String in) {
    for (int i = 0; i < in.length(); i++) {
      char c = in.charAt(i);
      int nextSS = 0; // next state set, initially empty
      for (int s = 0; s <= 2; s++) { // s is a state number
        if ((stateSet&(1<<s)) != 0) { // if we could be in state s
          try {
            nextSS |= delta[s][c-'a']; // include next states from s
          }
          catch (ArrayIndexOutOfBoundsException ex) {
            // no next state to include if index out of bounds
            System.out.println("Invalid alphabet symbol.  Please use {a,b}");
          }
        }
      }
      stateSet = nextSS; // this is the set after character c
    }
  }


  /**
   * Test whether the DFA accepted the string.
   * @return true iff the final state was accepting
   */
  public boolean accepted() {
    return (stateSet&(1<<2))!=0; // true if set contains q2
  }


  /**
   * main method.  Until EOF, read lines, and echo 
   * only those that the NFA accepts.
   */
  public static void main(String[] args) 
        throws IOException {

    NFAEx2 m = new NFAEx2(); // the NFA
    BufferedReader in =  // standard input
      new BufferedReader(new InputStreamReader(System.in));

    // Read and echo lines until EOF.
    System.out.println("Enter a string:");
    String s = in.readLine();
    while (s!=null) {
      m.reset();
      m.process(s);
      if (m.accepted()) {
        System.out.println("Accepted: "+s);
      }
      else {
        System.out.println("Rejected: "+s);
      }
      s = in.readLine();
    }
  }
}