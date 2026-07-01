public class Task2_StudentData {

    static class StudentData {
        private String name;
        private int rollNumber;
        private float score;

        public StudentData(String name, int rollNumber, float score) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.score = score;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        StudentData student = new StudentData("Umair", 12, 14.0f);
        System.out.println("Student Name: " + student.getName());
    }
}
