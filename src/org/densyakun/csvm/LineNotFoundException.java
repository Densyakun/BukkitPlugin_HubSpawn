package org.densyakun.csvm;
public class LineNotFoundException extends Throwable{
	private static final long serialVersionUID=-68296175430166153L;
	public LineNotFoundException(int l){
		super("Line:"+l);
	}
}