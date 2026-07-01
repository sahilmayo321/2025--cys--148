public class Task1_LearnerInfo {

    static class LearnerInfo {
        private int age;
        private char group;

        public void setAge(int age) { this.age = age; }
        public int getAge() { return age; }
        public void setGroup(char group) { this.group = group; }
        public char getGroup() { return group; }

        @Override
        public String toString() {
            return "LearnerInfo(age=" + age + ", group=" + group + ")";
        }
    }

    public static void main(String[] args) {
        LearnerInfo l1 = new LearnerInfo();
        l1.setAge(10);
        l1.setGroup('A');

        System.out.println("Age = " + l1.getAge());
        System.out.println("Group = " + l1.getGroup());
        System.out.println(l1);
    }
}
