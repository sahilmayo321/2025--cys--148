import java.util.*;

class NumberGuessingGame {

    private int secretNumber;
    private int guessesLeft;
    private int warnings;
    private ArrayList<Integer> guessedNumbers;

    // Constructor
    public NumberGuessingGame() {
        Random rand = new Random();
        secretNumber = rand.nextInt(1001); // 0 - 1000
        guessesLeft = 10;
        warnings = 1;
        guessedNumbers = new ArrayList<>();
    }

    // Start game
    public void play() {

        Scanner input = new Scanner(System.in);

        System.out.println("Welcome to Number Guessing Game!");
        System.out.println("I am thinking of a number between 0 and 1000.");
        System.out.println("You have " + warnings + " warning.");
        System.out.println("----------------------------------");

        while (guessesLeft > 0) {

            System.out.println("\nGuesses left: " + guessesLeft);

            // print guessed numbers
            System.out.print("Guessed numbers: ");
            for (int i = 0; i < guessedNumbers.size(); i++) {
                System.out.print(guessedNumbers.get(i) + " ");
            }
            System.out.println();

            System.out.print("Enter your guess: ");

            // invalid input check
            if (!input.hasNextInt()) {
                input.next();
                handleWarning();
                continue;
            }

            int guess = input.nextInt();

            // range check
            if (guess < 0 || guess > 1000) {
                System.out.println("Please enter a number between 0 and 1000.");
                handleWarning();
                continue;
            }

            // repeated guess
            if (guessedNumbers.contains(guess)) {
                System.out.println("You already guessed this number!");
                handleWarning();
                continue;
            }

            guessedNumbers.add(guess);

            // correct guess
            if (guess == secretNumber) {
                System.out.println("Hurray! You guessed the correct number 🎉");
                return;
            }

            // hint
            if (guess < secretNumber) {
                System.out.println("Your guess is too small.");
            } else {
                System.out.println("Your guess is too big.");
            }

            guessesLeft--;
            System.out.println("----------------------------------");
        }

        System.out.println("\nGame Over! You ran out of guesses.");
        System.out.println("The correct number was: " + secretNumber);
    }

    // warning handler
    private void handleWarning() {
        if (warnings > 0) {
            warnings--;
            System.out.println("Invalid input! Warnings left: " + warnings);
        } else {
            guessesLeft--;
            System.out.println("No warnings left! 1 guess deducted.");
        }
    }
}