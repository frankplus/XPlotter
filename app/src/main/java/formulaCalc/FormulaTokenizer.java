package formulaCalc;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.LinkedList;

public class FormulaTokenizer {
	private LinkedList<Token> formula; //the mathematical expression
	
	public FormulaTokenizer(){
		formula = new LinkedList<Token>();
	}
	
	//this divide the formula in tokens using StreamTokenizer
	public LinkedList<Token> tokenize(String str) throws IOException{
		StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(str));
		tokenizer.ordinaryChar('-');
		tokenizer.ordinaryChar('/');
		tokenizer.ordinaryChar('x');
		tokenizer.ordinaryChar('X');
		while(tokenizer.nextToken() != StreamTokenizer.TT_EOF){
			if(tokenizer.ttype==StreamTokenizer.TT_NUMBER)
				formula.add(new Number(tokenizer.nval));
			else if(tokenizer.ttype==StreamTokenizer.TT_WORD)
				formula.add(new Operator(tokenizer.sval));
			else{
				String tok = String.valueOf((char)tokenizer.ttype);
				if(tok.equals("(") || tok.equals(")"))
					formula.add(new Token(tok));
				else if(Operator.isOperator(tok))
					formula.add(new Operator(tok));
				else if(tok.equalsIgnoreCase("x")) //if it is the variable 'x' treat it as a number
					formula.add(new Number("x"));
				else 
					throw new RuntimeException("invalid token 1: "+tok);
			}
		}
		
		return formula;
	}
	
	//test method
	/*public void pullTokens(){
		while(!formula.isEmpty())
			System.out.println(formula.poll());
	}*/
}
