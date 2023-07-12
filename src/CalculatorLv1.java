
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import static java.lang.Integer.parseInt;

@Getter
@Setter
@NoArgsConstructor

public class CalculatorLv1 {

    public static String getInputExpression() {
        String input;
        Scanner sc = new Scanner(System.in);
        System.out.print("Input expression: ");
        input = sc.nextLine();
        return input;
    }

    public static List<String> tokenizeExpression(String expression) {
        List<String> tokens = new ArrayList<>();

        //in lv1 we don't need currentToken, but we use to maintain consistency
        //we need it when we don't have whitespace btw number and operator
        StringBuilder currentToken = new StringBuilder();

        StringTokenizer tokenizer = new StringTokenizer(expression, " ");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if(isNumeric(token) || isOperator(token)){
                currentToken.append(token);
                tokens.add(currentToken.toString());
                currentToken.setLength(0);
            }else {
                System.out.println(token + " 유효하지 않은 입력입니다.");
                String reInput = getInputExpression();
                tokenizeExpression(reInput);
                return tokens;
            }
        }

        tokens.add(currentToken.toString());
        System.out.println(tokens);
        return tokens;
    }//function tokenizeExpression end

    public static int evaluateExpression(List<String> tokens) {
        List<Integer> operandList = new ArrayList<>();
        List<String> operatorList = new ArrayList<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                int operand = parseInt(token);
                operandList.add(operand);
            } else if(isOperator(token)){
                System.out.println(token);
                while(!operatorList.isEmpty() && hasPrecedence(operatorList.get(operatorList.size()-1), token)){
                compute(operandList, operatorList);
                }
                operatorList.add(token);
            }
        }

        while (!operatorList.isEmpty()) {
            if(operatorList.get(operatorList.size()-1).equals("*") ||operatorList.get(operatorList.size()-1).equals("/") ){
                compute(operandList, operatorList);
            }else{
                computeLTR(operandList, operatorList);
            }

        }

        if (operandList.size() == 1) {
            return operandList.get(0);
        } else {
            // Handle error: invalid expression
            return 0;
        }

    }//function evaluateExpression end

    private static void compute(List<Integer> operandList, List<String> operatorList) {
        if (operandList.size() >= 2 && operatorList.size() >= 1) {
            int operand2 = operandList.remove(operandList.size() - 1);
            int operand1 = operandList.remove(operandList.size() - 1);
            String operator = operatorList.remove(operatorList.size() - 1);

            int result;

                switch (operator) {
                    case "+" -> result = operand1 + operand2;
                    case "-" -> result = operand1 - operand2;
                    case "*" -> result = operand1 * operand2;
                    case "/" -> result = operand1 / operand2;
                    default -> {
                        // Handle error: invalid operator
                        return;
                    }
                }

            System.out.println("original compute called");
            System.out.println("operand1: " + operand1 + " operand2: " + operand2 + " operator: " + operator);
            System.out.println("result: " + result);
            operandList.add(result);
        } else {
            // Handle error: insufficient operands or operators
        }
    }//function compute end

    private static void computeLTR(List<Integer> operandList, List<String> operatorList) {
        if (operandList.size() >= 2 && operatorList.size() >= 1) {
            int operand1 = operandList.remove(0);
            int operand2 = operandList.remove(0);
            String operator = operatorList.remove(0);
            System.out.println("ltr operand1: " + operand1 + " operand2: " + operand2 + " operator: " + operator);

            int result;
                switch (operator) {
                    case "+" -> result = operand1 + operand2;  //shouldn't reach
                    case "-" -> result = operand1 - operand2;  //shouldn't reach
                    case "*" -> result = operand1 * operand2;
                    case "/" -> result = operand1 / operand2;
                    default -> {
                        // Handle error: invalid operator
                        return;
                    }
                }

            System.out.println("ltr result: " + result);
            operandList.add(0,result);
        } else {
            // Handle error: insufficient operands or operators
        }
    }//function computeLTR end

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+"); // Matches integers
    }

    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean hasPrecedence(String operator1, String operator2) {
        if ((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("+") || operator2.equals("-"))) {
            return true;
        } else if ((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("*") || operator2.equals("/"))) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[]args){

        CalculatorLv1 clv1 = new CalculatorLv1();
        String expression = getInputExpression();
        List<String> tokens =  tokenizeExpression(expression);
        int result =  evaluateExpression(tokens);
        System.out.println(result);

    }//main end
}//CalculatorLv1 END