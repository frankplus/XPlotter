package formulaCalc;

//number in the mathematical expression. Variable 'x' is a number too
public class Number extends Token{
	private double num;
	public static double xValue=1;
	
	public double getNum() {
		if(getToken().equalsIgnoreCase("x"))
			return xValue;
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public Number(String n){
		super(n);
		if(n.equalsIgnoreCase("x"))
			num = -1;
		else
			num = Double.parseDouble(n);
	}
	public Number(double n){
		super(Double.toString(n));
		num=n;
	}
	public String toString(){
		return "number: "+num;
	}
}
