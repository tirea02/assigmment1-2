//calculator lv2   single Parenthesis
//2 + ( 3 - 2 * 4 - 4 / 4 + 2 ) * 2 - 1 test case
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

import static java.lang.Integer.parseInt;

@Getter
@Setter
@NoArgsConstructor

public class CalculatorLv2 {

    public static String getInputExpression() {
        String input;
        Scanner sc = new Scanner(System.in);
        System.out.print("Input expression: ");
        input = sc.nextLine();
        return input;
    }

    public static List<String> tokenizeExpression(String expression) {
        List<String> tokens = new ArrayList<>();

        //in lv2 we don't need currentToken, but we use to maintain consistency
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
        List<Integer> operandListInParenthesis = new ArrayList<>();
        List<String> operatorListInParenthesis = new ArrayList<>();

        boolean hasparenthesisStart = false;

        Stack<String> parenthesesStack  = new Stack<>();
        String lastElement = "";

        for (String token : tokens) {
            System.out.println("start of loop now token is : " + token);

            if(!operatorList.isEmpty()){
                 lastElement = operatorList.get(operatorList.size()-1);
            }

            if (isNumeric(token)) {
                int operand = parseInt(token);
                if(hasparenthesisStart){
                    operandListInParenthesis.add(operand);
                }else{
                    operandList.add(operand);
                }
            }else if(token.equals("(")){
                System.out.println("( met");
                parenthesesStack.push("(");
                hasparenthesisStart = true;
            }else if(token.equals( ")" )){
                System.out.println(") met");
                System.out.println(operandListInParenthesis);
                System.out.println(operatorListInParenthesis);
                System.out.println(hasPrecedence(operatorListInParenthesis.get(operatorListInParenthesis.size()-1), token));

                //check if las operator was * or /
                if(hasPrecedence(operatorListInParenthesis.get(operatorListInParenthesis.size()-1), token)){
                    compute(operandListInParenthesis, operatorListInParenthesis);
                }
                System.out.println("in () only +,- left");
                while(!operatorListInParenthesis.isEmpty()){
                    computeLTR(operandListInParenthesis, operatorListInParenthesis);
                }
                int resultOfParenthesis  = operandListInParenthesis.get(0);
                operandListInParenthesis.clear();
                operandList.add(resultOfParenthesis);
                hasparenthesisStart = false;
                parenthesesStack.pop();
            }else if(isOperator(token)){
                System.out.println(token);
                if(hasparenthesisStart){
                    while(!operatorListInParenthesis.isEmpty()
                            && hasPrecedence(operatorListInParenthesis.get(operatorListInParenthesis.size()-1), token) ){
                        System.out.println("tt : " + operandListInParenthesis);
                        System.out.println("token : " + token);
                            compute(operandListInParenthesis, operatorListInParenthesis);
                    }
                    operatorListInParenthesis.add(token);
                }else {
                    while (!operatorList.isEmpty() && hasPrecedence(operatorList.get(operatorList.size() - 1), token)) {
                        compute(operandList, operatorList);
                    }
                    operatorList.add(token);
                }
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
    }



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
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+"); // Matches integers
    }

    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("(") || token.equals((")"));
    }

    private static boolean hasPrecedence(String operator1, String operator2) {
        if ((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("+") || operator2.equals("-"))) {
            return true;
        } else if ((operator1.equals("*") || operator1.equals("/")) && (operator2.equals("*") || operator2.equals("/"))) {
            return true;
        } else if ((operator1.equals("*") || operator1.equals("/")) && (operator2.equals(")"))) {
            return true;
        }else {
            return false;
        }
    }


    public static void main(String[]args){

        CalculatorLv2 clv2 = new CalculatorLv2();
        String expression = clv2.getInputExpression();
        List<String> tokens = tokenizeExpression(expression);
        int result =  evaluateExpression(tokens);
        System.out.println(result);
    }//main end
}//CalculatorLv2 END