package command;

import java.util.ArrayList;

import driver.Tracker;
import exception.SystemErrorException;
import helper.FindPath;
import nodetype.*;

/**
 * This class is implemented from Commands and it would show the a String with contents of inputed
 * file(s) that are concatenated in the shell. If some paths do not exist, followed paths would not
 * do be read.
 * 
 * @author Du Han
 *
 */
public class Concatenate implements Commands {
  /**
   * This function would return the manual of this command.
   * 
   * @return a String that describes how to use this command.
   */
  public String getManual() {
    String Manual = "cat FILE1 [FILE2 ��]:\nDisplay the contents of "
        + "FILE1 and other files concatenated in the shell.";
    return Manual;
  }

  /**
   * This function would return a String with contents of file(s) that are given by user. For each
   * path, it would call cat function to call the proper helper function get contents of files then
   * add them into this String.
   * 
   * @param tracker the tracker that record the root directory and the current directory
   * @return the String that should be returned
   * @throws SystemErrorException
   */

  public String execute(String[] input, Tracker tracker) throws SystemErrorException {
    if (input.length == 0) {
      throw new SystemErrorException("must have at least one parameter");
    } else {
      StringBuilder result = new StringBuilder();
      try {
        for (int i = 0; i < input.length; i++) {
          String temp = cat(input[i], tracker.getRootDirectory(), tracker.getCurrentDirectory());
          result.append(temp);
          if (i != input.length - 1) {
            result.append("\n" + "\n" + "\n" + "\n");
          }
        }
        return result.toString();
      } catch (SystemErrorException e) {
        if (result.length() > 1 && result.charAt(result.length() - 1) == '\n') {
          result.delete(result.length() - 4, result.length());
        }
        result.append("\r" + e.getMessage());
        return result.toString();
      }
    }
  }


  /**
   * This function would recognize if the inputed path is a full path or a relative path and call
   * the proper helper function to find the path and read the content in the file, if the path does
   * not exist or leads to a directory, it would throw errer.
   * 
   * @param input one of paths given by user
   * @param rd the root directory
   * @param cwd the current working directory
   * @return String with contents in a file or String that represents error
   * @throws SystemErrorException
   */
  private static String cat(String input, Directory rd, Directory cwd) throws SystemErrorException {
    if (input.substring(0, 1).equals("/")) {
      return cat4full(input, rd, cwd);
    } else {
      return cat4re(input, rd, cwd);
    }
  }

  /**
   * this function would convert a full path to a ArrayList and call PathFinder to find this path,
   * then return the contents in the file leaded by the path if it exists. If the path does not
   * exist or leads to a directory, it would throw error
   * 
   * @param input a full path
   * @param rd the root directory
   * @param cwd the current directory
   * @return String with contents in a file or String that represents error
   * @throws SystemErrorException
   */
  private static String cat4full(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    if (input.equals("/")) {
      throw new SystemErrorException(input + " is not a file");
    }
    String[] temp = input.split("/");
    ArrayList<String> path = new ArrayList<>();
    for (String i : temp) {
      path.add(i);
    }
    path.remove(0);
    JNode target = FindPath.findpath(path, rd);
    if (target == null) {
      throw new SystemErrorException(input + " does not exist");
    } else if (target.getIsDirectory() == false) {
      return ((File) target).getContent();
    } else {
      throw new SystemErrorException(input + " is not a file");
    }
  }

  /**
   * this function would convert a relative path to a ArrayList and call PathFinder to find this
   * path, then return the contents in the file leaded by the path if it exists. If the path does
   * not exist or leads to a directory, it would throw error
   * 
   * @param input a relative path
   * @param rd the root directory
   * @param cwd the current directory
   * @return String with contents in a file or String that represents error
   * @throws SystemErrorException
   */
  private static String cat4re(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    String[] temp2 = input.split("/");
    ArrayList<String> path2 = new ArrayList<>();
    for (String i : temp2) {
      path2.add(i);
    }
    JNode target = FindPath.findpath(path2, cwd);
    if (target == null) {
      throw new SystemErrorException(input + " does not exist");
    } else if (target.getIsDirectory() == false) {
      return ((File) target).getContent();
    } else {
      throw new SystemErrorException(input + " is not a file");
    }
  }
}
