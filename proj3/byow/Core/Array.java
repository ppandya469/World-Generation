package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

public class Array {

    private int WIDTH = 80;
    private int HEIGHT = 50;
    private int MINROOM = 7;
    private int MAXROOM = 12;
    private int ROOMSMIN = 6;
    private int ROOMSMAX = 10;
    private static final int HALLSIZE = 2;
    public TETile[][] grid;
    private Random r = new Random();
    private ArrayList<Room> roomList;
    private boolean gettingSeed = false;
    private boolean awaitingQ = false;
    private String seed = "";
    private int[] playerCoords;
    private class Room {

        private String type = "generic";
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
            int choice = r.nextInt(5);
            type = switch (choice) {
                case 0 -> "rubble";
                case 1 -> "fountain";
                case 2 -> "rug";
                case 3 -> "chest";
                default -> "key";
            };
        }

        private int[] addPlayer(TETile[][] target) {
            int[] coords = new int[2];
            coords[0] = x + w / 2;
            coords[1] = y + h /2;
            target[coords[0]][coords[1]] = Tileset.AVATAR;
            return coords;
        }

    }

    public Array(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        grid = new TETile[WIDTH][HEIGHT + 1];
        fillArray();
    }

    private void fillArray() {
        for (int a = 0; a < WIDTH; a++) {
            for (int b = 0; b <= HEIGHT; b++) {
                if (b == HEIGHT) {
                    grid[a][b] = Tileset.WALL;
                } else {
                    grid[a][b] = Tileset.NOTHING;
                }
            }
        }

        int rooms = r.nextInt(ROOMSMIN, ROOMSMAX);
        roomList = new ArrayList<>();
        for (int i = 0; i < rooms; i++) {
            Room rm = generateRoom();
            roomList.add(rm);
            rm.drawRoom(grid);
        }
        generateHallways();
        playerCoords = roomList.get(0).addPlayer(grid);
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
        //Room hh = hHoriz(r1, r.nextInt(r2.x, r2.x + r2.w - HALLSIZE));
        Room hh = hHoriz(r1, r2.x + (r2.w / 2) + HALLSIZE);
        Room hv = hVertic(r2, hh.y);
        hh.drawRoom(grid);
        hv.drawRoom(grid);
        return "horizontal " + Integer.toString(hh.x) + " vertical " + Integer.toString(hv.y);
    }

    private Room hHoriz(Room origin, int x) {
        if (x >= WIDTH) {
            x = WIDTH - HALLSIZE;
        }
        if (x < HALLSIZE) {
            x = HALLSIZE;
        }

        if (x > origin.x) {
            return new Room(x - origin.x - origin.w + HALLSIZE, HALLSIZE, origin.x + origin.w - HALLSIZE, r.nextInt(origin.y, origin.y + origin.h - HALLSIZE));
        }
        return new Room(origin.x - x + HALLSIZE + HALLSIZE, HALLSIZE, x - HALLSIZE, r.nextInt(origin.y, origin.y + origin.h - HALLSIZE));
    }

    private Room hVertic(Room origin, int y) {
        if (y >= HEIGHT - 1) {
            y = HEIGHT - 2;
        }
        if (y < 0) {
            y = 0;
        }

        if (y < origin.y) {
            return new Room(HALLSIZE, origin.y - y + HALLSIZE, r.nextInt(origin.x, origin.x + origin.w - HALLSIZE), y);
        }
        return new Room(HALLSIZE, y - origin.y - origin.h + HALLSIZE + HALLSIZE, r.nextInt(origin.x, origin.x + origin.w - HALLSIZE), origin.y + origin.h - HALLSIZE);
    }

    public TETile[][] handleCommand(char c) {
        if ((c == 'q' || c == 'Q') && awaitingQ) {
            saveandquit();
        }
        if (c == 'S') {
            gettingSeed = false;
            r = new Random(Long.valueOf(seed));
            fillArray();
        }
        if (awaitingQ) {
            awaitingQ = false;
        }
        if (gettingSeed) {
            seed = seed + Character.toString(c);
            return grid;
        }
        if (c == ':') {
            awaitingQ = true;
        }
        if (c == 'N') {
            gettingSeed = true;
        }
        playerMove(c);
        return grid;
    }

    private void saveandquit() {
        System.out.println("saving and quitting");
    }

    private void playerMove(char c) {
        if (c == 'w' && grid[playerCoords[0]][playerCoords[1] + 1].equals(Tileset.FLOOR)) {
            grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
            playerCoords[1]++;
            grid[playerCoords[0]][playerCoords[1]] = Tileset.AVATAR;
        } else if (c == 'a' && grid[playerCoords[0] - 1][playerCoords[1]].equals(Tileset.FLOOR)) {
            grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
            playerCoords[0]--;
            grid[playerCoords[0]][playerCoords[1]] = Tileset.AVATAR;
        } else if (c == 's' && grid[playerCoords[0]][playerCoords[1] - 1].equals(Tileset.FLOOR)) {
            grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
            playerCoords[1]--;
            grid[playerCoords[0]][playerCoords[1]] = Tileset.AVATAR;
        } else if (c == 'd' && grid[playerCoords[0] + 1][playerCoords[1]].equals(Tileset.FLOOR)){
            grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
            playerCoords[0]++;
            grid[playerCoords[0]][playerCoords[1]] = Tileset.AVATAR;
        }
    }

    public static void main(String[] args) {
        Array a = new Array(80, 50);
        for (int i = 0; i < 1000; i++) {
            a.fillArray();
        }
    }

}
