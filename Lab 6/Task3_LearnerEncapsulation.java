public class Task3_LearnerEncapsulation {

    static class Learner {
        private String fullName;
        private int years;
        private char batch;

        public void setFullName(String fullName) { this.fullName = fullName; }
        public void setYears(int years) { this.years = years; }
        public int getYears() { return years; }
        public void setBatch(char batch) { this.batch = batch; }
        public char getBatch() { return batch; }

        @Override
        public String toString() {
            return "Learner(fullName=" + fullName + ", years=" + years + ", batch=" + batch + ")";
        }
    }

    public static void main(String[] args) {
        Learner l1 = new Learner();
        l1.setFullName("Umair");
        l1.setYears(10);
        l1.setBatch('A');

        System.out.println("Student Age: " + l1.getYears());
        System.out.println("Student Batch: " + l1.getBatch());
        System.out.println(l1);
    }
}
