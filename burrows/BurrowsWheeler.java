import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int ALPHABET_SIZE = 256;

    // Apply Burrows-Wheeler transform
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        int first = findFirst(csa, input.length());
        BinaryStdOut.write(first);

        for (int i = 0; i < input.length(); i++) {
            int lastIdx = (csa.index(i) - 1 + input.length()) % input.length();
            BinaryStdOut.write(input.charAt(lastIdx));
        }

        BinaryStdOut.close();
    }

    // Find the index of the first row in the sorted suffix array
    private static int findFirst(CircularSuffixArray csa, int len) {
        for (int i = 0; i < len; i++) {
            if (csa.index(i) == 0)
                return i;
        }
        return -1;
    }

    // Apply Burrows-Wheeler inverse transform
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String lastCol = BinaryStdIn.readString();

        int len = lastCol.length();
        int[] next = new int[len];
        int[] count = new int[ALPHABET_SIZE + 1];
        char[] firstCol = new char[len];

        countOccurrences(lastCol, count);

        populateFirstColAndNextArray(lastCol, len, count, firstCol, next);

        for (int i = 0; i < len; i++) {
            BinaryStdOut.write(firstCol[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // Count occurrences of characters in the last column
    private static void countOccurrences(String lastCol, int[] count) {
        for (int i = 0; i < lastCol.length(); i++)
            count[lastCol.charAt(i) + 1]++;
    }

    // Populate first column and next array
    private static void populateFirstColAndNextArray(String lastCol, int len, int[] count,
                                                     char[] firstCol, int[] next) {
        for (int i = 0; i < ALPHABET_SIZE; i++)
            count[i + 1] += count[i];

        for (int i = 0; i < len; i++) {
            int posi = count[lastCol.charAt(i)]++;
            firstCol[posi] = lastCol.charAt(i);
            next[posi] = i;
        }
    }

    // Main method
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        }
        else if (args[0].equals("+")) {
            inverseTransform();
        }
    }
}
