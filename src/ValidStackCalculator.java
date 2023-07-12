import java.util.*;

public class ValidStackCalculator {
    public static int calculate(String expression) {
        List<String> postfix = convertToPostfix(expression);
        return evaluatePostfix(postfix);
    }

    private static List<String> convertToPostfix(String expression) {
        List<String> postfix = new ArrayList<>();
        Deque<String> operatorStack = new ArrayDeque<>();

        StringTokenizer tokenizer = new StringTokenizer(expression, "()+-*/", true);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();

            if (token.isEmpty()) {
                continue;
            }

            if (isNumeric(token)) {
                postfix.add(token);
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && !isLeftParenthesis(operatorStack.peek()) &&
                        hasPrecedence(operatorStack.peek(), token)) {
                    postfix.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (isLeftParenthesis(token)) {
                operatorStack.push(token);
            } else if (isRightParenthesis(token)) {
                while (!operatorStack.isEmpty() && !isLeftParenthesis(operatorStack.peek())) {
                    postfix.add(operatorStack.pop());
                }
                if (!operatorStack.isEmpty() && isLeftParenthesis(operatorStack.peek())) {
                    operatorStack.pop(); // Discard the matching left parenthesis
                } else {
                    // Mismatched parentheses
                    throw new IllegalArgumentException("Invalid expression: mismatched parentheses");
                }
            } else {
                // Invalid token
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            if (isParenthesis(operatorStack.peek())) {
                // Mismatched parentheses
                throw new IllegalArgumentException("Invalid expression: mismatched parentheses");
            }
            postfix.add(operatorStack.pop());
        }

        return postfix;
    }

    private static int evaluatePostfix(List<String> postfix) {
        Deque<Integer> operandStack = new ArrayDeque<>();

        for (String token : postfix) {
            if (isNumeric(token)) {
                operandStack.push(Integer.parseInt(token));
            } else if (isOperator(token)) {
                if (operandStack.size() < 2) {
                    // Insufficient operands
                    throw new IllegalArgumentException("Invalid expression: insufficient operands");
                }
                int operand2 = operandStack.pop();
                int operand1 = operandStack.pop();

                switch (token) {
                    case "+":
                        operandStack.push(operand1 + operand2);
                        break;
                    case "-":
                        operandStack.push(operand1 - operand2);
                        break;
                    case "*":
                        operandStack.push(operand1 * operand2);
                        break;
                    case "/":
                        if (operand2 == 0) {
                            // Division by zero
                            throw new ArithmeticException("Division by zero");
                        }
                        operandStack.push(operand1 / operand2);
                        break;
                }
            } else {
                // Invalid token
                throw new IllegalArgumentException("Invalid token: " + token);
            }
        }

        if (operandStack.size() != 1) {
            // Invalid expression
            throw new IllegalArgumentException("Invalid expression");
        }

        return operandStack.pop();
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+");
    }

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private static boolean isLeftParenthesis(String token) {
        return token.equals("(");
    }

    private static boolean isRightParenthesis(String token) {
        return token.equals(")");
    }

    private static boolean isParenthesis(String token) {
        return isLeftParenthesis(token) || isRightParenthesis(token);
    }

    private static boolean hasPrecedence(String operator1, String operator2) {
        int precedence1 = getPrecedence(operator1);
        int precedence2 = getPrecedence(operator2);

        if (precedence1 == precedence2) {
            // Left associativity
            return !isRightAssociative(operator1);
        }

        return precedence1 > precedence2;
    }

    private static int getPrecedence(String operator) {
        if (operator.equals("+") || operator.equals("-")) {
            return 1;
        } else if (operator.equals("*") || operator.equals("/")) {
            return 2;
        }
        return 0;
    }

    private static boolean isRightAssociative(String operator) {
        return operator.equals("^");
    }

    public static void main(String[] args) {
        String expression = "2 + 4 * 35 - 2 / 2 + ( 2 - 3 * 2 - 4 * 3) + 2 - 3";
        int result = calculate(expression);
        System.out.println("Result: " + result);
    }
}
