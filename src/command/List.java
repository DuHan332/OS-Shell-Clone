package command;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import driver.Tracker;
import exception.SystemErrorException;
import nodetype.*;
import helper.*;

/**
 * This class is implemented from interface Commands and it would list the files and directories in
 * several specific paths. For each path, it would add a String that contains the contents in the
 * path into the return String, if the path is a file, then its name would be added into the String.
 * 
 * @author Du Han
 */
public class List implements Commands {
  /**
   * This function would return the manual of this command.
   * 
   * @return a String that describes how to use this command.
   */
  public String getManual() {
    String Manual = "ls [PATH ��]:\n" + "If no paths are given, print "
        + "the contents (file or directory) of the current\ndirectory, "
        + "with a new line following each of the content (file or directory"
        + ").\nOtherwise, for each path p, the order listed:\nIf p "
        + "specifies a file, print p\nIf p specifies a directory, print p,"
        + " a colon, then the contents of that\ndirectory, then an extra "
        + "new line.\nIf p does not exist, print a suitable message.";
    return Manual;
  }

  /**
   * This function would recognize the input and call the proper helper function that matches the
   * input, then call it and return the result.
   * 
   * @param input the input from user
   * @param track a Tracker that track the root directory and current directory
   * @return a String that contains the result of this function
   * @throws SystemErrorException
   */

  public String execute(String[] input, Tracker track) throws SystemErrorException {
    if (input.length == 0) {
      return list(track.getCurrentDirectory());
    } else if (input.length == 1) {
      if (input[0].equals("-R")) {
        return reListNodes(track.getRootDirectory());
      }
      return listExe(input, track);
    } else {
      if (input[0].equals("-R")) {
        return listSub(input, track);
      }
      return listExe(input, track);
    }
  }

  /**
   * This function would merge all returned Strings from helper functions that list all contents of
   * a directory into one String and return it.
   * 
   * @param input the input from user
   * @param track a Tracker that track the root directory and current directory
   * @return a String that contains the result of this function
   * @throws SystemErrorException
   */
  private String listExe(String[] input, Tracker track) {
    StringBuilder result = new StringBuilder();
    try {
      for (int i = 0; i < input.length; i++) {
        result.append(list(input[i], track.getRootDirectory(), track.getCurrentDirectory()));
        if (i != input.length - 1) {
          result.append("\n" + "\n");
        }
      }
      String strresult = result.toString();
      return strresult;
    } catch (SystemErrorException e) {
      if (result.length() > 1 && result.charAt(result.length() - 1) == '\n') {
        result.delete(result.length() - 2, result.length());
      }
      result.append("\r" + e.getMessage());
      return result.toString();
    }
  }

  /**
   * This function would merge all returned Strings from helper functions that list all sub
   * directories of a directory into one String and return it.
   * 
   * @param input the input from user
   * @param track a Tracker that track the root directory and current directory
   * @return a String that contains the result of this function
   * @throws SystemErrorException
   */
  private String listSub(String input[], Tracker track) {
    StringBuilder result = new StringBuilder();
    try {
      if (input.length == 1) {
        result.append(reListNodes(track.getCurrentDirectory()));
        result.append("\r");
        return result.toString();
      } else {
        for (int i = 1; i < input.length; i++) {
          result.append(reList(input[i], track.getRootDirectory(), track.getCurrentDirectory()));
          if (i != input.length - 1) {
            result.append("\n" + "\n");
          }
        }
      }
      return result.toString();
    } catch (SystemErrorException e) {
      if (result.length() > 1 && result.charAt(result.length() - 1) == '\n') {
        result.delete(result.length() - 2, result.length());
      }
      result.append("\r" + e.getMessage());
      return result.toString();
    }
  }

  /**
   * This function would recognize if input is a full path or a relative path and call the proper
   * helper function.
   * 
   * @param input the inputed path from user
   * @param rd the root directory
   * @param cwd the current working directory
   * @return names of contents in a directory or name of a file
   * @throws Exception
   */
  static private String list(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    if (input.substring(0, 1).equals("/")) {
      return listForFull(input, rd, cwd);
    } else {
      return listForRe(input, rd, cwd);
    }
  }

  /**
   * This function would recognize if input is a full path or a relative path and call the proper
   * helper function.
   * 
   * @param input the inputed path from user
   * @param rd the root directory
   * @param cwd the current working directory
   * @return names of contents in a directory or name of a file
   * @throws Exception
   */
  static private String reList(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    if (input.substring(0, 1).equals("/")) {
      return reListForFull(input, rd, cwd);
    } else {
      return reListForRe(input, rd, cwd);
    }
  }

  /**
   * This function overwrites the list function and it would be called when no parameter is inputed,
   * it would call list nodes with given directory directly.
   * 
   * @param directory a directory
   * @return names of contents in this directory
   */
  private static String list(Directory directory) {
    String result = listNodes(directory);
    return result;
  }

  /**
   * this function would convert a full path to a ArrayList and call PathFinder to find this path
   * and then call list nodes to return the proper String.
   * 
   * @param input a full path
   * @param rd the root directory
   * @param cwd the current directory
   * @return names of contents in a directory or name of a file
   * @throws Exception
   */
  static private String listForFull(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    if (input.equals("/")) {
      return list(rd);
    }
    String[] temp = input.split("/");
    ArrayList<String> path = new ArrayList<>();
    for (String i : temp) {
      path.add(i);
    }
    path.remove(0);
    JNode target = FindPath.findpath(path, rd);
    if (target == null) {
      throw new SystemErrorException(input + " directory does not exist");
    } else if (target.getIsDirectory() == false) {
      return target.getName();
    } else {
      String result = listNodes((Directory) target);
      return result;
    }
  }

  /**
   * this function would convert a full path to a ArrayList and call PathFinder to find this path
   * and then call list nodes to return the proper String.
   * 
   * @param input a full path
   * @param rd the root directory
   * @param cwd the current directory
   * @return names of contents in a directory or name of a file
   * @throws Exception
   */
  static private String reListForFull(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    if (input.equals("/")) {
      return reListNodes(rd);
    }
    String[] temp = input.split("/");
    ArrayList<String> path = new ArrayList<>();
    for (String i : temp) {
      path.add(i);
    }
    path.remove(0);
    JNode target = FindPath.findpath(path, rd);
    if (target == null) {
      throw new SystemErrorException(input + " directory does not exist");
    } else if (target.getIsDirectory() == false) {
      return target.getName();
    } else {
      String result = reListNodes((Directory) target);
      return result;
    }
  }

  /**
   * this function would convert a relative path to a ArrayList and call PathFinder to find this
   * path then call list nodes to return the proper String.
   * 
   * @param input a relative path
   * @param rd the root directory
   * @param cwd the current directory
   * @return names of contents in a directory or name of a file
   * @throws Exception
   */
  static private String listForRe(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    String[] temp = input.split("/");
    ArrayList<String> path = new ArrayList<>();
    for (String i : temp) {
      path.add(i);
    }
    JNode target = FindPath.findpath(path, cwd);
    if (target == null) {
      throw new SystemErrorException(input + " directory does not exist");
    } else if (target.getIsDirectory() == false) {
      return target.getName();
    } else {
      String result = listNodes((Directory) target);
      return result;
    }
  }

  /**
   * this function would convert a relative path to a ArrayList and call PathFinder to find this
   * path then call list nodes to return the proper String.
   * 
   * @param input a relative path
   * @param rd the root directory
   * @param cwd the current directory
   * @return names of contents in a directory or name of a file
   * @throws Exception
   */
  static private String reListForRe(String input, Directory rd, Directory cwd)
      throws SystemErrorException {
    String[] temp = input.split("/");
    ArrayList<String> path = new ArrayList<>();
    for (String i : temp) {
      path.add(i);
    }
    JNode target = FindPath.findpath(path, cwd);
    if (target == null) {
      throw new SystemErrorException(input + " directory does not exist");
    } else if (target.getIsDirectory() == false) {
      return target.getName();
    } else {
      String result = reListNodes((Directory) target);
      return result;
    }
  }

  /**
   * This function would return a String, begin with the name of the directory and followed by a
   * colon, then the names of directories or files in the directory.
   * 
   * @param directory a directory that contain files and directories that we need to list
   * @return names of contents in a directory
   */
  static private String listNodes(Directory directory) {
    ArrayList<String> ctList = new ArrayList<String>();
    for (JNode node : directory.getSub()) {
      ctList.add(node.getName());
    }
    StringBuilder result = new StringBuilder();
    result.append(directory.getName() + ":");
    for (String temp : ctList) {
      result.append(temp);
      result.append(" ");
    }
    if (ctList.size() != 0) {
      result.deleteCharAt(result.length() - 1);
    }
    String strresult = result.toString();
    return strresult;
  }

  /**
   * This function would traverse the directory system and return a String, with the name of all the
   * sub directories in a specific directory.
   * 
   * @param directory a directory that contain files and directories that we need to list
   * @return name of all the sub directories in directory.
   */
  static private String reListNodes(Directory directory) {
    StringBuilder result = new StringBuilder();
    Deque<JNode> nodeDeque = new LinkedList<>();
    JNode node = directory;
    nodeDeque.add(node);
    while (!nodeDeque.isEmpty()) {
      node = nodeDeque.pop();
      if (node.getIsDirectory()) {
        result.append(listNodes((Directory) node) + "\n\n");
        nodeDeque.addAll(((Directory) node).getSub());
      }
    }
    result.delete(result.length() - 2, result.length());
    return result.toString();
  }
}

