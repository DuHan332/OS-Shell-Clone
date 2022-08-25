package command;

import java.util.ArrayList;
import driver.Tracker;
import nodetype.*;
import helper.FindPath;
import exception.*;

/**
 * This class is implemented from interface Commands and it would change the current directory to
 * the target directory.
 * 
 * @author Dezhi Ren
 *
 */

public class ChangeDirectory implements Commands {


  /**
   * The function would first check input and throw error if the input is invalid. Then the function
   * would recognize the input and call the corresponding method that changes the current working
   * directory to the target directory.
   * 
   * @param parameter the input from user interpreted by driver.InputInterpreter
   * @param tracker a tracker that store the root directory and current directory
   * @return an empty string if the path is changed successfully
   * @exception SystemErrorException this exception with an error message is thrown if the input is
   *            invalid
   */
  public String execute(String[] parameter, Tracker tracker) throws SystemErrorException {
    if (parameter.length == 0)
      throw new SystemErrorException("There is no parameter.");
    else if (parameter.length > 1)
      throw new SystemErrorException("There is more than one parameter.");
    if (parameter[0].equals(".")) {
      return "";
    } else if (parameter[0].equals("..")) {
      if (tracker.getCurrentDirectory().getName().equals("/"))
        throw new SystemErrorException("The current directory is root!");
      tracker.setCurrentDirectory(tracker.getCurrentDirectory().getRoot());
      return "";
    } else if (parameter[0].equals("/")) {
      tracker.setCurrentDirectory(tracker.getRootDirectory());
      return "";
    }
    String result;
    String[] route = parameter[0].split("/");
    ArrayList<String> routeList = new ArrayList<String>();
    for (String routeName : route) {
      routeList.add(routeName);
    }
    if (route[0].equals("")) {
      routeList.remove(0);
      result = changeDirectoryForWholeRoute(tracker, routeList, parameter[0]);
    } else {
      result = changeDirectoryForRelativeRoute(tracker, routeList, parameter[0]);
    }
    return result;
  }

  /**
   * The function would change the current directory to target directory based on the give whole
   * path. The function would throw exception if the path is not exist or the target is not a
   * directory.
   * 
   * @param tracker a tracker that store the root directory and current directory
   * @param routelist a list of the strings that store the name of every directory in the path from
   *        root directory to target
   * @param path the path given by user
   * @return an empty string if the path is changed successfully
   * @exception SystemErrorException this exception with an error message is thrown if the input is
   *            invalid or fails to change current directory to target directory
   */
  private String changeDirectoryForWholeRoute(Tracker tracker, ArrayList<String> routeList,
      String path) throws SystemErrorException {
    JNode target;
    target = FindPath.findpath(routeList, tracker.getRootDirectory());
    if (target == null) {
      throw new SystemErrorException("Cannot find the path \"" + path + "\".");
    } else if (!target.getIsDirectory()) {
      throw new SystemErrorException("The target is not a directory.");
    } else {
      tracker.setCurrentDirectory((Directory) target);
    }
    return "";
  }

  /**
   * The function would change the current directory to target directory based on the give relative
   * path. The function would throw exception if the path is not exist or the target is not a
   * directory.
   * 
   * @param tracker a tracker that store the root directory and current directory
   * @param routelist a list of the strings that store the name of every directory in the path from
   *        current directory to target
   * @param path the path given by user
   * @return an empty string if the path is changed successfully
   * @exception SystemErrorException this exception with an error message is thrown if the input is
   *            invalid or fails to change current directory to target directory
   */
  private String changeDirectoryForRelativeRoute(Tracker tracker, ArrayList<String> routeList,
      String path) throws SystemErrorException {
    JNode target;
    target = FindPath.findpath(routeList, tracker.getCurrentDirectory());
    if (target == null) {
      throw new SystemErrorException("Cannot find the path \"" + path + "\".");
    } else if (!target.getIsDirectory()) {
      throw new SystemErrorException("The target is not a directory.");
    } else {
      tracker.setCurrentDirectory((Directory) target);
    }
    return "";
  }

  /**
   * The function would return the manual for ChangeDirectory command
   */
  @Override
  public String getManual() {
    return "cd DIR:\n" + "Change directory to DIR, which may be"
        + " relative to the current directory or\nmay be a full path. .. "
        + "means a parent directory and a . means the current \ndirectory.";
  }
}
