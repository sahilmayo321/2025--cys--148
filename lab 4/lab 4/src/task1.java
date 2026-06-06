class JTime {
    int hour;
    int minute;
    int second;

    // Default Constructor
    JTime() {
        hour = 0;
        minute = 0;
        second = 0;
    }

    // Parameterized Constructor
    JTime(int h, int m, int s) {
        hour = h;
        minute = m;
        second = s;
    }

    // Constructor using total seconds after 12 AM
    JTime(int totalSeconds) {
        hour = totalSeconds / 3600;
        totalSeconds %= 3600;

        minute = totalSeconds / 60;
        second = totalSeconds % 60;
    }

    // Convert current time to total seconds
    int toSeconds() {
        return (hour * 3600) + (minute * 60) + second;
    }

    // Find elapsed seconds between two timestamps
    int elapsedSeconds(JTime t) {
        return Math.abs(this.toSeconds() - t.toSeconds());
    }

    // Find elapsed time in timestamp format
    JTime elapsedTime(JTime t) {
        int diff = Math.abs(this.toSeconds() - t.toSeconds());
        return new JTime(diff);
    }

    // Display Time
    void display() {
        System.out.println(hour + ":" + minute + ":" + second);
    }
}

public class task1 {
    public static void main(String[] args) {

        JTime t1 = new JTime(10, 20, 30);
        JTime t2 = new JTime(12, 45, 50);

        System.out.print("Time 1 = ");
        t1.display();

        System.out.print("Time 2 = ");
        t2.display();

        int seconds = t1.elapsedSeconds(t2);
        System.out.println("Elapsed Seconds = " + seconds);

        JTime elapsed = t1.elapsedTime(t2);

        System.out.print("Elapsed Time = ");
        elapsed.display();

        JTime t3 = new JTime(5000);

        System.out.print("Time from total seconds = ");
        t3.display();
    }
}