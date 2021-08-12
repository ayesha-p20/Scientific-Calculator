package eecs40;

public class CalculatorTestDrive_2 {
    public static void main(String[] args){
        CalculatorInterface calc = new Calculator();

        calc.acceptInput("1");

        System.out.println(calc.getDisplayString()); // show (457+992)/2+12*cos(0)

        calc.acceptInput("+");

        System.out.println("Display: " + calc.getDisplayString()); // show 736.5

        calc.acceptInput("5");

        System.out.println("Display: " + calc.getDisplayString()); // show 736.5+(876-8*9

        calc.acceptInput("=");

        System.out.println("Display: " + calc.getDisplayString()); // show Error:Parentheses












    }
}
