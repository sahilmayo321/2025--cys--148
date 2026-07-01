import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Task2_FileWrite {
    public static void main(String[] args) {
        File file = new File("firstfile.txt");

        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }

        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write("HEY how are you");
            System.out.println("Data written successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
