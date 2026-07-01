public class Task3_MultipleInheritanceInterfaces {

    interface A2 {
        void name();
        void age();
    }

    interface C2 {
        void section();
    }

    static class B2 implements A2, C2 {
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
        B2 b = new B2();
        b.name();
        b.age();
        b.section();
    }
}
