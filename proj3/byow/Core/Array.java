package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

//import java.io.FileNotFoundException;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
//import java.util.Scanner;
//import javax.swing.Timer;

public class Array {

    private int WIDTH = 80;
    private int HEIGHT = 50;
    private int MINROOM = 7;
    private int MAXROOM = 12;
    private int ROOMSMIN = 6;
    private int ROOMSMAX = 10;
    private int COINNUM = 5;
    public long timer = 10;
    public long sysTicks = 0;
    private int coinsCollected = 0;
    private static final int HALLSIZE = 2;
    private char playertilechar = '6';
    public TETile[][] grid;
    private Random r = new Random();
    private ArrayList<Room> roomList;
    private boolean gettingSeed = false;
    private boolean awaitingQ = false;
    private boolean changingChar;
    public boolean ready = false;
    public boolean inEnc = false;
    private boolean loading = false;
    private String seed = "";
    private int[] playerCoords;
    private String ioString = "";
    private HashSet<Character> playerMovesTracker = new HashSet<>();
    private Map<TETile, Character> tiles = Map.of(Tileset.NOTHING, '0', Tileset.AVATAR, '1', Tileset.FLOOR, '2', Tileset.WALL, '3', Tileset.FLOWER, '4', Tileset.TREE, '5', Tileset.WATER, '6', Tileset.RUG, '7');
    private Map<Character, TETile> tileLoad = Map.of('0', Tileset.NOTHING, '1', Tileset.AVATAR, '2', Tileset.FLOOR, '3', Tileset.WALL, '4', Tileset.FLOWER, '5', Tileset.TREE, '6', Tileset.WATER, '7', Tileset.RUG);
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
            int choice = r.nextInt(3);
            type = switch (choice) {
                case 0, 1 -> "encounter";
                default -> "generic";
            };
        }

        private int[] addPlayer(TETile[][] target) { //don't understand
            //type = "generic";
            int[] coords = new int[2];
            coords[0] = x + w / 2;
            coords[1] = y + h /2;
            target[coords[0]][coords[1]] = tileLoad.get(playertilechar);
            return coords;
        }

        private void decorateRoom(TETile[][] target) {
            if (type.equals("encounter")) {
                int encX = r.nextInt(x + 1, x + w);
                int encY = r.nextInt(y + 1, y + h);
                target[encX][encY] = Tileset.RUG;
            } else if (type.equals("coins")) {
                for (int i = 0; i < COINNUM; i++) {
                    int encX = r.nextInt(x + 1, x + w);
                    int encY = r.nextInt(y + 1, y + h);
                    target[encX][encY] = Tileset.AVATAR;
                }
            }
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
                //if (b == HEIGHT) {
                    //grid[a][b] = Tileset.WALL;
                //} else {
                    grid[a][b] = Tileset.NOTHING;
               // }
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
        roomList.get(0).type = "generic";
        playerCoords = roomList.get(0).addPlayer(grid);
        for (Room r : roomList) {
            r.decorateRoom(grid);
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
            return new Room(x - origin.x - origin.w, HALLSIZE, origin.x + origin.w - HALLSIZE, r.nextInt(origin.y, origin.y + origin.h - HALLSIZE));
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

    public String handleCommand(char c) {
        if (!loading) {
            ioString += Character.toString(c);
        }
        if ((c == 'q' || c == 'Q') && awaitingQ) {
            saveandquit();
            return "quit";
        } else if (c == 'S' || c == 's') {
            gettingSeed = false;
            if (seed.length() > 0) {
                ready = true;
                r = new Random(Long.valueOf(seed));
                fillArray();
            }
            seed = "";
        } else if (awaitingQ) {
            awaitingQ = false;
            if (c == 'l' || c == 'L') {
                ready = true;
                load();
            } else if (c == 'N' || c == 'n') {
                gettingSeed = true;
            } else if (c == 'C' || c == 'c') {
                changingChar = true;
            }
        } else if (gettingSeed) {
            seed = seed + Character.toString(c);
            return seed;
        } else if (c == ':' && !inEnc) {
            awaitingQ = true;
        } else if (c == 'N' || c == 'n') {
            gettingSeed = true;
        } else if (c == 'l' || c == 'L') {
            load();
        } else if (c == 'f' || c == 'F') {
            playertilechar = '4';
        } else if (c == 't' || c == 'T') {
            playertilechar = '5';
        } else if (c == 'v' || c == 'V') {
            playertilechar = '6';
        }
        playerMove(c);
        return "done";
    }

    private void saveandquit() {
        try {
            FileWriter f = new FileWriter("out/production/proj3/save.txt");
            /*for (int a = 0; a <= HEIGHT; a++) {
                for (int b = 0; b < WIDTH; b++) {
                    f.write(tiles.get(grid[b][a]));
                }
                f.write(System.getProperty("line.separator"));
            }*/
            f.write(ioString);
            f.write(System.lineSeparator());
            f.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load() {
        loading = true;
        In in = new In("out/production/proj3/save.txt");
        ioString = in.readString();
        char[] row = ioString.toCharArray();
        for (int i = 0; i < row.length; i++) {
            handleCommand(row[i]);
        }
        loading = false;
        /*for (int a = 0; a < HEIGHT - 5; a++) {
            char[] row = in.readString().toCharArray();
            for (int b = 0; b < WIDTH; b++) {
                //if (tileLoad.get(row[b]).equals(tileLoad.get(playertilechar))) {
                if (tileLoad.get(row[b]) == tileLoad.get('4')) {
                    playerCoords[0] = b;
                    playerCoords[1] = a;
                    playertilechar = '4';
                } else if (tileLoad.get(row[b]) == tileLoad.get('5')) {
                    playerCoords[0] = b;
                    playerCoords[1] = a;
                    playertilechar = '5';
                } else if (tileLoad.get(row[b]) == tileLoad.get('6')) {
                    playerCoords[0] = b;
                    playerCoords[1] = a;
                    playertilechar = '6';
                }
                grid[b][a] = tileLoad.get(row[b]);
            }
        }*/
    }

    private void playerMove(char c) {
        if (c == 'w') {
            if (grid[playerCoords[0]][playerCoords[1] + 1].equals(Tileset.FLOOR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[1]++;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
            } else if (grid[playerCoords[0]][playerCoords[1] + 1].equals(Tileset.RUG)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[1]++;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                encounter();
            } else if (grid[playerCoords[0]][playerCoords[1] + 1].equals(Tileset.AVATAR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[1]++;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                coinsCollected++;
            }
        } else if (c == 'a') {
            if (grid[playerCoords[0] - 1][playerCoords[1]].equals(Tileset.FLOOR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[0]--;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
            } else if (grid[playerCoords[0] - 1][playerCoords[1]].equals(Tileset.RUG)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[0]--;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                encounter();
            } else if (grid[playerCoords[0] - 1][playerCoords[1]].equals(Tileset.AVATAR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[0]--;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                coinsCollected++;
            }
        } else if (c == 's') {
            if (grid[playerCoords[0]][playerCoords[1] - 1].equals(Tileset.FLOOR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[1]--;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
            } else if (grid[playerCoords[0]][playerCoords[1] - 1].equals(Tileset.RUG)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[1]--;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                encounter();
            } else if (grid[playerCoords[0]][playerCoords[1] - 1].equals(Tileset.AVATAR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[1]--;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                coinsCollected++;
            }
        } else if (c == 'd'){
            if (grid[playerCoords[0] + 1][playerCoords[1]].equals(Tileset.FLOOR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[0]++;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
            } else if (grid[playerCoords[0] + 1][playerCoords[1]].equals(Tileset.RUG)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[0]++;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                encounter();
            } else if (grid[playerCoords[0] + 1][playerCoords[1]].equals(Tileset.AVATAR)) {
                grid[playerCoords[0]][playerCoords[1]] = Tileset.FLOOR;
                playerCoords[0]++;
                grid[playerCoords[0]][playerCoords[1]] = tileLoad.get(playertilechar);
                coinsCollected++;
            }
        }

    }

    public void encounter() {
        inEnc = true;
        ioString = ioString.substring(0, ioString.length() - 1);
        saveandquit();
        sysTicks = System.currentTimeMillis();

        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "You have 10 seconds to collect the coins.");
        //StdDraw.show(1);

        for (int a = 0; a < WIDTH; a++) {
            for (int b = 0; b <= HEIGHT; b++) {
                //if (b == HEIGHT) {
                    //grid[a][b] = Tileset.WALL;
                //} else {
                    grid[a][b] = Tileset.NOTHING;
                //}
            }
        }

        Room r = new Room(20, 10, 30, 20);
        r.drawRoom(grid);
        r.type = "coins";
        r.decorateRoom(grid);
        playerCoords = r.addPlayer(grid);

        //HUD(StdDraw.mouseX(), StdDraw.mouseY());
    }

    public void encWinLose() {
        if (coinsCollected == COINNUM) {
            load();
            inEnc = false;
            coinsCollected = 0;
            timer = 10;
        } else {
            ready = false;
        }
    }

    public void mainMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(font);
        if (!inEnc) {
            if (gettingSeed) {
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Enter a random seed! (S) when finished");
                StdDraw.text(WIDTH / 2, HEIGHT / 2, seed);
            } else if (changingChar) {
                //StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Project 3");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Choose an Avatar:");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "Flower (F)");
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Tree (T)");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Water (V)");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "Press N to Continue");

            } else {
                awaitingQ = true;
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 10, "Project 3");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 + 5, "New Game (N)");
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Load Game (L)");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 5, "Quit (:Q)");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 10, "Change Avatar (C)");
            }
        }
        StdDraw.show();
    }

    public void HUD (double x, double y) {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 15);
        StdDraw.setFont(font);
        if (x < WIDTH && y < HEIGHT) {
            TETile tileHolder = grid[(int) x][(int) y];
            if (inEnc) {
                StdDraw.textLeft(1, HEIGHT - 1, Long.toString(timer));
            } else if (tileHolder == Tileset.FLOOR) {
                StdDraw.textLeft(1, HEIGHT + 4, "Floor");
            } else if (tileHolder == Tileset.WALL) {
                StdDraw.textLeft(1, HEIGHT + 4, "Wall");
            } else if (tileHolder == Tileset.NOTHING) {
                StdDraw.textLeft(1, HEIGHT + 4, "Nothing");
            } else if (tileHolder == Tileset.RUG) {
                StdDraw.textLeft(1, HEIGHT + 4, "Encounter");
            } else {
                StdDraw.textLeft(1, HEIGHT + 4, "Player");
            }
        } else {
            StdDraw.textLeft(1, HEIGHT + 4, "Nothing");
        }
        StdDraw.show();
    }

    public static void main(String[] args) {

    }

}
