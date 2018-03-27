package NFA;
import java.io.*;

/**
 * A nondeterministic finite-state automaton that 
 * accepts odd-length strings of as and odd-length
 * strings of bs.  We use a parallel-search
 * approach.
 */
public class NFAEx4 {

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
   * The transition function represented as two arrays.
   * The set of next states from a given state s and character c
   * is at delta[s,c-'a'].  The set of next states from a
   * given state s on an epsilon transition is at deltae[s].
   */
  static private int[][] delta = 
     {{0,0},   // delta[q0,a], delta[q0,b]
      {1<<2,0},  // delta[q1,a], delta[q1,b]
      {1<<1,0},  // delta[q2,a], delta[q2,b]
      {0,1<<4},  // delta[q3,a], delta[q3,b]
      {0,1<<3}}; // delta[q4,a], delta[q4,b]
  private static int[] deltae =
    {1<<1|1<<3, // delta[q0,eps]
     0,    // delta[q1,eps]
     0,    // delta[q2,eps]
     0,    // delta[q3,eps]
     0};   // delta[q4,eps]


  /**
   * Make one transition on each char in the given
   * string.
   * @param in the String to use
   */
  public void process(String in) {
    stateSet = eclose(stateSet); // initial epsilons
    for (int i = 0; i < in.length(); i++) {
      char c = in.charAt(i);
      int nextSS = 0; // next state set, initially empty
      for (int s = 0; s <= 4; s++) { // s is a state number
        if ((stateSet&(1<<s)) != 0) { // if we could be in state s
          try {
            nextSS |= delta[s][c-'a']; // include next states from s
          }
          catch (ArrayIndexOutOfBoundsException ex) {
            // no next state to include if index out of bounds
          }
        }
      }
      stateSet = eclose(nextSS); // allow epsilons after each c
    }
  }


  /**
   * eclose computes the set of states that could be reached
   * after zero or more epsilon-transitions from the given set.
   * @param ss the initial state set
   * @return the state set after zero or more epsilons
   */
  private static int eclose(int ss) {
    int lastss = 0;
    while (lastss!=ss) { // repeat until no more changes
      lastss = ss;
      for (int s = 0; s <= 4; s++) { // s is a state number
        if ((ss & 1<<s) != 0) { // if we could be in s
          ss |= deltae[s]; // include epsilons from s
        }
      }
    }
    return ss;
  }     
    

  /**
   * Test whether the DFA accepted the string.
   * @return true iff the final state was accepting
   */
  public boolean accepted() {
    return (stateSet&(1<<2 | 1<<4))!=0; // set contains q2 or q4
  }


  /**
   * main method.  Until EOF, read lines, and echo 
   * only those that the NFA accepts.
   */
  public static void main(String[] args) 
        throws IOException {

    NFAEx4 m = new NFAEx4(); // the NFA
    BufferedReader in =  // standard input
      new BufferedReader(new InputStreamReader(System.in));

    // Read and echo lines until EOF.

    String s = in.readLine();
    while (s!=null) {
      m.reset();
      m.process(s);
      if (m.accepted()) System.out.println(s);
      s = in.readLine();
    }
  }
}