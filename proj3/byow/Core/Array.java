package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Array {

    private int WIDTH;
    private int HEIGHT;
    private static final int MINROOM = 7;
    private static final int MAXROOM = 12;
    private static final int ROOMSMIN = 6;
    private static final int ROOMSMAX = 12;
    private static final int HALLSIZE = 2;
    public TETile[][] grid;
    private Random r = new Random();
    private ArrayList<Room> roomList;
    private boolean gettingSeed = false;
    private String seed;
    private class Room {

        private int w;
        private int h;
        private int x;
        private int y;
        private Room(int width, int height, int xCoord, int yCoord) {
            w = width;
            h = height;
            x = xCoord;
            y = yCoord;
        }

        private void drawRoom(TETile[][] target) {
            for (int a = x; a <= (x + w); a++) {
                for (int b = y; b <= (y + h); b++) {
                    if (a == WIDTH) {
                        break;
                    }
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
                grid[a][b] = Tileset.SAND;
            }
        }

        grid[0][0] = Tileset.AVATAR;

        int rooms = r.nextInt(ROOMSMIN, ROOMSMAX);
        roomList = new ArrayList<>();
        for (int i = 0; i < rooms; i++) {
            Room rm = generateRoom();
            roomList.add(rm);
            rm.drawRoom(grid);
        }
        generateHallways();
    }

    private Room generateRoom() {
        int w = r.nextInt(MINROOM, MAXROOM);
        int h = r.nextInt(MINROOM, MAXROOM);
        int x = r.nextInt(0, WIDTH - w);
        int y = r.nextInt(0, HEIGHT - h);
        return new Room(w, h, x, y);
    }

    private void generateHallways() {
        for (int i = 0; i < roomList.size() - 1; i++) {
            drawHallway(roomList.get(i), roomList.get(i + 1));
        }
        drawHallway(roomList.get(0), roomList.get(roomList.size() - 1));
    }

    private String drawHallway(Room r1, Room r2) {
        Room hh = hHoriz(r1, r.nextInt(r2.x, r2.x + r2.w - HALLSIZE));
        Room hv = hVertic(r2, hh.y);
        hh.drawRoom(grid);
        hv.drawRoom(grid);
        return "horizontal " + Integer.toString(hh.x) + " vertical " + Integer.toString(hv.y);
    }

    private Room hHoriz(Room origin, int x) {
        if (x >= WIDTH) {
            x = WIDTH - 1;
        }
        if (x < 0) {
            x = 0;
        }

        if (x > origin.x) {
            return new Room(x - origin.x - HALLSIZE, HALLSIZE, origin.x + origin.w - HALLSIZE, r.nextInt(origin.y, origin.y + origin.h));
        }
        return new Room(origin.x - x + HALLSIZE, HALLSIZE, x, r.nextInt(origin.y, origin.y + origin.h));
    }

    private Room hVertic(Room origin, int y) {
        if (y >= HEIGHT) {
            y = HEIGHT - 1;
        }
        if (y < 0) {
            y = 0;
        }

        if (y < origin.y) {
            return new Room(HALLSIZE, origin.y - y + HALLSIZE, r.nextInt(origin.x, origin.x + origin.w - HALLSIZE), y);
        }
        return new Room(HALLSIZE, y - origin.y - origin.h + HALLSIZE + HALLSIZE, r.nextInt(origin.x, origin.x + origin.w - HALLSIZE), origin.y + origin.h - HALLSIZE);
    }

    public TETile[][] handleCommand(char c){
        if (gettingSeed) {
            seed = seed + Character.toString(c);
        }
        if (c == 'N') {
            gettingSeed = true;
        }
        if (c == 'S') {
            gettingSeed = false;
            r = new Random(seed.hashCode());
        }
        return grid;
    }

}
