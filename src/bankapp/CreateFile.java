package bankapp;

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

public class CreateFile {
    public static void toFile(String tekst) {
        try {
            File myObj = new File("JDBC_errors.txt");
            FileWriter myWriter = new FileWriter("JDBC_errors.txt");
            myWriter.write(tekst);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }
}