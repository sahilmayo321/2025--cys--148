public class Task4_Record {

    static class Record {
        private String name;
        private int rollNo;

        public Record(String name, int rollNo) {
            this.name = name;
            this.rollNo = rollNo;
        }

        public void displayRecord() {
            System.out.println("Name = " + name);
            System.out.println("Roll No = " + rollNo);
        }

        public void setStudentName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        Record r1 = new Record("Umair", 163);
        r1.setStudentName("Sherry");
        r1.displayRecord();
    }
}
