package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Random;
import java.util.TreeMap;

public class Array {

    private int WIDTH;
    private int HEIGHT;
    private static final int MINROOM = 5;
    private static final int MAXROOM = 15;
    private static final int ROOMSMIN = 6;
    private static final int ROOMSMAX = 12;
    private static final int HALLSIZE = 2;
    public TETile[][] grid;
    private Random r = new Random();
    private WeightedQuickUnionUF uf;
    private TreeMap<Integer, Room> gMap;
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

        int rooms = r.nextInt(ROOMSMIN, ROOMSMAX);
        uf = new WeightedQuickUnionUF(rooms);
        gMap = new TreeMap<>();
        for (int i = 0; i < rooms; i++) {
            Room rm = generateRoom();
            gMap.put(i, rm);
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
        for (int i = 0; i < gMap.size(); i++) {
            int a = i;
            while (a == i) {
                a = r.nextInt(gMap.size());
            }
            uf.union(i, a);
            drawHallway(i, a);
        }
    }

    private void drawHallway(int ind1, int ind2) {
        Room r1 = gMap.get(ind1);
        Room r2 = gMap.get(ind2);

        if (r1.x + r1.w > r2.x && r1.x + r1.w < r2.x + r2.w) {
            Room hv = hVertic(r1, r2.y + 1);
            hv.drawRoom(grid);
            return;
        }
        if (r1.y + r1.h > r2.y && r1.y + r1.h < r2.y + r2.h) {
            Room hh = hHoriz(r1, r2.x + 1);
            hh.drawRoom(grid);
            return;
        }

        int x = r.nextInt(r1.x, r1.x + r1.w);
        int y = r.nextInt(r2.y, r2.y + r2.h);
        Room hv = hVertic(r2, x);
        Room hh = hHoriz(r1, y);
        hh.drawRoom(grid);
        hv.drawRoom(grid);
    }

    private Room hHoriz(Room origin, int x) {
        if (x >= WIDTH) {
            x = WIDTH - 1;
        }
        if (x < 0) {
            x = 0;
        }

        if (x < origin.x) {
            return new Room(origin.x + 1 - x, HALLSIZE, x, r.nextInt(origin.y, origin.y + origin.h));
        }
        return new Room(x - (origin.x + origin.w), HALLSIZE, origin.x + origin.w - 1, r.nextInt(origin.y, origin.y + origin.h));
    }

    private Room hVertic(Room origin, int y) {
        if (y >= HEIGHT) {
            y = HEIGHT - 1;
        }
        if (y < 0) {
            y = 0;
        }
        
        if (y < origin.y) {
            return new Room(HALLSIZE, origin.y + 1 - y, r.nextInt(origin.x, origin.x + origin.w), y);
        }
        return new Room(HALLSIZE, y - (origin.y + origin.h) + 1, r.nextInt(origin.x, origin.x + origin.w), origin.y + origin.h - 1);
    }

    public TETile[][] handleCommand(char c){
        return grid;
    }

}
