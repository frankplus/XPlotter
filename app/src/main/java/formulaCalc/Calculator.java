package formulaCalc;

import java.lang.reflect.Method;
import java.util.ArrayList;

//this class evaluate the formula, replacing the variable 'x' each time
public class Calculator {
	private PostfixAlgorithm calc;

    public void init(String formula){
        FormulaTokenizer tokenizer = new FormulaTokenizer();
        try{
            ToRPN convertor = new ToRPN(tokenizer.tokenize(formula));
            calc = new PostfixAlgorithm(convertor.conversion());
        }catch(Exception e){
            System.out.println("Invalid expression: "+formula);
            System.out.println("error! "+e.getMessage());
            throw new RuntimeException("Invalid expression: "+formula);
        }
    }
	
	public Calculator(String formula){
		init(formula);
	}
	
	public double calculation(double xValue){
		Number.xValue = xValue;
		try{
			return calc.getResult();
		}catch(RuntimeException e){
			System.out.println("error on performing calculation!");
			System.out.println("error! "+e.getMessage());
			throw new RuntimeException("error on performing calculation, check the expression.");
		}
	}

    public static String[] getFunctionsName() {
        ArrayList<String> methodsName = new ArrayList<String>();

        Method[] methods = Math.class.getMethods();
        for (Method method : methods) {
            Class<?>[] parametersType = method.getParameterTypes();
            int numParameters = parametersType.length;

            boolean parametersAreDouble = true;
            for (Class<?> paramType : parametersType) {
                if (paramType != double.class) {
                    parametersAreDouble = false;
                    break;
                }
            }
            if (!parametersAreDouble) continue;
            if(numParameters!=1) continue; //change if add performing multiple parameters feature

            methodsName.add(method.getName());
        }

        String[] result = new String[methodsName.size()];
        return methodsName.toArray(result);
    }

	/*public static void main(String[] args) {
		String testExpression = "x";
		Calculator c = new Calculator(testExpression);
		System.out.println(testExpression+" = "+c.calculation(6));
	}*/
}
