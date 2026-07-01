public class Task4_InterfaceAndAbstractClass {

    interface A3 {
        void name();
        void age();
    }

    abstract static class C3 {
        abstract void section();
    }

    static class B3 extends C3 implements A3 {
        public void name() {
            System.out.println("Umair");
        }

        public void age() {
            System.out.println("18");
        }

        public void section() {
            System.out.println("Section: C");
        }
    }

    public static void main(String[] args) {
        B3 b = new B3();
        b.name();
        b.age();
        b.section();
    }
}
