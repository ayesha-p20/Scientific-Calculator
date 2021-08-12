package eecs40;

import java.util.ArrayList;

public class Calculator implements CalculatorInterface{
    String output = new String("0"); //variable to hold the string to be displayed
    String number = new String("0"); //variable to combine individual characters to obtain each number. ex number = "1" + "2" = "12"
    char operator = '#'; //variable to store the last input arithmetic operator
    char state = '&'; //variable to store previous input
    int error = 2; //variable to determine error type. error = 2 means no error, error = 1 => Error, error = 0 => NaN
    boolean negative = false; //checks if a negative number has been entered after C
    ArrayList<String> input = new ArrayList<>(); //each element is either an operand or operator (+,-)
    @Override
    public void acceptInput(String s) {
        // your assignment #1 code
        /* when the user hits clear */
        if(s.equals("C")){
            output = "0";
            number = "0";
            if(!input.isEmpty()){
                input.clear();
            }
            state = 'C';
            error = 2;
            negative = false;
        }
        /* if the calculator is in state of error */
        else if(error == 1 || error == 0){
            // do nothing until the user hits 'C' to exit error state

        }
        /* if the current character is . */
        else if(s.charAt(0) == '.'){
            if(state != '.' && state != '+' && state != '-' && state != '*' && state != '/'){
                output += ".";
                number += ".";
            }
            else if (state != '.'){
                error = 1;
            }

        }
        /* if user inputs an operator */
        else if((int)s.charAt(0) >= 42 && (int)s.charAt(0) <= 47 && s.charAt(0) != '.' && (int)s.charAt(0) != 44 ){
            if(state == '.' || state == operator){
                error = 1;
            }
            else if(state == '&' && s.charAt(0) == '-'){
                negative = true;
            }
            else if(state == '='){
                input.add(output);
                input.add(s);
                state = operator;
            }
            else{
                //check if previous operator was * or /
                input.add(number);
                number = "0";
                if(operator == '*' || operator == '/'){
                    //compute multiplication/division and enter result into arraylist
                    String result = computeResult(operator, input.get(input.size()-2), input.get(input.size()-1));
                    //ex operator = * , s = - and input = [11,+,5,2] ---> [11,+,10,-]
                    input.set(input.size()-2,result);
                    input.remove(input.size()-1);
                }
                operator = s.charAt(0); //update operator
                if(operator == '+' || operator == '-'){
                    input.add(s); //adding the operator to the input array list
                }
            }
            operator = s.charAt(0);
        }
        /* if user enters = */
        else if(s.charAt(0) == '='){
            if(operator == '/' && state == '0'){
                error = 0;
            }
            else if(state == '.' || state == operator){
                error = 1;
            }
            else if(state == '=' || state == '&'){ //if user hits '=' multiple times or after pressing 'C'
                state = '=';
            }
            else{
                //compute result
                input.add(number);
                number = "0";
                String result = new String(output);
                if(operator == '*' || operator == '/'){
                    result = computeResult(operator, input.get(input.size()-2), input.get(input.size()-1));
                    input.set(input.size()-2,result);
                    input.remove(input.size()-1);
                }
                if(input.size() > 2){
                    for(int i=1; i < input.size(); i+=2){
                        result = computeResult(input.get(i).charAt(0),input.get(i-1),input.get(i+1)); // ex. input = [2,+,3], i-1 = 2, i = +, i+1 = 3
                        input.set(i+1,result);
                    }
                }
                output = result;
            }
            input.clear(); //clear array list
        }
        /* if user enters a number */
        else if ((int)s.charAt(0) >= 48 && (int)s.charAt(0) <= 57 ){
            if(state == '='){ //start a new calculation if user enters number after =
                input.clear();
                number = s;
                output = s;
                operator = '#';
                state = '&';
            }
            else if(s.charAt(0) != '0' && negative){
                output = "-" + s;
                number = output;
                negative = false;
            }
            else{
                if(number.equals("0")){  //number and output are 0 at the same time
                    number = s;
                    output = s;
                }
                else{
                    number += s;
                    output += s;
                }
            }
        }
        /* if the input is invalid */
        else{
            error = 1;
        }
        state = s.charAt(0); //updating state to store current character
        checkError(); //checking if an error has been encountered

    }

    @Override
    public String getDisplayString() {
        // your assignment #1 code
        if(output.equals("NaN") || output.equals("Error") || output.endsWith(".")){
            return output;
        }
        /* if output is an integer, it is returned as an integer */
        else if(Double.parseDouble(output) % 1 == 0){
            Double d = new Double(Double.parseDouble(output));
            return String.valueOf(d.intValue());
        }
        return output;
    }
    /* method to compute result */
    public String computeResult(char operator, String op1, String op2){
        double operand1 = Double.parseDouble(op1);
        double operand2 = Double.parseDouble(op2);
        double answer = switch (operator) {
            case '+' -> operand1 + operand2;
            case '-' -> operand1 - operand2;
            case '*' -> operand1 * operand2;
            case '/' -> operand1 / operand2;
            default -> 0.0;
        };
        return String.valueOf(answer);
    }
    /* method to check error state. 'error' is an error code for different types of errors. */
    public void checkError(){
        if (error == 0){
            output = "NaN";
        }
        else if(error == 1){
            output = "Error";
        }
    }
}
