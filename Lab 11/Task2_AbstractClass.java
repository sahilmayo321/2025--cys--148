public class Task2_AbstractClass {

    abstract static class A1 {
        abstract void name();
        abstract void age();

        void section() {
            System.out.println("Section: C");
        }
    }

    static class B1 extends A1 {
        public void name() {
            System.out.println("Umair");
        }

        public void age() {
            System.out.println("18");
        }
    }

    public static void main(String[] args) {
        A1 a = new B1(); // abstract class reference to concrete object
        a.name();
        a.age();
        a.section();
    }
}
