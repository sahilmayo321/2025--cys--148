public class Task2_Upcasting {

    static class Parent {
        void showParent() {
            System.out.println("Parent method");
        }
    }

    static class Child extends Parent {
        void showChild() {
            System.out.println("Child method");
        }
    }

    public static void main(String[] args) {
        Parent parentRef = new Child(); // upcasting
        Child childRef = new Child();

        parentRef.showParent();
        // parentRef.showChild(); // NOT allowed: parent reference cannot access child-only members

        childRef.showParent();
        childRef.showChild();

        System.out.println("A parent class reference can only access members declared in the parent class,");
        System.out.println("even when it refers to a child class object.");
    }
}
