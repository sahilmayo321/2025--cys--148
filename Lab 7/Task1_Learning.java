public class Task1_Learning {

    static class Learning {
        private String name;
        private int id;

        public Learning(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public void displayInfo() {
            System.out.println("Name = " + name);
            System.out.println("Roll Number = " + id);
        }
    }

    public static void main(String[] args) {
        Learning l1 = new Learning("Umair", 163);
        l1.displayInfo();
    }
}
