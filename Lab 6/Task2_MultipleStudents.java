public class Task2_MultipleStudents {

    static class Learner {
        private int rollNumber;
        private String name;

        public void setRollNumber(int rollNumber) { this.rollNumber = rollNumber; }
        public int getRollNumber() { return rollNumber; }
        public void setName(String name) { this.name = name; }
        public String getName() { return name; }
    }

    public static void main(String[] args) {
        Learner l1 = new Learner();
        Learner l2 = new Learner();

        l1.setRollNumber(162);
        l1.setName("Theon");

        l2.setRollNumber(111);
        l2.setName("Tendulkar");

        System.out.println("Name: " + l1.getName() + " Roll No: " + l1.getRollNumber());
        System.out.println("Name: " + l2.getName() + " Roll No: " + l2.getRollNumber());
    }
}
