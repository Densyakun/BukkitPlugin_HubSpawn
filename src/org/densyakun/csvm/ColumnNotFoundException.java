package org.densyakun.csvm;
public class ColumnNotFoundException extends Throwable{
	private static final long serialVersionUID=4159600689386640315L;
	public ColumnNotFoundException(int l,int c){
		super("Line:"+l+" Column:"+c);
	}
}