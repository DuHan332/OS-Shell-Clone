package helper;

import java.util.ArrayList;

import nodetype.*;

/**
 * This class contains a helper function which help commands to find a directory by specific path.
 * 
 * @author Du Han
 *
 */
public class FindPath<E> {


  /**
   * This helper function would find the specific JNode by a path which is stored in a ArrayList.
   * This function would find the first JNode that has same name with the first entry of the path in
   * the parameter directory until the path is empty and return the last JNode in the path. This
   * function would return a File only when it is the last entry in the path.
   * 
   * @param list the ArrayList the store the path
   * @param directory The directory that I should find path in
   * @return a directory or a file which is the last entry in the path, it's null if the target path
   *         doese not exist.
   */
  static public JNode findpath(ArrayList<String> list, Directory directory) {
    if (list.isEmpty()) {
      return directory;
    } else {
      for (JNode i : directory.getSub()) {
        if (i.getName().equals(list.get(0))) {
          if (i.getIsDirectory() == false) {
            if (list.size() == 1) {
              return i;
            } else {
              return null;
            }
          }
          list.remove(0);
          return findpath(list, (Directory) i);
        }
      }
      return null;
    }
  }

}
