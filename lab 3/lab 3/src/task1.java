class Point {

    private double x;
    private double y;
    private double z;

    public Point() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setPoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void display() {
        System.out.println("(" + x + ", " + y + ", " + z + ")");
    }

    public double distance(Point p) {

        double dx = x - p.x;
        double dy = y - p.y;
        double dz = z - p.z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}

public class task1 {
    public static void main(String[] args) {

        Point p1 = new Point(2, 3, 4);
        Point p2 = new Point(5, 6, 7);

        p1.display();
        p2.display();

        System.out.println("Distance = " + p1.distance(p2));
    }
}