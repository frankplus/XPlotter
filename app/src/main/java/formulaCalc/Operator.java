package formulaCalc;

import java.lang.reflect.InvocationTargetException;

public class Operator extends Token{
    private String oper;
    private static final String[] kKeywords = Calculator.getFunctionsName();

    public static final int LEFT_ASSOCIATIVITY = 1;
    public static final int RIGHT_ASSOCIATIVITY = 2;

    public Operator(String op){
        super(op);
        oper = op;
    }

    public String toString(){
        return "Operator: "+oper;
    }

    public int getPrecedence(){
        for(int i=0; i<kKeywords.length; i++){
            if(oper.equals(kKeywords[i])){
                return 5;
            }
        }
        switch(oper){
            case "+":
            case "-":
                return 2;
            case "*":
            case "/":
                return 3;
            case "^":
                return 4;
            default:
                throw new RuntimeException("getPrecedence on an invalid operator: "+oper);
        }
    }

    public int getAssociativity(){
        for(int i=0; i<kKeywords.length; i++){
            if(oper.equals(kKeywords[i]))
                return LEFT_ASSOCIATIVITY;
        }
        switch(oper){
            case "+":
            case "-":
            case "*":
            case "/":
                return LEFT_ASSOCIATIVITY;
            case "^":
                return RIGHT_ASSOCIATIVITY;
            default:
                throw new RuntimeException("getAssociativity on an invalid operator: "+oper);
        }
    }

    public static boolean isOperator(String op){
        for(int i=0; i<kKeywords.length; i++){
            if(op.equals(kKeywords[i]))
                return true;
        }
        return
            op.equals("+")||
            op.equals("-")||
            op.equals("/")||
            op.equals("*")||
            op.equals("^");
    }
    public int nArgument(){
        for(int i=0; i<kKeywords.length; i++){
            if(oper.equals(kKeywords[i]))
                return 1;
        }
        switch(oper){
            case "+":
            case "-":
            case "*":
            case "/":
            case "^":
                return 2;
            default:
                throw new RuntimeException("error invalid operator!");
        }
    }

    //calculation function for binary operators
    public double calculate(double a, double b){
        double ret;
        switch(oper){
            case "+":
                ret = a+b;
                break;
            case "-":
                ret = a-b;
                break;
            case "*":
                ret = a*b;
                break;
            case "/":
                ret = a/b;
                break;
            case "^":
                ret = Math.pow(a,b);
                break;
            default:
                throw new RuntimeException("error invalid operator 1: "+oper);
        }
        return ret;
    }

    //calculation function for unary operators
    public double calculate(double a){
        double ret = 0;
        for(int i=0; i<kKeywords.length; i++){
            if(oper.equals(kKeywords[i])){
                java.lang.reflect.Method method;
                try {
                    method = Math.class.getMethod(oper, double.class);
                    ret = (double) method.invoke(null,a);
                } catch (SecurityException | NoSuchMethodException e) {
                    throw new RuntimeException("error invalid operator 2: "+oper);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    return 0;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return 0;
                } catch (IllegalArgumentException e) {
                    return 0;
                }
            }
        }
        return ret;
    }
}