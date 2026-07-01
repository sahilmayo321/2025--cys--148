public class Task1_ConstructorOverloading {

    static class Measurements {
        float length, width, height;
        float perimeter, squareArea, boxVolume;

        // Constructor 1: square
        Measurements(float side) {
            this.length = side;
            squareArea = this.length * this.length;
            System.out.println("Square area = " + squareArea);
        }

        // Constructor 2: rectangle - chains to 3-arg constructor via this()
        Measurements(float length, float width) {
            this(length, width, 1); // constructor chaining using this()
            this.perimeter = 2 * (this.length + this.width);
            System.out.println("Rectangle perimeter = " + perimeter);
        }

        // Constructor 3: box
        Measurements(float length, float width, float height) {
            this.length = length;
            this.width = width;
            this.height = height;
            boxVolume = this.height * (this.length * this.width);
            System.out.println("Box volume = " + boxVolume);
        }

        // Copy constructor
        Measurements(Measurements obj) {
            this.length = obj.length;
            this.width = obj.width;
            this.height = obj.height;
            System.out.println("Copy constructor invoked");
        }
    }

    static class Box extends Measurements {
        Box() {
            super(2, 3); // calls parent constructor (rectangle version, which chains to box version)
            System.out.println("Box created via inheritance.");
        }
    }

    public static void main(String[] args) {
        Box b = new Box();
    }
}
