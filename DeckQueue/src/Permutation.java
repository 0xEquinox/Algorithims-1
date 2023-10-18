import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int numberToIterate = Integer.parseInt(args[0]);
        RandomizedQueue<String> items = new RandomizedQueue<>();

        for (int i = 0; !StdIn.isEmpty(); i++) {
            items.enqueue(StdIn.readString());
        }

        int count = 0;
        for (String item : items) {
            if (count < numberToIterate) {
                System.out.println(item);
                count += 1;
            }
        }
    }
}
