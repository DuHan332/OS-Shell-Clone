package exception;

/**
 * This class contains a custom exception to be thrown in each command.
 * 
 * @author Dezhi Ren
 *
 */
public class SystemErrorException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = -6072150732031417879L;

  /**
   * The constructor of SystemErrorException
   */
  public SystemErrorException() {
    super();
  }

  /**
   * The constructor of SystemErrorException with a message
   * 
   * @param message the error message
   */
  public SystemErrorException(String message) {
    super(message);
  }

}
