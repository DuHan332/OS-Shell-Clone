package command;

import java.util.ArrayList;
import nodetype.*;
import driver.Tracker;
import exception.SystemErrorException;
import helper.*;

/**
 * This class is implemented from from interface Commands and is able to create new directories
 * based on user's input.
 * 
 * @author Yuanqian Fang
 *
 */
public class MakeDirectory implements Commands {
  /**
   * a Directory represents the current directory
   */
  private Directory currentdirectory;
  /**
   * a Directory represents the root
   */
  private Directory root;

  public String getManual() {
    /**
     * This function would return the manual of this command.
     * 
     * @return a String that describes how to use this command.
     */
    String Manual = "mkdir DIR1 DIR2:\n" + "This command takes in two"
        + " arguments only. Create directories, each of which\nmay be "
        + "relative to the current directory or may be a full path. If "
        + "creating\nDIR1 results in any kind of error, do not proceed with"
        + " creating DIR 2.\nHowever, if DIR1 was created successfully, "
        + "and DIR2 creation results in an\nerror, then give back an error " + "specfic to DIR2.";
    return Manual;
  }

  /**
   * This function would check the length of input and throws error if the length doesn't equal to
   * 2. The the function would call another function named mkdir. The function returns empty string
   * if creating directories successfully. The function throws error corresponding to what mkdir
   * returns.
   * 
   * @param input input from the user
   * @param track a Tracker that track the root directory and current directory
   * @return an empty string
   * @throws SystemErrorException
   */
  public String execute(String[] input, Tracker track) throws SystemErrorException {
    this.currentdirectory = track.getCurrentDirectory();
    this.root = track.getRootDirectory();
    int i = 0;
    if (input.length == 0) {
      throw new SystemErrorException("This command must at least take one argument");
    }
    while (i < input.length) {
      if (mkdir(input[i]) == 1) {
        i++;
      } else if (mkdir(input[i]) == 2) {
        throw new SystemErrorException(
            "The number" + String.valueOf(i + 1) + " path has already existed");
      } else if (mkdir(input[i]) == 3) {
        throw new SystemErrorException("The number" + String.valueOf(i + 1) + " path is invalid");
      } else if (mkdir(input[i]) == 4) {
        throw new SystemErrorException(
            "The number" + String.valueOf(i + 1) + " directory's name contains invalid character");
      }
    }
    return "";
  }

  /**
   * This function would check whether the directory that user wants to create exists in the current
   * directory.
   * 
   * @param directory current directory that need to be checked
   * @param to_plug the name of directory that user wants to create
   * @return true or false
   */
  private boolean IsExist(Directory diretory, String toPlug) {
    boolean result = false;
    for (JNode s : diretory.getSub()) {
      if (toPlug.equals(s.getName())) {
        result = true;
      }
    }
    return result;
  }

  /**
   * This function transform a String that contains the information of path to an ArrayList of
   * String.
   * 
   * @param path a String that contains the information of path
   * @return an ArrayList of String that stores name of every directory appears in the path
   */
  private ArrayList<String> pathToArrayList(String path) {
    String[] subPath = path.split("/");
    ArrayList<String> result = new ArrayList<>();
    for (String s : subPath) {
      result.add(s);
    }
    if (result.get(0).equals(""))
      result.remove(0);
    return result;
  }

  /**
   * This function would add the new directory to its root directory's sub.
   * 
   * @param directory a directory that need to be added with a new directory
   * @param name a string that stands for the name of a new directory
   */
  private void add(Directory directory, String name) {
    JNode newThing = new Directory(directory, name);
    ArrayList<JNode> sub = new ArrayList<JNode>();
    sub = directory.getSub();
    sub.add(newThing);
    directory.setSub(sub);
  }

  /**
   * This function would first call the helper function named isValidName to check whether the name
   * of directory contains invalid characters and then call a helper function of findpath to check
   * whether the path exists or not and it would call IsExist to check whether the directory that
   * user wants to add exists or not. If it doesn't find anything wrong, it would create a new
   * directory and return an integer to show that the directory has been created. Otherwise, it
   * would return integers to show different types of error.
   * 
   * @param directory a directory that stands for root or current directory
   * @param dir a String of the name of directory that user wants to add
   * @return integers to show different types of error happens or the directory has been created
   */
  private int checkAndAdd(Directory directory, String path) {


    ArrayList<String> temp = pathToArrayList(path);
    String needAdd = temp.get(temp.size() - 1);
    if (IsValidName.isValidName(needAdd) == true)
      return 4;
    temp.remove(temp.size() - 1);
    JNode returnDirectory = FindPath.findpath(temp, directory);
    if (returnDirectory != null && returnDirectory.getIsDirectory() == true) {
      if (IsExist((Directory) returnDirectory, needAdd) == false) {
        add((Directory) returnDirectory, needAdd);
        return 1;
      } else
        return 2;
    } else
      return 3;
  }

  /**
   * This function would judge the path the user put in is an absolute path or a relative path and
   * then call the function of checkAndAdd
   * 
   * @param dir a String of the name of directory that user wants to add
   * @return integers depends on what checkAndAdd returns
   */

  private int mkdir(String path) {
    if (path.substring(0, 1).equals("/") == false) {
      return checkAndAdd(currentdirectory, path);
    } else {
      return checkAndAdd(root, path);
    }
  }

}


