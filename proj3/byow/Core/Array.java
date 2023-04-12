package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.Graph;

import java.util.Random;
import java.util.TreeMap;

public class Array {

    private int WIDTH;
    private int HEIGHT;
    private static final int MINROOM = 2;
    private static final int MAXROOM = 10;
    public TETile[][] grid;
    private Random r = new Random();
    private Graph g;
    private TreeMap<Integer, Room> gMap;

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
            for (int a = x; a <= (x + w); a++) {
                for (int b = y; b <= (y + h); b++) {
                    if ((a == x || a == (x + w) || b == y || b == (y + h)) && target[a][b] != Tileset.FLOOR) {
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

        int rooms = r.nextInt(4, 9);
        g = new Graph(rooms);
        gMap = new TreeMap<>();
        for (int i = 0; i < rooms; i++) {
            Room rm = generateRoom();
            gMap.put(i, rm);
            rm.drawRoom(grid);
        }
    }

    private Room generateRoom() {
        int w = r.nextInt(MINROOM, MAXROOM);
        int h = r.nextInt(MINROOM, MAXROOM);
        int x = r.nextInt(0, WIDTH - w);
        int y = r.nextInt(0, HEIGHT - h);
        return new Room(w, h, x, y);
    }

    private void generateHallways() {

    }

    public TETile[][] handleCommand(char c){
        return grid;
    }

}
