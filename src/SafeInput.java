import java.util.Scanner;

public class SafeInput {

    /**
     * getUserName
     *
     * @param pipe a Scanner opened to read from System.in
     * @return a String containing the user's full name (firstName + " " + lastName)
     */

    public static String getUserName(Scanner pipe) {
        String firstName = "";
        String lastName = "";

        firstName = getNonZeroLenString(pipe, "Enter your first name");
        lastName = getNonZeroLenString(pipe, "Enter your last name");

        return firstName + " " + lastName;
    }

    /**
     * Part A: getNonZeroLenString
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a String response that is not zero length
     */

    public static String getNonZeroLenString(Scanner pipe, String prompt) {
        String retString = ""; // Set this to zero length. Loop runs until it isn't
        do {
            System.out.print("\n" + prompt + ": "); // Show prompt; add space
            retString = pipe.nextLine();
        } while (retString.length() == 0);

        return retString;
    }

    /**
     * Part B: getInt
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return an integer value entered by the user
     */

    public static int getInt(Scanner pipe, String prompt) {
        int value = 0;
        boolean done = false;
        String trash = "";

        do {
            System.out.print("\n" + prompt + ": "); // Show prompt; add space
            if (pipe.hasNextInt()) {
                // Safe to read in integer
                value = pipe.nextInt();
                pipe.nextLine(); // Clear the buffer
                done = true; // Valid input
            } else {
                // Not an integer, cannot use nextInt()
                trash = pipe.nextLine(); // Read input as String
                System.out.println("You must enter an integer! You entered an invalid input: " + trash);
            }
        } while (!done);

        return value;
    }

    /**
     * Part C: getDouble
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return a double value entered by the user
     */

    public static double getDouble(Scanner pipe, String prompt) {
        double value = 0;
        boolean done = false;
        String trash = "";

        do {
            System.out.print("\n" + prompt + ": "); // Show prompt; add space
            if (pipe.hasNextDouble()) {
                // Safe to read in double
                value = pipe.nextDouble();
                pipe.nextLine(); // Clear the buffer
                done = true; // Valid input
            } else {
                // Not a double, cannot use nextDouble()
                trash = pipe.nextLine(); // Read input as String
                System.out.println("You must enter a double! You entered an invalid input: " + trash);
            }
        } while (!done);

        return value;
    }

    /**
     * Part D: getRangedInt
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param low    the low end of the range
     * @param high   the high end of the range
     * @return an integer value within the range
     */

    public static int getRangedInt(Scanner pipe, String prompt, int low, int high) {
        int value = 0;
        boolean done = false;
        String trash = "";

        do {
            System.out.print("\n" + prompt + " [" + low + " - " + high + "]: "); // Show prompt with range
            if (pipe.hasNextInt()) {
                // Safe to read in integer
                value = pipe.nextInt();
                pipe.nextLine(); // Clear the buffer

                // Check if the value is within the specified range
                if (value >= low && value <= high) {
                    done = true; // Valid input within range
                } else {
                    System.out.println("You must enter a value within the range [" + low + " - " + high + "]!");
                }
            } else {
                // Not an integer, cannot use nextInt()
                trash = pipe.nextLine(); // Read input as String
                System.out.println("You must enter an integer! You entered an invalid input: " + trash);
            }
        } while (!done);

        return value;
    }

    /**
     * Part E: getRangedDouble
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param low    the low end of the range
     * @param high   the high end of the range
     * @return a double value within the specified range
     */

    public static double getRangedDouble(Scanner pipe, String prompt, double low, double high) {
        double value = 0;
        boolean done = false;
        String trash = "";

        do {
            System.out.print("\n" + prompt + " [" + low + " - " + high + "]: "); // Show prompt with range
            if (pipe.hasNextDouble()) {
                // Safe to read in double
                value = pipe.nextDouble();
                pipe.nextLine(); // Clear the buffer

                // Check if the value is within the specified range
                if (value >= low && value <= high) {
                    done = true; // Valid input within range
                } else {
                    System.out.println("You must enter a value within the range [" + low + " - " + high + "]!");
                }
            } else {
                // Not a double, cannot use nextDouble()
                trash = pipe.nextLine(); // Read input as String
                System.out.println("You must enter a double! You entered an invalid input: " + trash);
            }
        } while (!done);

        return value;
    }

    /**
     * Part F: getYNConfirm
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @return true for yes and false for no
     */

    public static boolean getYNConfirm(Scanner pipe, String prompt) {
        boolean value = false;
        boolean done = false;
        String input = "";

        do {
            System.out.print("\n" + prompt + " [Y/N]: "); // Show prompt with Y/N options
            input = pipe.nextLine().trim().trim(); // Read input and standardize it

            if (input.equalsIgnoreCase("Y")) {
                value = true;
                done = true;
            } else if (input.equalsIgnoreCase("N")) {
                value = false;
                done = true;
            } else {
                System.out.println("You must enter Y or N!");
            }
        } while (!done);

        return value;
    }

    /**
     * Part G: getRegExString
     *
     * @param pipe   a Scanner opened to read from System.in
     * @param prompt prompt for the user
     * @param regEx  the RegEx pattern to match
     * @return a String that matches the RegEx pattern
     */

    public static String getRegExString(Scanner pipe, String prompt, String regEx) {
        String value = "";
        boolean done = false;

        do {
            System.out.print("\n" + prompt + ": "); // Show prompt
            value = pipe.nextLine(); // Read input as String

            if (value.matches(regEx)) {
                done = true; // Valid input matching the pattern
            } else {
                System.out.println("Input must match the required pattern. Please try again.");
            }
        } while (!done);

        return value;
    }

    /**
     * Part H: prettyHeader
     */

    public static void prettyHeader(String msg) {
        // First line of all asterisks
        for (int i = 0; i < 60; i++) {
            System.out.print("*");
        }
        System.out.println();

        // Calculate spaces before and after
        int spacesBefore = (60 - msg.length() - 6) / 2;
        int spacesAfter = 60 - msg.length() - 6 - spacesBefore;

        // Print 3 asterisks
        System.out.print("***");

        // Print spaces before message
        for (int i = 0; i < spacesBefore; i++) {
            System.out.print(" ");
        }

        // Print message
        System.out.print(msg);

        // Print spaces after message
        for (int i = 0; i < spacesAfter; i++) {
            System.out.print(" ");
        }

        // Print 3 asterisks
        System.out.println("***");

        // Third line of all asterisks
        for (int i = 0; i < 60; i++) {
            System.out.print("*");
        }
    }
}