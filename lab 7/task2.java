import java.util.Scanner;

class SavingAccount {

    double balance;

    SavingAccount(double balance) {
        this.balance = balance;
    }

    void deposit(double amount) {
        balance += amount;
        System.out.println("Amount Deposited Successfully!");
    }

    void withdraw(double amount) {

        if (amount > balance) {
            System.out.println("Insufficient Balance!");
        } else {
            balance -= amount;
            System.out.println("Amount Withdrawn Successfully!");
        }
    }

    void checkBalance() {
        System.out.println("Current Balance = " + balance);
    }
}

public class task2 {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        SavingAccount acc = new SavingAccount(1000);

        int choice;

        do {

            System.out.println("\n===== BANK MENU =====");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");

            System.out.print("Enter Choice: ");
            choice = input.nextInt();

            switch (choice) {

                case 1:
                    System.out.print("Enter Deposit Amount: ");
                    double dep = input.nextDouble();
                    acc.deposit(dep);
                    break;

                case 2:
                    System.out.print("Enter Withdraw Amount: ");
                    double with = input.nextDouble();
                    acc.withdraw(with);
                    break;

                case 3:
                    acc.checkBalance();
                    break;

                case 4:
                    System.out.println("Program Ended.");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 4);
    }
}