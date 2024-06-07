import java.util.Arrays;

public class CircularSuffixArray {
    private final String originalString;
    private final Integer[] indices;

    // CircularSuffixArray constructor
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        originalString = s;
        indices = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            indices[i] = i;
        }
        Arrays.sort(indices, (a, b) -> compareCircularSuffixes(a, b));
    }

    // Length of the input string
    public int length() {
        return originalString.length();
    }

    // Returns index of the ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= originalString.length()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        return indices[i];
    }

    // Compare circular suffixes starting at index i and j
    private int compareCircularSuffixes(int i, int j) {
        for (int k = 0; k < originalString.length(); k++) {
            char charI = originalString.charAt((i + k) % originalString.length());
            char charJ = originalString.charAt((j + k) % originalString.length());
            if (charI != charJ) {
                return charI - charJ;
            }
        }
        return 0; // Suffixes are identical
    }

    // Test client (optional)
    public static void main(String[] args) {
        String input = "banana";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(input);
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            System.out.println(circularSuffixArray.index(i));
        }
    }
}
