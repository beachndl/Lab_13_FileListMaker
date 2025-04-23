import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileListMaker {
    // ArrayList
    private static final ArrayList<String> itemList = new ArrayList<>();

    // Tracking variables
    private static boolean needsSaved = false;
    private static String currentFileName = "";

    public static void main(String[] args) {
        // Scanner + variable declaration
        Scanner in = new Scanner(System.in);
        String menuChoice = "";
        boolean done = false;

        // Main loop
        try {
            do {
                // Display the current list + menu
                displayListAndMenu();

                // Using getRedExString method from SafeInput.java to get user menu choice
                menuChoice = SafeInput.getRegExString(in, "Enter your choice (A|a|D|d|I|i|M|m|O|o|S|s|C|c|V|v|Q|q)", "[AaDdIiMmOoSsCcVvQq]");
                menuChoice = menuChoice.toUpperCase(); // Convert to uppercase for easier comparison

                // Process user choice
                switch (menuChoice) {
                    case "A":
                        addItem(in);
                        break;
                    case "D":
                        deleteItem(in);
                        break;
                    case "I":
                        insertItem(in);
                        break;
                    case "M":
                        moveItem(in);
                        break;
                    case "O":
                        openFile(in);
                        break;
                    case "S":
                        saveFile(in);
                        break;
                    case "C":
                        clearList(in);
                        break;
                    case "V":
                        viewList();
                        break;
                    case "Q":
                        done = confirmQuit(in);
                        break;
                }

            } while (!done);
        } catch (IOException e) {
            System.out.println("Error with files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static method to display current list + menu options
    private static void displayListAndMenu() {
        // Variable declaration
        String item = "";

        // Print information
        System.out.println("\n-------- File List Maker -------");

        // Condition to display current file name if it exists
        if (!currentFileName.isEmpty()) {
            System.out.println("Current File: " + currentFileName + (needsSaved ? " *" : ""));
        } else {
            System.out.println("Current File: [None]" + (needsSaved ? " *" : ""));
        }

        // Print information
        System.out.println("Current List:");

        // Condition to check if list is empty
        if (itemList.isEmpty()) {
            System.out.println("No items in the list.");
        } else {
            for (int i = 0; i < itemList.size(); i++) {
                item = itemList.get(i);
                System.out.println((i + 1) + ". " + item);
            }
        }

        // Print menu options
        System.out.println("\nMenu Options:");
        System.out.println("A – Add an item to the list");
        System.out.println("D – Delete an item from the list");
        System.out.println("I – Insert an item into the list");
        System.out.println("M – Move an item in the list");
        System.out.println("O – Open a list file from disk");
        System.out.println("S – Save the current list file to disk");
        System.out.println("C – Clear the current list");
        System.out.println("V – View the list");
        System.out.println("Q – Quit the program");
    }

    // Static method to add a new item to the end of the list
    private static void addItem(Scanner pipe) {
        // Variable declaration
        String newItem = "";

        // Using getNonZeroLenString method from SafeInput.java to add item
        newItem = SafeInput.getNonZeroLenString(pipe, "Enter the item to add");
        itemList.add(newItem); // Add element to the end of the list
        System.out.println("\nItem added successfully.");

        // Mark as needing to be saved
        needsSaved = true;
    }

    // Static method to delete an item from the list based on position
    private static void deleteItem(Scanner pipe) {
        // Variable declaration
        int itemNum;
        String removedItem = "";

        // Condition to check if list is empty
        if (itemList.isEmpty()) {
            System.out.println("No items in the list.");
            return;
        }

        // Display the list with numbers
        printNumberedList();

        // Using getRedExString method from SafeInput.java to get item number to delete
        itemNum = SafeInput.getRangedInt(pipe, "Enter the item number to delete", 1, itemList.size());

        // Delete the item; account for 0-based index
        removedItem = itemList.remove(itemNum - 1);
        System.out.println("\nItem \"" + removedItem + "\" has been deleted.");

        // Mark as needing to be saved
        needsSaved = true;
    }

    // Static method to insert an item at a specific position in the list
    private static void insertItem(Scanner pipe) {
        // Variable declaration
        int position;
        String newItem = "";

        // Condition to check if list is empty; add to the beginning if empty
        if (itemList.isEmpty()) {
            System.out.println("No items in the list. Item will be added at the beginning.");
            addItem(pipe);
            return;
        }

        // Display the current list
        printNumberedList();

        // Using getRangedInt method from SafeInput.java to get the position to insert
        position = SafeInput.getRangedInt(pipe, "Enter position to insert (1 to " + (itemList.size() + 1) + ")", 1, itemList.size() + 1);

        // Using getNonZeroLenString method from SafeInput.java to get the item to insert
        newItem = SafeInput.getNonZeroLenString(pipe, "Enter the item to insert");

        // Insert the item at specified position; account for 0-based index
        itemList.add(position - 1, newItem);
        System.out.println("\nItem inserted successfully at position " + position + ".");

        // Mark as needing to be saved
        needsSaved = true;
    }

    // Static method to display the numbered version of the list
    private static void printNumberedList() {
        // Variable declaration
        String item = "";

        // Print information
        System.out.println("\nCurrent List:");

        for (int i = 0; i < itemList.size(); i++) {
            item = itemList.get(i); // Reading element at index i
            System.out.println((i + 1) + ". " + item);
        }
    }

    // Static method to confirm if user wants to quit
    private static boolean confirmQuit(Scanner pipe) {
        // Check if the list needs to be saved
        if (needsSaved) {
            boolean saveBeforeQuit = SafeInput.getYNConfirm(pipe, "You have unsaved changes. Do you want to save before quitting?");

            // Condition for saveBeforeQuit
            if (saveBeforeQuit) {
                try {
                    saveFile(pipe);
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
            }
        }

        // Using getYNConfirm method from SafeInput.java to ask user if they are sure they want to quit
        return SafeInput.getYNConfirm(pipe, "Are you sure you want to quit?");
    }

    // Static method to move an item
    private static void moveItem(Scanner pipe) {
        // Variable declaration
        int sourcePosition;
        int targetPosition;
        String itemToMove;

        // Condition to check if list is empty
        if (itemList.isEmpty()) {
            System.out.println("No items in the list.");
            return;
        }

        //  Condition to check if list only has one item
        if (itemList.size() == 1) {
            System.out.println("Only one item in the list. There is nothing to move.");
            return;
        }

        // Display the current list
        printNumberedList();

        // Get the source position
        sourcePosition = SafeInput.getRangedInt(pipe, "Enter the item number to move", 1, itemList.size());

        // Get the target position
        targetPosition = SafeInput.getRangedInt(pipe, "Enter the position to move it to (1 to " + itemList.size() + ")", 1, itemList.size());

        // If source and target are the same, do nothing
        if (sourcePosition == targetPosition) {
            System.out.println("Item is already at position " + sourcePosition + ".");
            return;
        }

        // Remove the item from the source position
        itemToMove = itemList.remove(sourcePosition - 1);

        // Insert the item at the target position
        itemList.add(targetPosition - 1, itemToMove);
        System.out.println("\nItem \"" + itemToMove + "\" moved from position " + sourcePosition + " to position " + targetPosition + ".");

        // Mark as needing to be saved
        needsSaved = true;
    }

    // Static method to open a file from disk - now using JFileChooser
    private static void openFile(Scanner pipe) throws IOException {
        // Check if current list needs to be saved first
        if (needsSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(pipe, "Current list has unsaved changes. Do you want to save first?");
            if (saveFirst) {
                saveFile(pipe);
            }
        }

        // Print information
        System.out.println("\n---- Open File ----");

        // Create a file chooser
        JFileChooser chooser = new JFileChooser();

        // Use the toolkit to get the current working directory
        File workingDirectory = new File(System.getProperty("user.dir"));

        // Set the current directory into the src folder within the project
        File srcDirectory = new File(workingDirectory.getPath() + File.separator + "src");
        chooser.setCurrentDirectory(srcDirectory);

        // Show open dialog and check return value
        int returnValue = chooser.showOpenDialog(null);

        // If user canceled the dialog, return without doing anything
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            System.out.println("File open operation canceled.");
            return;
        }

        // Ensure dialog title to Open
        chooser.setDialogTitle("Open");

        // Get selected file
        File file = chooser.getSelectedFile();
        String fileName = file.getName();

        // Check if file exists
        if (!file.exists()) {
            System.out.println("Error: File does not exist.");
            return;
        }

        // Clear the current list
        itemList.clear();

        try {
            // Scanner to read file
            Scanner in = new Scanner(file);
            int count = 0;

            // Read line by line
            while (in.hasNextLine()) {
                String line = in.nextLine();
                itemList.add(line);
                count++;
            }

            // Close scanner
            in.close();

            // Update tracking variables
            currentFileName = file.getAbsolutePath();
            needsSaved = false;

            // Print information
            System.out.println("\nFile loaded successfully: " + fileName);
            System.out.println(count + " items loaded.");

        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not find file '" + fileName + "'");
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static method to save the current list to disk - now using JFileChooser
    private static void saveFile(Scanner pipe) throws IOException {
        // Condition to check if the list is already empty
        if (itemList.isEmpty()) {
            System.out.println("Nothing to save. The list is empty.");
            return;
        }

        // Create a file chooser
        JFileChooser chooser = new JFileChooser();

        // Condition to check if there is a current file
        if (!currentFileName.isEmpty()) {
            File currentFile = new File(currentFileName);
            chooser.setCurrentDirectory(currentFile.getParentFile());
            chooser.setSelectedFile(currentFile);
        } else {
            // Use the toolkit to get the current working directory
            File workingDirectory = new File(System.getProperty("user.dir"));

            // Set the current directory into the src folder within the project
            File srcDirectory = new File(workingDirectory.getPath() + File.separator + "src");
            chooser.setCurrentDirectory(srcDirectory);
        }

        // Show save dialog and check return value
        int returnValue = chooser.showSaveDialog(null);

        // If user canceled the dialog, return without doing anything
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            System.out.println("File save operation canceled.");
            return;
        }

        // Ensure dialog title to Save
        chooser.setDialogTitle("Save");

        // Get selected file
        File file = chooser.getSelectedFile();
        String filePath = file.getAbsolutePath();

        if (!filePath.toLowerCase().endsWith(".txt")) {
            filePath += ".txt";
            file = new File(filePath);
        }

        try {
            // Create BufferedWriter
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // Write each item in the list to the file
            for (int i = 0; i < itemList.size(); i++) {
                writer.write(itemList.get(i));
                if (i < itemList.size() - 1) {
                    writer.newLine();
                }
            }

            // Close BufferedWriter
            writer.close();

            // Update tracking variables
            currentFileName = file.getAbsolutePath();
            needsSaved = false;

            // Print information
            System.out.println("\nFile saved successfully: " + file.getName());

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Static method to clear the current list
    private static void clearList(Scanner pipe) {
        // Condition to check if the list is already empty
        if (itemList.isEmpty()) {
            System.out.println("The list is already empty.");
            return;
        }

        // Condition to check if the list has unsaved changes
        if (needsSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(pipe, "Current list has unsaved changes. Do you want to save first?");
            if (saveFirst) {
                try {
                    saveFile(pipe);
                } catch (IOException e) {
                    System.out.println("Error saving file: " + e.getMessage());
                }
            }
        }

        // Confirm before clearing the list
        boolean confirmClear = SafeInput.getYNConfirm(pipe, "Are you sure you want to clear the list?");
        if (confirmClear) {
            itemList.clear();
            currentFileName = "";
            needsSaved = false;
            System.out.println("\nList cleared.");
        } else {
            System.out.println("\nList not cleared.");
        }
    }

    // Static method to view the list (replaced printList)
    private static void viewList() {
        // Variable declaration
        String item = "";

        // Print information
        System.out.println("\n-------- Current List --------");

        // Condition to check if list is empty
        if (itemList.isEmpty()) {
            System.out.println("No items in the list.");
        } else {
            for (int i = 0; i < itemList.size(); i++) {
                item = itemList.get(i);
                System.out.println((i + 1) + ". " + item);
            }
        }
        System.out.println("-------------------------------");
    }
}