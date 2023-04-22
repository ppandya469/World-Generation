package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
//import org.junit.Test;

//import java.awt.*;

//import static com.google.common.truth.Truth.assertThat;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        Array a = new Array(WIDTH, HEIGHT);
        ter.initialize(WIDTH, HEIGHT);
        /*ter.renderFrame(a.grid);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String out = a.handleCommand(StdDraw.nextKeyTyped());
                ter.renderFrame(a.grid);
                if (out.equals("quit")) {
                    System.exit(0);
                }
            }
        }*/
        while (true) {
            if (a.ready) {
                ter.renderFrame(a.grid);
            } else {
                a.mainMenu();
            }
            if (StdDraw.hasNextKeyTyped()) {
                String out = a.handleCommand(StdDraw.nextKeyTyped());
                if (out.equals("quit")) {
                    System.exit(0);
                }
            }
        }
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww"). The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for different input types.
        char[] c = input.toCharArray();
        Array a = new Array(WIDTH, HEIGHT);
        for (int i = 0; i < c.length; i++) {
            String out = a.handleCommand(c[i]);
        }
        System.out.println(input);
        return a.grid;
    }

    public static void main(String[] args) {
        Engine e = new Engine();
        e.interactWithKeyboard();
    }
}
