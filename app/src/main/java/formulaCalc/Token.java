package formulaCalc;

public class Token {
	private String str;
	public Token(String str){
		this.str = str;
	}
	public String toString(){
		return str;
	}
	public String getToken() {
		return str;
	}
}
