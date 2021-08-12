//EECS 40: ASSIGNMENT 2
//AYESHA PARVEZ
//STUDENT ID: 35998893

package eecs40;

import java.util.ArrayList;
import java.util.Stack;

public class ExprCalculator implements CalculatorInterface{
    private String expression = ""; //string input and output
    private String input = new String();
    private Stack<String> postfix = new Stack<>(); //stack to evaluate the postfix expression
    private Stack<String> solution = new Stack<>(); //stack to store intermediate and final result during computation
    private ArrayList<String> postfixExpression = new ArrayList<>();
    private ArrayList<String> convertedInput = new ArrayList<>();
    private int error = 2; //error = 2 => no error
    private char state = '&'; //holds last entered character, & means starting
    private char operator = '#'; //stores last entered operator;
    private int expressionSize = 0;
    private boolean unary = false; // check if a unary operator was found
    private boolean mod = false;
    private boolean negative = false; //check if the first character s -
    private String errorOutput = new String(""); //display error state
    int parenthesesCount = 0; // check if there is a mismatch in parentheses
    // helper function to check if a character lies in a given range
    private boolean isBetween(char value, char upperLimit, char lowerLimit){
        if((int)value <= (int)upperLimit && (int)value >= (int)lowerLimit){
            return true;
        }
        return false;
    }
    //eval
    private void eval() {
        // your code here
        expressionSize = expression.length();
        error = 2; // no error
        parenthesesCount = 0;
        unary = false;
        negative = false;
        errorOutput = "";
        state = '&';
        operator = '#';
        postfixExpression.clear();
        postfix.clear();
        solution.clear();
        convertedInput.clear();

        input = expression;
        for(int i = 0 ; i < expressionSize; i++){
            if(error != 2){ //break if there is an error
                break;
            }
            if(expression.charAt(i) == ')'){ //if char is )
                parenthesesCount -= 1;
                if(parenthesesCount < 0){ //parentheses mismatch
                    error = 3;
                }
                else if(state == '#'){
                    error = 3; // ) after C is an error
                }
                else if((state == operator && state != ')') || state == '.'){ //if ) follows an operator or .
                    error = 1;
                }
                else{
                    convertedInput.add(")");
                    operator = ')';
                }
            }
            else if(expression.charAt(i) == ' '){ //if the character is a space, skip
                continue;
            }
            else if(isBetween(expression.charAt(i),'9','0') ){ //if character is a number
              /*  if(state == '/' && expression.charAt(i) == '0'){ //div by 0
                    convertedInput.add(Character.toString(expression.charAt(i)));
                    error = 0; //NAN
                }*/
                if(state == ')'){ // if number follows )
                    convertedInput.add(Character.toString(expression.charAt(i)));
                    error = 1;
                }
                else if(isBetween(state, '9', '0') || state == '.'){ //if it's a continuation of a number
                    convertedInput.set(convertedInput.size()-1,convertedInput.get(convertedInput.size()-1) + expression.charAt(i));
                }
                else if(negative){
                    convertedInput.add("-" + Character.toString(expression.charAt(i)));
                    negative = false;
                }
                else{  // add number to operands array
                    convertedInput.add(Character.toString(expression.charAt(i)));
                }
            }
            else if(expression.charAt(i) == '.'){ //if character = .
                if(state == '&'){
                    convertedInput.add("0.");
                }
                else if(!isBetween(state,'9','0')){ //if previous character is not a number => error
                    error = 1;
                }
                else{
                    convertedInput.set(convertedInput.size()-1, convertedInput.get(convertedInput.size()-1) + expression.charAt(i));
                }
            }
            else if(expression.charAt(i) != ')' && (isBetween(expression.charAt(i), '/', '(') || expression.charAt(i) == '^')){ //if character is a binary symbolic operator
                if(expression.charAt(i) != '(' && !isBetween(state, '9','0') && state != ')' && state != '&'){
                    error = 1;
                }
                else if(state == '&' && expression.charAt(i) == '-'){
                    negative = true;
                }
                else{
                    convertedInput.add(Character.toString(expression.charAt(i)));
                    operator = expression.charAt(i);
                    if(expression.charAt(i) == '('){
                        parenthesesCount += 1;
                    }
                }
            }
            else if(expressionSize - 1 - i >= 3 && expression.substring(i,i+3).equals("mod")){ //if input is mod
                if(state == operator || state == '.' || (isBetween(state,'/','%') && expression.charAt(i) != '(')){ //if previous character is an operator or .
                    error = 1;
                }
                else{
                    convertedInput.add("%");
                    operator = '%';
                    mod = true;
                }

            }
            //if input is a unary operator
            else if(expressionSize - 1 - i >= 3 && (expression.substring(i, i+3).equals("tan") || expression.substring(i,i+3).equals("cos") || expression.substring(i,i+3).equals("sin") || expression.substring(i,i+3).equals("log") || expression.substring(i,i+3).equals("fac"))){
                if(state == '.' || state == ')' || isBetween(state,'9','0')){ //if unary operator follows a number or . or )
                    error = 1;
                }
                else{
                    convertedInput.add(Character.toString(expression.charAt(i)));
                    unary = true;
                    operator = expression.charAt(i); //operator identification is by the first letter
                }
            }
            else if(expressionSize - 1 - i >= 2 && expression.substring(i,i+2).equals("ln")){ //if input is ln
                if(state == '.' || state == ')'|| isBetween(state,'9','0')){ //if unary operator follows a number or . or )
                    error = 1;
                }
                else {
                    convertedInput.add("n");
                    unary = true;
                    operator = 'n';
                }
            }
            else if(expressionSize - 1 - i >= 4 && expression.substring(i,i+4).equals("sqrt")){ //if input is sqrt
                if(state == '.' || state == ')' || isBetween(state,'9','0')){ //if unary operator follows a number or . or )
                    error = 1;
                }
                else{
                    convertedInput.add("q");
                    unary = true;
                    operator = 'q';
                }
            }
            if(unary == true || mod == true || expression.charAt(i) == operator){ //(operator == '%' && state != '%')
                state = operator;
                unary = false;
                mod = false;
            }
            else{
                state = expression.charAt(i);
            }
            checkError();
        }
        if(error == 2 && parenthesesCount != 0){
            error = 3;
            errorOutput = "Error: Parentheses";
        }
        else if(error == 2){
            convertToPostfix();
            evaluatePostfixExpression();
        }
        if(error != 2){
            checkError();
        }
        else{
            expression = solution.pop();
            if(Double.parseDouble(expression) % 1 == 0){
                Double d = new Double(Double.parseDouble(expression));
                expression = String.valueOf(d.intValue());
            }
        }
    }
    // evaluate the expression in postfix format
    private void evaluatePostfixExpression() {
        for(int i=0; i < postfixExpression.size(); i++){
            if(postfixExpression.get(i).equals("*") || postfixExpression.get(i).equals("+") || postfixExpression.get(i).equals("/") || postfixExpression.get(i).equals("%") || postfixExpression.get(i).equals("-" )|| postfixExpression.get(i).equals("^")){
                String op2 = solution.pop();
                String op1 = solution.pop();
                if(postfixExpression.get(i).equals("/") && Double.parseDouble(op2) == 0){ //divide by 0 error
                    error = 0;
                    break;
                }
                else{
                    solution.push(computeResult(postfixExpression.get(i).charAt(0), op1, op2));
                }
            }
            else if(isBetween(postfixExpression.get(i).charAt(0), 'z','a')){ //unary operator
                String ans = computeResult(postfixExpression.get(i),solution.pop());
                if(ans != null){
                    solution.push(ans);
                }
            }
            else{ //number
                solution.push(postfixExpression.get(i));
            }
            if(error != 2){
                break;
            }
        }
    }
    //infix to postfix conversion
    private void convertToPostfix() {
        for(int i=0; i < convertedInput.size(); i++){
            if(convertedInput.get(i).equals( "(")){
                postfix.push("(");
            }
            else if(isBetween(convertedInput.get(i).charAt(0), 'z', 'a')){ //if it is a unary operator or function
                postfix.push(convertedInput.get(i));
            }
            else if(convertedInput.get(i).equals("+") || convertedInput.get(i).equals("-")){ //if operator is + or -
                if(!postfix.isEmpty() && !isBetween(postfix.peek().charAt(0), 'z','a') && !postfix.peek().equals( "(" )){ // pop higher precedence operator first
                    postfixExpression.add(postfix.pop());
                }
                postfix.push(convertedInput.get(i));
            }
            else if(convertedInput.get(i).equals("*") || convertedInput.get(i).equals("/") || convertedInput.get(i).equals("%")){ // if operator is */%
                if(!postfix.isEmpty() && (postfix.peek().equals( "^") || postfix.peek().equals("*") || postfix.peek().equals("/") || postfix.peek().equals("%"))){
                    postfixExpression.add(postfix.pop());
                }
                postfix.push(convertedInput.get(i));
            }
            else if(convertedInput.get(i).equals("^")){ //if operator is ^
                if(!postfix.isEmpty() && postfix.peek().equals("^")){
                    postfixExpression.add(postfix.pop());
                }
                postfix.add("^");
            }
            else if(convertedInput.get(i).equals(")")){
                while(!postfix.isEmpty() && !postfix.peek().equals("(")){ //pop everything until the last (
                    postfixExpression.add(postfix.pop());
                }
                if(!postfix.isEmpty()){
                    postfix.pop(); //pop the (
                }
                if(!postfix.isEmpty() && isBetween(postfix.peek().charAt(0), 'z', 'a')){
                    postfixExpression.add(postfix.pop());
                }
            }
            else{ //if it is a number
                postfixExpression.add(convertedInput.get(i));
            }
        }
        if(!postfix.isEmpty()){
            while(!postfix.isEmpty()){ //add all the remaining operators to the postfix expression
                postfixExpression.add(postfix.pop());
            }
        }
    }

    private int computeFactorial(String operand) {
        int fact = 1;
        int intOperand;
        try {
            intOperand = Integer.parseInt(operand);
            if(intOperand == 0){
                return 0;
            }
            for(int i = 1; i <= intOperand; i++){
                fact *= i;
            }
        }
        catch (NumberFormatException e){
            fact = -1;
        }
        return fact;
    }
    //unary operations
    private String computeResult(String operator, String op){
        double operand = Double.parseDouble(op);
        double answer = 0.0;
        switch (operator){
            case "c" : answer = Math.cos(operand); break;
            case "s" : answer = Math.sin(operand); break;
            case "t" : answer = Math.tan(operand); break;
            case "n"  : if(operand > 0){
                            answer = Math.log(operand);
                        }
                        else{
                            error = 1;
                        }
                        break;
            case "l" : if(operand > 0){
                            answer = Math.log10(operand);
                        }
                        else{
                            error = 1;
                        }
                        break;
            case "q": if(operand >= 0){
                            answer =  Math.sqrt(operand);
                        }
                        else{
                            error = 1;
                        }
                        break;
            case "f" :  int ans = computeFactorial(op);
                        if(ans != -1){
                            answer = ans;
                        }
                        else{
                            error = 1;
                        }
                        break;
            default : error = 1;
        };
        if(error != 1){
            return String.valueOf(answer);
        }
        return null; //if there is an error

    }
    //binary operations
    private String computeResult(char operator, String op1, String op2){
        double operand1 = Double.parseDouble(op1);
        double operand2 = Double.parseDouble(op2);
        double answer = switch (operator) {
            case '+' -> operand1 + operand2;
            case '-' -> operand1 - operand2;
            case '*' -> operand1 * operand2;
            case '/' -> operand1 / operand2;
            case '%' -> operand1 % operand2;
            case '^' -> Math.pow(operand1,operand2);
            default -> 0.0;
        };
        return String.valueOf(answer);
    }
    /* method to check error state. 'error' is an error code for different types of errors. */
    public void checkError(){
        if (error == 0){
            errorOutput = "NaN";
        }
        else if (error == 3){
            errorOutput = "Error: Parentheses";
        }
        else if(error == 1){
            errorOutput = "Error";
        }
    }
    @Override
    public void acceptInput(String s) {
        if (s.equals("=")) {
            eval();
        } else if (s.equals("Backspace")) {
            expression = expression.substring(0, expression.length() - 1);
        } else if (s.equals("C")) {
            expression = ""; // clear!
        } else { // accumulate input String
            expression = expression + s;
        }

    }

    @Override
    public String getDisplayString() {
        if(error != 2){
            error = 2;
            return errorOutput;
        }
        return expression;
    }
}
