import java.nio.file.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            String text = Files.readString(Paths.get("input.txt"));
            text = text.replaceAll("[^a-zA-Z0-9\\s]", "");
            text = text.replaceAll("\\s+", " ");
            text = text.toLowerCase();
            System.out.println(text);
        } catch (IOException e) {
            System.out.println("Eroare la citirea fișierului: " + e.getMessage());
        }
    }
}
