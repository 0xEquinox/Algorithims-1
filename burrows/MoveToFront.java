import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] aux = initializeAuxiliaryArray();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int idx = findIndex(aux, c);
            BinaryStdOut.write((char) idx);

            // Move character to front
            moveToFront(aux, idx, c);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] aux = initializeAuxiliaryArray();

        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar();
            char c = aux[idx];
            BinaryStdOut.write(c);

            // Move character to front
            moveToFront(aux, idx, c);
        }

        BinaryStdOut.close();
    }

    // Initialize auxiliary array
    private static char[] initializeAuxiliaryArray() {
        char[] aux = new char[R];
        for (int i = 0; i < R; i++)
            aux[i] = (char) i;
        return aux;
    }

    // Find index of character c in array aux
    private static int findIndex(char[] aux, char c) {
        for (int i = 0; i < R; i++) {
            if (aux[i] == c)
                return i;
        }
        return -1; // Character not found
    }

    // Move character at index idx to the front of array aux
    private static void moveToFront(char[] aux, int idx, char c) {
        for (int i = idx; i > 0; i--)
            aux[i] = aux[i - 1];
        aux[0] = c;
    }

    // If args[0] is '-', apply move-to-front encoding
    // If args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) MoveToFront.encode();
        if (args[0].equals("+")) MoveToFront.decode();
    }
}