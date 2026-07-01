public class Task1_Interface {

    interface A {
        void name();
        void age();
    }

    static class B implements A {
        public void name() {
            System.out.println("Umair");
        }

        public void age() {
            System.out.println("18");
        }
    }

    public static void main(String[] args) {
        A a = new B(); // interface reference to object of implementing class
        a.name();
        a.age();
    }
}
