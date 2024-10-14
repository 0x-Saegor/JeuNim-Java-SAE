public class Test {

    public void testBinaryString() {
        for (int i = 0; i <= 10; i++) {
            System.out.println(binaryString(i));
        }
    }

    // Assuming this is the method to be tested
    public String binaryString(int number) {
        return Integer.toBinaryString(number);
    }

    public static void main(String[] args) {
        Test tester = new Test();
        tester.testBinaryString();
    }
}
