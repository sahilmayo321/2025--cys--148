import java.util.*;

class HangmanGame {

    public void startGame() {

        String[] words = {"java", "code", "game", "laptop"};
        Random r = new Random();
        String word = words[r.nextInt(words.length)];

        char[] guess = new char[word.length()];
        Arrays.fill(guess, '_');

        int chances = 6;
        Scanner sc = new Scanner(System.in);

        while (chances > 0) {

            System.out.println("\nWord: " + String.valueOf(guess));
            System.out.println("Chances left: " + chances);

            System.out.print("Enter letter: ");
            char ch = sc.next().charAt(0);

            boolean check = false;

            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == ch) {
                    guess[i] = ch;
                    check = true;
                }
            }

            if (check == true) {
                System.out.println("Right");
            } else {
                chances--;
                System.out.println("Wrong");
            }

            if (String.valueOf(guess).equals(word)) {
                System.out.println("You win! Word: " + word);
                break;
            }
        }

        if (!String.valueOf(guess).equals(word)) {
            System.out.println("You lose! Word was: " + word);
        }

        sc.close();
    }
}

public class hangmangsame {
    public static void main(String[] args) {

        HangmanGame obj = new HangmanGame();
        obj.startGame();
    }
}
