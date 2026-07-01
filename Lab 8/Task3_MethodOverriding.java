public class Task3_MethodOverriding {

    static class Parent {
        public void display(int num) {
            System.out.println("Parent display: " + num);
        }

        public void display(int num1, int num2) {
            System.out.println("Parent display (overloaded): " + (num1 * num2));
        }
    }

    static class Child extends Parent {
        @Override
        public void display(int num) {
            System.out.println("Child display (overridden): " + num);
        }
    }

    public static void main(String[] args) {
        Parent obj1 = new Parent();
        Child obj2 = new Child();
        Parent obj3 = new Child(); // parent reference pointing to child object

        obj1.display(5);
        obj2.display(5);
        obj3.display(5); // dynamic method dispatch -> calls Child's overridden version

        obj1.display(2, 3); // overloaded method, only accessible via Parent type
    }
}
