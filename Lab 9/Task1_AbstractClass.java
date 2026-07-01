public class Task1_AbstractClass {

    abstract static class BaseClass {
        BaseClass() {
            System.out.println("BaseClass constructor called");
        }

        abstract void methodOne();
        abstract void methodTwo();
    }

    static class DerivedClass extends BaseClass {
        DerivedClass() {
            System.out.println("DerivedClass constructor called");
        }

        @Override
        void methodOne() {
            System.out.println("This is methodOne");
        }

        @Override
        void methodTwo() {
            System.out.println("This is methodTwo");
        }
    }

    public static void main(String[] args) {
        BaseClass baseRef = new DerivedClass(); // parent reference to child object
        baseRef.methodOne();
        baseRef.methodTwo();
    }
}
