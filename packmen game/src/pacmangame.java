import java.util.*;

public class pacmangame{


    private Maze maze;
    private Player player;
    private Enemy enemy;
    private int score = 0;
    private int lives = 3;
    private boolean running = true;
    private Scanner input = new Scanner(System.in);

    public pacmangame() {
        maze = new Maze();
        player = new Player(1, 1);
        enemy = new Enemy(7, 20);
    }

    public void start() {
        while (running) {
            clearScreen();
            display();
            handleInput();
            updateGame();
            sleep();
        }
        endGame();
    }

    private void display() {
        maze.printMaze(player, enemy);
        System.out.println("\nScore: " + score + " | Lives: " + lives);
        System.out.print("Move (W/A/S/D): ");
    }

    private void handleInput() {
        String in = input.next();
        if (in.length() == 0) return;
        char move = Character.toLowerCase(in.charAt(0));
        movePlayer(move);
    }

    private void movePlayer(char move) {
        int newX = player.getX();
        int newY = player.getY();

        if (move == 'w') newX--;
        else if (move == 's') newX++;
        else if (move == 'a') newY--;
        else if (move == 'd') newY++;
        else return;

        if (maze.isWalkable(newX, newY)) {
            player.setPosition(newX, newY);
        }
    }

    private void updateGame() {
        checkFood();
        enemy.moveRandom(maze);
        checkCollision();
        checkGameOver();
    }

    private void checkFood() {
        if (maze.getCell(player.getX(), player.getY()) == '.') {
            score++;
            maze.clearCell(player.getX(), player.getY());
        }
    }

    private void checkCollision() {
        if (player.getX() == enemy.getX() &&
                player.getY() == enemy.getY()) {

            lives--;
            System.out.println("\nEnemy hit!");
            player.setPosition(1, 1);
        }
    }

    private void checkGameOver() {
        if (lives <= 0) running = false;
    }

    private void endGame() {
        System.out.println("\n===== GAME OVER =====");
        System.out.println("Final Score: " + score);
    }

    private void clearScreen() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (Exception e) {}
    }

    public static void main(String[] args) {
        pacmangame game = new pacmangame();
        game.start();
    }
}

class Maze {

    private char[][] grid = {
            "############################".toCharArray(),
            "#............##............#".toCharArray(),
            "#.######.###.##.###.######.#".toCharArray(),
            "#..........................#".toCharArray(),
            "#.######.##.####.##.######.#".toCharArray(),
            "#..........#....#..........#".toCharArray(),
            "#.######.###.##.###.######.#".toCharArray(),
            "#..........................#".toCharArray(),
            "############################".toCharArray()
    };

    public void printMaze(Player p, Enemy e) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {

                if (i == p.getX() && j == p.getY())
                    System.out.print('P');
                else if (i == e.getX() && j == e.getY())
                    System.out.print('G');
                else
                    System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    public boolean isWalkable(int x, int y) {
        if (x < 0 || y < 0 || x >= grid.length || y >= grid[0].length)
            return false;
        return grid[x][y] != '#';
    }

    public char getCell(int x, int y) {
        return grid[x][y];
    }

    public void clearCell(int x, int y) {
        grid[x][y] = ' ';
    }
}

class Player {
    private int x, y;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Enemy {
    private int x, y;
    private Random rand = new Random();

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveRandom(Maze maze) {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};

        int attempts = 0;

        while (attempts < 10) {
            int dir = rand.nextInt(4);

            int newX = x + dx[dir];
            int newY = y + dy[dir];

            if (maze.isWalkable(newX, newY)) {
                x = newX;
                y = newY;
                break;
            }
            attempts++;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}

class Food {
}