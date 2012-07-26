
package src.com.Exception;

/**
 * <p>
 * This is the base exception class for DirectTV code.
 * <p>
 * 
 * @author Astadia.
 * @version $Id: ResilientException.java
 */
public class ResilientException extends Exception{
	/**
	 * <p>
	 * Thrown when the cause of this exception is not another exception.
	 * </p>
	 * 
	 * @param sourceClass
	 *            The class which threw the exception.
	 * @param message
	 *            The reason for the exception.
	 */
	public ResilientException(String msg){ 
	      super(msg); 
	    } 
	
	/**
	 * <p>
	 * Thrown when the cause of this exception is another exception.
	 * </p>
	 * 
	 * @param sourceClass
	 *            The class which threw the exception.
	 * @param message
	 *            The reason for the exception.
	 * @param cause
	 *            The causing exception.
	 */
	    public ResilientException(String msg, Throwable t){ 
	      super(msg,t); 
	    } 
}
