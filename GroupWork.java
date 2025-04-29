import java.util.Scanner;

public class GroupWork {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nCOLLABORATIVE CALCULATOR");
        System.out.print("Enter first number: ");
        int num1 = scanner.nextInt();
        System.out.print("Enter second number: ");
        int num2 = scanner.nextInt();
        System.out.println("Product: " + Calculator.multiple(num1, num2)); // Group 3's
        // method
        System.out.println("subtract: " + Calculator.subtract(num1, num2)); // Group
        // 2's method
        System.out.println("Minimum: " + Calculator.min(num1, num2)); // Group 9's
        // method
        // System.out.println("Remainder: " + Calculator.mod(num1, num2)); // Group 5's
        // method
        // System.out.println("Sum: " + Calculator.multiple(num1, num2)); // Group 2's
        // method
        // System.out.println("subtract: " + Calculator.add(num1, num2)); // Group 2's
        // method
        System.out.println("Division result: " + Calculator.divide(num1, num2));
        // System.out.println("Are numbers equal? " + Calculator.isEqual(num1, num2));
        // // Group 10's method
        scanner.close();
    }
}
