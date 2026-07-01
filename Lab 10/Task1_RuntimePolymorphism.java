public class Task1_RuntimePolymorphism {

    static class ShapeCalculator {
        double result;

        public void calculateCircle(float radius) {
            this.result = Math.PI * radius * radius;
            System.out.println("Area of Circle: " + result);
        }

        // final method - cannot be overridden
        final void calculateSquare(double sideLength) {
            this.result = sideLength * sideLength;
            System.out.println("Area of Square: " + result);
        }
    }

    static class AdvancedCalculator extends ShapeCalculator {
        @Override
        public void calculateCircle(float radius) {
            this.result = 2 * Math.PI * radius * radius; // different formula to show overriding
            System.out.println("Advanced Area of Circle: " + result);
        }

        // Cannot override calculateSquare() because it is declared final in parent
    }

    public static void main(String[] args) {
        ShapeCalculator shapeObj = new ShapeCalculator();
        shapeObj.calculateCircle(4.0f);
        shapeObj.calculateSquare(5.0);

        AdvancedCalculator advancedObj = new AdvancedCalculator();
        advancedObj.calculateCircle(4.0f);

        ShapeCalculator referenceObj = new AdvancedCalculator();
        referenceObj.calculateCircle(4.0f); // dynamic method dispatch -> calls overridden version
    }
}
