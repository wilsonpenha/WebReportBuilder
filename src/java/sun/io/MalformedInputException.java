package sun.io;

public class MalformedInputException extends java.io.CharConversionException {
	private static final long serialVersionUID = 2585413228493157652L;

	/**
	 * Constructs a MalformedInputException with no detail message. A detail message
	 * is a String that describes this particular exception.
	 */
	public MalformedInputException() {
		super();
	}

	/**
	 * Constructs a MalformedInputException with the specified detail message. A
	 * detail message is a String that describes this particular exception.
	 * 
	 * @param s
	 *            the String containing a detail message
	 */
	public MalformedInputException(String s) {
		super(s);
	}
}