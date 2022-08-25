package command;

import java.util.ArrayList;
import driver.Tracker;
import exception.SystemErrorException;
import nodetype.*;
import helper.FindPath;

/**
 * This class is implemented from interface Commands and it would copy the item from old path into
 * new path
 * 
 * @author Dezhi Ren
 *
 */

public class CopyItem implements Commands {

  private boolean isCopyDirectory;

  /**
   * The function would first check input and throw error if the input is invalid. Then the function
   * would determine whether the old path is a file or directory and call the corresponding method
   * to copy it.
   * 
   * @param parameter the input from user interpreted by driver.InputInterpreter
   * @param tracker a tracker that store the root directory and current directory
   * @return an empty string if the item is copied successfully
   * @exception SystemErrorException this exception with an error message is thrown if the input is
   *            invalid
   */
  @Override
  public String execute(String[] input, Tracker tracker) throws SystemErrorException {
    if (input.length > 2) {
      throw new SystemErrorException("There are more than two path!");
    } else if (input.length == 1) {
      throw new SystemErrorException("Lossing NEWPATH!");
    } else if (input.length == 0) {
      throw new SystemErrorException("Lossing OLDPATH and NEWPATH!");
    }
    JNode item = getOldPath(input, tracker);
    Directory targetDirectory = getNewPath(input, tracker);
    for (JNode i : targetDirectory.getSub())
      if (i.getName().equals(item.getName()) && i.getIsDirectory() == item.getIsDirectory()) {
        String isDirectory = item.getIsDirectory() ? "directory" : "file";
        throw new SystemErrorException(
            "There already exists a " + isDirectory + " named: " + item.getName());
      }
    if (item.getIsDirectory()) {
      this.isCopyDirectory = true;
      targetDirectory.getSub().add(copyDirectory((Directory) item, targetDirectory));
    } else {
      this.isCopyDirectory = false;
      targetDirectory.getSub().add(copyFile((File) item, targetDirectory));
    }

    return "";
  }

  /**
   * The function would copy the file.
   * 
   * @param org the original file
   * @param newFather the new path
   * @return newFile the copy of the file
   */
  private static File copyFile(File org, Directory newFather) {
    File newFile = new File(org.getContent(), newFather, org.getName());
    return newFile;
  }

  /**
   * The function would copy the directory recursively.
   * 
   * @param org the original directory
   * @param newFather the new path
   * @return newDirectory the copy of the directory
   */
  private static Directory copyDirectory(Directory org, Directory newFather) {
    Directory newDirectory = new Directory(newFather, org.getName());
    if (org.getSub().size() == 0) {
      return newDirectory;
    }
    for (JNode i : org.getSub()) {
      if (i.getIsDirectory()) {
        newDirectory.getSub().add((copyDirectory((Directory) i, newDirectory)));
      } else {
        newDirectory.getSub().add(copyFile((File) i, newDirectory));
      }
    }
    return newDirectory;
  }

  /**
   * The function would return the old path item from the input
   * 
   * @param input the user's input
   * @param tracker a tracker that store the root directory and current directory
   * @return oldPathItem the item get from the old path
   * @exception SystemErrorException this exception with an error message is thrown if the old path
   *            is wrong
   */
  private static JNode getOldPath(String[] input, Tracker tracker) throws SystemErrorException {
    if (input[0].equals("/"))
      throw new SystemErrorException("You cannot copy the root directory");
    String[] oldPathList = input[0].split("/");
    ArrayList<String> oldPathArrayList = new ArrayList<String>();
    for (String i : oldPathList)
      oldPathArrayList.add(i);
    JNode oldPathItem;
    if (oldPathList[0].equals("")) {
      oldPathArrayList.remove(0);
      oldPathItem = FindPath.findpath(oldPathArrayList, tracker.getRootDirectory());
    } else {
      oldPathItem = FindPath.findpath(oldPathArrayList, tracker.getCurrentDirectory());
    }
    if (oldPathItem == null) {
      throw new SystemErrorException("The OLDPATH is wrong");
    }
    return oldPathItem;
  }

  /**
   * The function would return the new path directory from the input
   * 
   * @param input the user's input
   * @param tracker a tracker that store the root directory and current directory
   * @return newPath the directory get from the old path
   * @exception SystemErrorException this exception with an error message is thrown if the new path
   *            is wrong
   */
  private static Directory getNewPath(String[] input, Tracker tracker) throws SystemErrorException {
    if (input[1].equals("/"))
      return tracker.getRootDirectory();
    String[] newPathList = input[1].split("/");
    ArrayList<String> newPathArrayList = new ArrayList<String>();
    for (String i : newPathList)
      newPathArrayList.add(i);
    JNode newPath;
    if (newPathList[0].equals("")) {
      newPathArrayList.remove(0);
      newPath = FindPath.findpath(newPathArrayList, tracker.getRootDirectory());
    } else {
      newPath = FindPath.findpath(newPathArrayList, tracker.getCurrentDirectory());
    }
    if (newPath == null) {
      throw new SystemErrorException("The NEWPATH is wrong");
    }
    if (!newPath.getIsDirectory()) {
      throw new SystemErrorException("The NEWPATH is a file");
    }
    return (Directory) newPath;
  }

  public boolean isCopyDirectory() {
    return isCopyDirectory;
  }

  /**
   * The function would return the manual for CopyItem command
   */
  @Override
  public String getManual() {
    return "cp OLDPATH NEWPATH:\nLike mv, but don��t remove OLDPATH. "
        + "If OLDPATH is a directory, recursively\ncopy the contents.";
  }

}
