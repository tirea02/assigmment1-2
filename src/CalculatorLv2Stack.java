import java.util.EmptyStackException;
import java.util.Scanner;
//this is copy code from internet
//invalid code
// 1 + 2 * 3 - ( 2 - 3 * 9 - 3 / 3 ) - 2 * 4 didn't pass it
public class CalculatorLv2Stack {
    MyStack mySt;
    String[] postfix;
    boolean isError;

    public CalculatorLv2Stack(int arrLen){
        mySt = new MyStack();
        postfix = new String[arrLen];
        isError = false;
    }
    public int getPriority(String ch){
        //연산자의 우선순위를 반환해주는 함수
        //+, -는 1, *, /는 2를 반환.
        int answer=0;
        if(ch.equals("+") || ch.equals("-"))
            answer=1;
        if(ch.equals("*") || ch.equals("/"))
            answer=2;

        return answer;
    }

    public void setPostfixLength(){
        //변환한 postfix가 저장된 배열의 길이를 경우에 따라 조정하는 함수
        int idx=0;

        while(idx != postfix.length && postfix[idx] != null)
            idx++;

        if(postfix.length != idx){
            String[] newPostfix = new String[idx];
            System.arraycopy(postfix, 0, newPostfix, 0, idx);
            postfix = newPostfix;
        }

    }

    public void printPostfix(){
        //후위표기법으로 변환한 결과를 출력하는 함수
        int idx=0;
        System.out.print("후위표기법 변환결과: ");
        for(int i=0;i<postfix.length;i++)
            System.out.print(postfix[i]+" ");
        System.out.println();
    }

    public void printWrongExp(){
        postfix[0] = "잘못된 수식입니다.";
        isError = true;
        for(int j=1;j<postfix.length;j++)
            postfix[j]=null;
        setPostfixLength();
    }

    public boolean isOperator(String exp){
        //입력받은 exp 문자열이 연산자(+, -, *, /)라면 true 리턴
        if(exp.equals("+") || exp.equals("-") ||
                exp.equals("*") || exp.equals("/")){
            return true;
        }
        else
            return false;
    }

    public double simpleCalc(double n1, double n2, String operator){
        //입력받은 n1, n2를 operator에 맞게 계산해주는 함수
        double result = 0.0;

        if(operator.equals("+"))
            result = n1 + n2;
        else if(operator.equals("-"))
            result = n1 - n2;
        else if(operator.equals("*"))
            result = n1 * n2;
        else
            result = n1 / n2;

        return result;
    }

    public void infix2Postfix(String[] infix) {
        int idx=0;
        if(isOperator(infix[0]))    //수식의 첫 글자가 연산자인 경우
            printWrongExp();        //에러 출력

        for(int i=0;i<infix.length;i++){
            if(Character.isDigit(infix[i].charAt(0))){  //숫자인 경우, postfix 배열에 추가
                if(i!=0 && Character.isDigit(infix[i-1].charAt(0))){
                    printWrongExp();
                    return;
                }
                postfix[idx++] = infix[i];
            }
            else if(isOperator(infix[i])){  //연산자인 경우,
                if(i==0 || isOperator(infix[i-1])){ //연산자를 중복하여 입력한 경우
                    printWrongExp();        //에러 출력
                    return;
                }
                else if(mySt.isStrStackEmpty())   //연산자 스택이 비어있는 경우,
                    mySt.push(infix[i]);  //스택에 push
                else{
                    if(getPriority(mySt.strPeek()) < getPriority(infix[i])){
                        mySt.push(infix[i]);
                        //현재 스택의 top이 위치한 연산자와 입력받은 연산자의 우선순위를 비교
                        //입력받은 연산자의 우선순위가 더 크다면 그냥 스택에 push
                    }
                    else{
                        postfix[idx++] = mySt.strPop();
                        mySt.push(infix[i]);
                        //그렇지 않다면, 스택에 있는 연산자를 pop하여 postfix 배열에 넣고
                        //입력받은 연산자를 push
                    }
                }
            }
            else if(infix[i].equals("(")) {
                mySt.push(infix[i]);
            }   //여는 괄호인 경우, 연산자 스택에 push

            else if(infix[i].equals(")")){
                //닫는 괄호인 경우,
                try {
                    while (!mySt.strPeek().equals("(")) {
                        postfix[idx++] = mySt.strPop();
                    } //여는 괄호가 나올때까지, pop하여 postfix 배열에 넣음.
                    mySt.strPop(); //스택에 있는 여는 괄호도 pop함.
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    printWrongExp();
                    return;
                    //만약, 여는 괄호 개수보다 닫는 괄호가 많다면 에러 출력
                }
            }
            else{
                printWrongExp();
                return;
                //Operand, Operator 간의 띄어쓰기를 제대로 안하거나
                //잘못된 입력을 했을 경우, 에러 출력
            }
        }
        while(!mySt.isStrStackEmpty()){
            postfix[idx++] = mySt.strPop();
        }   //스택에 남아있는 연산자를 postfix 배열에 추가

        setPostfixLength(); //변환된 후위표기식에 맞는 길이로 배열 크기 변환
        for(int i=0;i<postfix.length;i++){
            if(postfix[i].equals("(")){
                printWrongExp();
                return;
            }
        }
        //만약, 변환 완료된 후위표기식에 "("가 있다면 에러 출력
    }

    public void calculatePostfix(){
        int result;
        for(int i=0;i<postfix.length;i++){
            if(Character.isDigit(postfix[i].charAt(0))){
                mySt.push(Double.parseDouble(postfix[i]));
            }   //숫자라면 number stack에 push
            else if(isOperator(postfix[i])){
                double num2 = mySt.intPop();
                double num1 = mySt.intPop();
                double temp_result=simpleCalc(num1, num2, postfix[i]);
                mySt.push(temp_result);
            }   //만약 연산자라면 number stack에서 2번 pop해서
            //해당 연산자와 계산하고 그 결과값을 push
        }
        System.out.println("계산결과: "+String.format("%.3f", mySt.intPeek()));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("========= 계산기 =========");
        System.out.println("*주의*, 수식은 각각 띄어쓰기 해야합니다.");
        System.out.println("예시: 1 * ( 2 + 3 )");
        System.out.println("=========================");

        System.out.print("수식을 입력하세요: ");
        String exp = scanner.nextLine();
        String[] infix = exp.split(" ");

        CalculatorLv2Stack MyCal = new CalculatorLv2Stack(infix.length);
        MyCal.infix2Postfix(infix);
        MyCal.printPostfix();
        if(!MyCal.isError)
            MyCal.calculatePostfix();
    }
}

class MyStack {
    int num_top, str_top;
    String[] str_data;
    double[] num_data;

    public MyStack(){
        this.num_top = 0;
        this.str_top = 0;
        str_data = new String[30];
        num_data = new double[30];
    }

    public void push(String data){
        this.str_data[str_top++] = data;
    }

    public void push(double data){
        this.num_data[num_top++] = data;
    }

    public String strPop(){
        if(str_top == 0)
            throw new EmptyStackException();
        String answer = str_data[--str_top];
        return answer;
    }

    public double intPop(){
        if(num_top == 0)
            throw new EmptyStackException();
        double answer = num_data[--num_top];
        return answer;
    }

    public String strPeek(){
        return this.str_data[str_top-1];
    }

    public double intPeek(){
        return this.num_data[num_top-1];
    }

    public boolean isStrStackEmpty(){
        if(str_top == 0)
            return true;
        else
            return false;
    }

    public boolean isIntStackEmpty(){
        if(num_top == 0)
            return true;
        else
            return false;
    }
}