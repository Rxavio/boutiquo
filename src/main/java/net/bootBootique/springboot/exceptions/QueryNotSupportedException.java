package net.bootBootique.springboot.exceptions;

//public class QueryNotSupportedException extends RuntimeException {
//
//    public QueryNotSupportedException(String message) {
//        super(message);
//    }
//
//}

public class QueryNotSupportedException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueryNotSupportedException(String message) {
        super(message);
    }

}