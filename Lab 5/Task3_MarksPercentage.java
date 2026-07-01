import java.util.Scanner;

public class Task3_MarksPercentage {

    static class ResultCalculator {
        private double maxMarks;
        private double obtainedMarks;

        public void setMaxMarks(double maxMarks) {
            this.maxMarks = maxMarks;
        }

        public void setObtainedMarks(double obtainedMarks) {
            this.obtainedMarks = obtainedMarks;
        }

        public void computeAndPrint() {
            double percentage = (obtainedMarks / maxMarks) * 100;
            System.out.println("Percentage: " + percentage + "%");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Total Marks: ");
        double totalMarks = sc.nextDouble();
        System.out.print("Enter Obtained Marks: ");
        double obtainedMarks = sc.nextDouble();

        ResultCalculator calc = new ResultCalculator();
        calc.setMaxMarks(totalMarks);
        calc.setObtainedMarks(obtainedMarks);
        calc.computeAndPrint();

        sc.close();
    }
}
