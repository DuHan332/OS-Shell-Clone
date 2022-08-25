package command;

import driver.Tracker;
import exception.SystemErrorException;

/**
 * This Class would read the input from user and execute the proper command. If a string which which
 * is surrounded by quotation is provided only then it would print the string out, if it's followed
 * by ">" and a file it would overwrite the file, if it is followed by ">>", it would append the
 * file with the string.
 * 
 * @author Du Han
 *
 */
public class Echo implements Commands {
  /**
   * This function would return the manual of this command.
   * 
   * @return a String that describes how to use this command.
   */
  public String getManual() {
    String Manual = "echo STRING :\n print STRING on theshell.";
    return Manual;
  }

  /**
   * This function would check if input is valid and then return the String that user input
   * 
   * @param input the input from user
   * @param track a Tracker that track the root directory and current directory
   * @return a String that contains the result of this function
   */
  public String execute(String[] newinput, Tracker track) throws SystemErrorException {
    if (newinput.length == 0) {
      throw new SystemErrorException("must have at least one parameter");
    }
    if (newinput.length == 1) {
      if (isSurrounded(newinput[0])) {
        return newinput[0].substring(1, newinput[0].length() - 1);
      }
      throw new SystemErrorException("the input is invalid for echo");
    }
    throw new SystemErrorException("too many parameters");
  }

  /**
   * this is a helper function that check if the string is surrounded by quotations
   * 
   * @param input the input from user/
   * @return true of false
   */
  private boolean isSurrounded(String input) {
    String first = input.substring(0, 1);
    String last = input.substring(input.length() - 1, input.length());
    if (first.equals("\"") && last.equals("\"")) {
      return true;
    }
    return false;
  }
}
