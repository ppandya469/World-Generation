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
    public TETile[][] grid;
    private Random r = new Random();
    private WeightedQuickUnionUF uf;
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
            drawHallway(i, a);
        }
    }

    private void drawHallway(int ind1, int ind2) {
        Room r1 = gMap.get(ind1);
        Room r2 = gMap.get(ind2);

        Room hX;
        Room hY;
        if (r2.y > r1.y && r2.y < (r2.y + r2.h)) {
            hY = new Room(3, r1.y - (r2.y + r2.h), r.nextInt(r2.y, r2.y + r2.h), r2.y + r2.h);
            hY.drawRoom(grid);
            return;
        } else if (r1.x > r2.x) {
            if (r1.x < (r2.x + r2.w)) {
                hX = new Room(r2.x - (r1.x + r1.w), 3, r1.x + r1.w, r.nextInt(r1.y, r1.y + r1.h));
                hX.drawRoom(grid);
                return;
            }
            int width = r.nextInt(r2.x, r2.x + r2.w);
            if (r1.x + r1.w + width > WIDTH) {
                width = WIDTH;
            }
            hX = new Room(width, 3, r1.x + r1.w, r.nextInt(r1.y, r1.y + r1.h));
            hX.drawRoom(grid);
        } else {
            int width = r.nextInt(r2.x, r2.x + r2.w);
            if (r1.x < width) {
                width = r1.x;
            }
            hX = new Room(width, 3, r1.x - width, r.nextInt(r1.y, r1.y + r1.h));
            hX.drawRoom(grid);
        }
        if (r2.y > r1.y) {
            hY = new Room(3, r1.y - (r2.y + r2.h), hX.x - 3, r2.y + r2.h);
            hY.drawRoom(grid);
        } else {
            int height = r.nextInt(r1.y, r1.y + r1.h);
            if (r2.y - height < 0) {
                height = r2.y;
            }
            hY = new Room(3, r1.y - (r2.y + r2.h), hX.x + 3, r2.y - height);
            hY.drawRoom(grid);
        }
    }

    public TETile[][] handleCommand(char c){
        return grid;
    }

}
