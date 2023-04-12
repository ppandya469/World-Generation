package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.Random;

public class Array {

    private int WIDTH;
    private int HEIGHT;
    public TETile[][] grid;
    private Random r = new Random();

    private class Room {

        private int w;
        private int h;
        private int x;
        private int y;
        public Room(int width, int height, int xCoord, int yCoord) {
            w = width;
            h = height;
            x = xCoord;
            y = yCoord;
        }

        private void drawRoom(TETile[][] target) {
            for (int a = x; x <= (x + w); x++) {
                for (int b = y; y <= (y + h); y++) {
                    if (a == x || a == (x + w) || b == y || b == (y + h)) {
                        target[a][b] = Tileset.WALL;
                    } else {
                        target[a][b] = Tileset.FLOOR;
                    }
                }
            }
        }

    }
    public Array(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        grid = new TETile[WIDTH][HEIGHT];
        for (int a = 0; a < WIDTH; a++) {
            for (int b = 0; b < HEIGHT; b++) {
                grid[a][b] = Tileset.NOTHING;
            }
        }

        int rooms = r.nextInt(4, 6);
        for (int i = 0; i < rooms; i++) {
            Room rm = generateRoom();
            rm.drawRoom(grid);
        }
    }

    private Room generateRoom() {
        int w = r.nextInt(2, 5);
        int h = r.nextInt(2, 5);
        int x = r.nextInt(0, WIDTH - w);
        int y = r.nextInt(0, HEIGHT - h);
        return new Room(w, h, x, y);
    }

    private void generateHallways() {

    }

    public TETile[][] handleCommand(char c){
        return grid;
    }

    public static void main(String[] args) {
        Array a = new Array(80, 30);
        TERenderer ter = new TERenderer();
        ter.initialize(a.WIDTH, a.HEIGHT);
        ter.renderFrame(a.grid);
    }
}
