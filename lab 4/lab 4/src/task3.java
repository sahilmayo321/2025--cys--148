import java.util.Random;
import java.util.Scanner;

class Person {

    int x = 0;
    int y = 0;

    void move(char direction) {

        switch (direction) {

            case '^':
                y++;
                break;

            case 'v':
                y--;
                break;

            case '>':
                x++;
                break;

            case '<':
                x--;
                break;
        }
    }

    void display(String name) {
        System.out.println(name + " is at = (" + x + " : " + y + ")");
    }
}

public class task3{

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Random rand = new Random();

        Person ali = new Person();
        Person hasan = new Person();

        // Hurdle Position
        int hurdleX = 1;
        int hurdleY = 2;

        System.out.println("Starting:");
        ali.display("Ali");
        hasan.display("Hasan");

        for (int i = 0; i < 5; i++) {

            // Ali Movement
            System.out.print("\nAli: Where should I go now (^,v,<,>) : ");
            char dir = input.next().charAt(0);

            ali.move(dir);

            ali.display("Ali");

            // Hasan Random Movement
            char[] moves = {'^', 'v', '<', '>'};

            char randomMove = moves[rand.nextInt(4)];

            int oldX = hasan.x;
            int oldY = hasan.y;

            hasan.move(randomMove);

            // Check hurdle
            if (hasan.x == hurdleX && hasan.y == hurdleY) {

                System.out.println("Hasan encounters hurdle at (1 : 2)");

                hasan.x = oldX;
                hasan.y = oldY;
            }

            hasan.display("Hasan");
        }
    }
}