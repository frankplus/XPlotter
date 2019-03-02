package formulaCalc;
import java.util.*;

//this class converts the mathematical expression to Reverse 
//Polish Notation using the Shunting Yard Algorithm
public class ToRPN{
	
	private LinkedList<Token> expression; //the mathematical expression
	
	public ToRPN(LinkedList<Token> f){
		expression = f;
	}
	
	//this perform the Shunting Yard Algorithm and return the stack in postFix notation
	public LinkedList<Token> conversion(){
		LinkedList<Token> output = new LinkedList<Token>();
		Stack<Token> operatorStack = new Stack<Token>();
		
		Token tok;
		while(!expression.isEmpty()){ //While there are tokens to be read
			tok = expression.poll(); //Read a token
			if(tok instanceof Number) //If the token is a number, then add it to the output queue.
				output.add(tok);
			else if(tok instanceof Operator){ //If the token is an operator
				Operator o1 = (Operator) tok;
				int o1Pre = o1.getPrecedence();
				int o1Ass = o1.getAssociativity();
				
				/*while there is an operator token, o2, at the top of the stack, and
				either o1 is left-associative and its precedence is *less than or equal* to that of o2,
				or o1 if right associative, and has precedence *less than* that of o2,
				then pop o2 off the stack, onto the output queue;*/
				Operator o2;
				Token tok2;
				while(!operatorStack.isEmpty()){
					tok2 = operatorStack.peek();
					if(!(tok2 instanceof Operator))
						break;
					o2 = (Operator)tok2;
					int o2Pre = o2.getPrecedence();
					if(o1Ass==Operator.LEFT_ASSOCIATIVITY && o1Pre<=o2Pre ||
						o1Ass==Operator.RIGHT_ASSOCIATIVITY && o1Pre<o2Pre){
						operatorStack.pop();
						output.add(o2);
					}
					else 
						break;
				}
				operatorStack.push(o1);
			}
			else if(tok.getToken().equals("(")){//If the token is a left parenthesis
				operatorStack.push(tok); //then push it onto the stack
				//System.out.println("right parenthesis pushed onto the operator stack");
			}
			else if(tok.getToken().equals(")")){ //If the token is a right parenthesis
				//Until the token at the top of the stack is a left parenthesis
				while(!operatorStack.isEmpty() && !operatorStack.peek().getToken().equals("("))
					output.add(operatorStack.pop());
				if(!operatorStack.isEmpty() && operatorStack.peek().getToken().equals("("))
					operatorStack.pop();
				else
					throw new RuntimeException("Error, mismatched parenthesis: 1");
			}
			else{
				throw new RuntimeException("invalid token 2: "+tok.getToken());
			}
		}
		
		while(!operatorStack.isEmpty()){ //While there are still operator tokens in the stack
			tok = operatorStack.pop(); 
			
			//If the operator token on the top of the stack is a parenthesis 
			if(tok.getToken()=="("||tok.getToken()==")")
				//then there are mismatched parentheses.
				throw new RuntimeException("Error, mismatched parenthesis: 2");
			output.add(tok);//Pop the operator onto the output queue.
		}
		
		return output;
	}
	
	//testing function
	/*public void testToRPN() throws Exception{
		Deque<Token> outputStack = conversion();
		while(!outputStack.isEmpty())
			System.out.println(outputStack.pop());
	}*/
}
