/* *****************************************************************************
 *  Name:              Will McDonald
 *  Coursera User ID:  William McDonald
 *  Last modified:     September 7th, 2023
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        for (int i = 1; !StdIn.isEmpty(); i++) {
            String nextWord = StdIn.readString();
            if (StdRandom.bernoulli((double) 1 / i)) {
                champion = nextWord;
            }
        }
        StdOut.println(champion);
    }
}
