// **********************************************************
// Assignment2:
// Student1: Dezhi Ren
// UTORID user_name: rendezhi
// UT Student #: 1005736795
// Author: Ren, Dezhi
//
// Student1: Yuanqian Fang
// UTORID user_name: fangyu29
// UT Student #: 1006619919
// Author: Yuanqian Fang
//
// Student3: Du Han
// UTORID user_name: handu
// UT Student #: 1005681727
// Author: Du Han
//
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package driver;

import java.util.Scanner;
import command.Exit;
import nodetype.*;

// import jdk.internal.util.xml.impl.Input;
//
/***
 * This Class would initialize the interpreter and ask user to input commands continually and then
 * call InputInterpreter to execute these inputs. It would repeat it until "Exit" is inputted.
 * 
 * @author DuHan
 *
 */
public class JShell {
  public static void main(String[] args) {
    InputInterpreter interpreter = new InputInterpreter();
    Directory root = new Directory(null, "/");
    interpreter.tracker = new Tracker();
    interpreter.tracker.setRootDirectory(root);
    interpreter.tracker.setCurrentDirectory(root);
    interpreter.initializeCommandHashMap();
    interpreter.tracker.setSwc(true);
    System.out.print("     ____. _________.__           .__  .__   \n"
        + "    |    |/   _____/|  |__   ____ |  | |  |  \n"
        + "    |    |\\_____  \\ |  |  \\_/ __ \\|  | |  |  \n"
        + "/\\__|    |/ JShell \\|   Y  \\  ___/|  |_|  |__\n"
        + "\\________/_______  /|___|  /\\___  >____/____/\n"
        + "                 \\/      \\/     \\/     Beta  \n");
    System.out.println("Welcome to JShell Beta");
    System.out.println("author: Dezhi Ren, Yuanqian Fang and Du Han");
    while (interpreter.tracker.getSwc()) {
      String input;
      Scanner in = new Scanner(System.in);
      System.out.print("/#: ");
      input = in.nextLine();
      if (!input.equals(""))
        interpreter.interprete(input);
    }
  }
}
