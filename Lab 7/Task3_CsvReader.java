import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Task3_CsvReader {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("Book1.csv");
        Scanner sc = new Scanner(file);

        if (sc.hasNextLine()) {
            sc.nextLine(); // skip header row
        }

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.trim().isEmpty()) continue;

            String[] details = line.split(",");
            String name = details[0];
            String id = details[1];
            Double score = Double.valueOf(details[2]);

            System.out.println("Name: " + name + ", ID: " + id + ", Score: " + score);
        }

        sc.close();
    }
}
