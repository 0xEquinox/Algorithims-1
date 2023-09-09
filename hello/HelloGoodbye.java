/* *****************************************************************************
 *  Name:              Will McDonald
 *  Coursera User ID:  William McDonald
 *  Last modified:     September 7th, 2023
 **************************************************************************** */

public class HelloGoodbye {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Hello World");
        }
        else {
            System.out.println("Hello " + args[0] + " and " + args[1] + ".");
            System.out.println("Goodbye " + args[1] + " and " + args[0] + ".");
        }
    }
}
