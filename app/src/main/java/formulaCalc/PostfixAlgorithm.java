package formulaCalc;

import java.util.Stack;
import java.util.LinkedList;
import java.util.ListIterator;

//search Reverse Polish Notation on wiki
public class PostfixAlgorithm {
	private LinkedList<Token> expression; //the mathematical expression
	public PostfixAlgorithm(LinkedList<Token> f){
		expression = f;
	}
	
	public double getResult(){ //perform the calculation of the mathematical expression
		Stack<Number> outputStack = new Stack<Number>();
		Token tok;
		
		//initiate iterator
		ListIterator<Token> iterator = expression.listIterator();
		
		//While there are input tokens left 
		while(iterator.hasNext()){
			tok = iterator.next();
			if(tok instanceof Number)
				outputStack.push((Number)tok);
			else if(tok instanceof Operator){
				Operator op = (Operator) tok;
				int nArgs = op.nArgument();
				Number res = null;
				if(nArgs==1){
					if(outputStack.isEmpty())
						throw new RuntimeException("The user has not input sufficient values in the expression.");
					Number n1 = outputStack.pop();
					res = new Number(op.calculate(n1.getNum()));
				}
				else if(nArgs==2){
					if(outputStack.isEmpty())
						throw new RuntimeException("The user has not input sufficient values in the expression.");
					Number n1 = outputStack.pop();
					
					if(outputStack.isEmpty())
						throw new RuntimeException("The user has not input sufficient values in the expression.");
					Number n2 = outputStack.pop();
					res = new Number(op.calculate(n2.getNum(),n1.getNum()));
				}
				outputStack.push(res);
			}
		}
		
		//If there is only one value in the stack 
		if(!outputStack.isEmpty()){
			Number res = outputStack.pop();
			if(outputStack.isEmpty())
				return res.getNum();
			else 
				throw new RuntimeException("The user input has too many values.");
		}
		return -1;
	}
}
