package main;

import CfgReader.CfgReader;
import Gameplay.waves.waveManager;
import classes.Entity.Entity;
import classes.canvas;
import classes.square.squareCollection;
import classes.util.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class main {

    //TODO: Go onto the git and add more data

    //For ease of file locations and packages, I have uploaded all images to a public github repo, the URL of which can be found as BASE_LOCATION on line 10.

    //For ease of file locations and packages, I have uploaded all images to a public github repo, the URL of which can be found as BASE_LOCATION.

    //region URL locations
    private static final String BASE_LOCATION = "https://raw.githubusercontent.com/Epacnoss/NEAAssets/master/Actual/";
    private static final String IMAGES_LOC = BASE_LOCATION + "images/";

    public static final String ENEMIES_LOC = BASE_LOCATION + "enemies/";
    public static final String TURRETS_LOC = BASE_LOCATION + "turrets/";
    public static final String WAVES_LOC = BASE_LOCATION + "waves/";
    public static final String MAPS_LOC = BASE_LOCATION + "maps/";

    public static final String ENEMY_IMAGES_LOC = IMAGES_LOC + "enemies/";
    public static final String TURRET_IMAGES_LOC = IMAGES_LOC + "turrets/";
    public static final String PATHS_IMAGES_LOC = IMAGES_LOC + "paths/";
    public static final String BULLET_IMAGE_LOC = IMAGES_LOC + "bullets/";
    //endregion

    //region UI sizes
    public static int CURRENT_LEVEL = 1;
    private static final CfgReader stage;
    public static final int NUM_OF_TILES_WIDTH;
    public static final int NUM_OF_TILES_HEIGHT;

    public static final int WINDOW_WIDTH = 600; //px
    public static final int WINDOW_HEIGHT = 630; //px

    private static Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

    public static final int TOP_JPANEL_HEIGHT = 0; //px
    public static final int REST_OF_FRAME_HEIGHT = WINDOW_HEIGHT - TOP_JPANEL_HEIGHT; //px

    public static final int TILE_HEIGHT; //px
    public static final int TILE_WIDTH;

    public static final int BOUND;

    //region widths and heights of entities
    public static final int TURRET_WIDTH;
    public static final int TURRET_HEIGHT;

    public static final int BULLET_WIDTH;
    public static final int BULLET_HEIGHT;

    public static final int ENEMY_WIDTH;
    public static final int ENEMY_HEIGHT;
    //endregion

    public static final int TURRET_X_ON_TILE;
    public static final int TURRET_Y_ON_TILE;

    //endregion

    //region fn arrays

    public static final String[] TURRET_FNS = {"wizard.cfg", "dropTower.cfg"};
    public static final String[] ENEMY_FNS = {"fastButWeak.cfg", "slowButStrong.cfg"};

    public static final String[] TURRET_IMG_FNS = {"dropper_big.png", "sorcerer_big.png"};
    public static final String[] ENEMY_IMG_FNS = {"skeleton_big.png", "bigButSlow_big.png"};

    //endregion

    public static void lvl1() {
        //region main window
        canvas c = new canvas(CURRENT_LEVEL);

        JFrame window = new JFrame("Apex Turrets");

        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.setPreferredSize(size);
        window.add(c);
        window.pack();
        window.setVisible(true);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        waveManager waves = new waveManager("lvl1.cfg", "stg1.cfg");

        Dimension newSize = new Dimension(main.TILE_WIDTH * main.NUM_OF_TILES_WIDTH, main.TILE_HEIGHT * main.NUM_OF_TILES_HEIGHT);
        window.setSize(newSize);
        //endregion

//        JFrame turretWin = new JFrame("Turret Window");
//        JButton[][] buttons = new JButton[NUM_OF_TILES_WIDTH][NUM_OF_TILES_HEIGHT];
//        GridLayout gl = new GridLayout(NUM_OF_TILES_WIDTH, NUM_OF_TILES_HEIGHT);
//        turretWin.setLayout(gl);
//        turretWin.setPreferredSize(newSize);
//
//        squareCollection squares = c.getSquares();
//
//        for (int j = 0; j < NUM_OF_TILES_HEIGHT; j++) {
//            for (int i = 0; i < NUM_OF_TILES_WIDTH; i++) {
//                boolean isTurretSquare = squares.getAvailableTurretSquares().contains(new Coordinate(i, j));
//                String txt = "";
//
//                if(isTurretSquare)
//                    txt = "Turret Here fam";
//
//                buttons[i][j] = new JButton(txt);
//                buttons[i][j].setPreferredSize(new Dimension(TILE_WIDTH, TILE_HEIGHT));
//                turretWin.add(buttons[i][j]);
//            }
//        }
//
//        turretWin.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        turretWin.pack();
//        turretWin.setVisible(true);

        long current = System.currentTimeMillis();

        long delay = 10;

        while (true) {

            c.render();

            while (!c.isFinishedRendering()) {
                System.out.println("Doing de good render");
                continue;
            }


            window.pack();

            if (System.currentTimeMillis() - current > delay) {


                ArrayList<Entity> enemyActuals = waves.step(System.currentTimeMillis() - current);

                c.setEntities(enemyActuals);

                current = System.currentTimeMillis();
            }
        }
    }

    public static void main(String[] args) {
        lvl1();
    }

    static {
        stage = new CfgReader(MAPS_LOC + "stg" + CURRENT_LEVEL + ".cfg");
        NUM_OF_TILES_WIDTH = Integer.parseInt(stage.get("mapDeets", "rows").toString());
        NUM_OF_TILES_HEIGHT = Integer.parseInt(stage.get("mapDeets", "cols").toString());

        TILE_WIDTH = WINDOW_WIDTH / NUM_OF_TILES_WIDTH;
        TILE_HEIGHT = WINDOW_HEIGHT / NUM_OF_TILES_HEIGHT;

        BOUND = (TILE_WIDTH + TILE_HEIGHT) / 2;


        TURRET_WIDTH = TILE_WIDTH / 3 * 2;
        TURRET_HEIGHT = TILE_HEIGHT / 3 * 2;

        BULLET_WIDTH = TILE_WIDTH / 10;
        BULLET_HEIGHT = TILE_HEIGHT / 10;

        ENEMY_WIDTH = TILE_WIDTH * 3 / 2;
        ENEMY_HEIGHT = TILE_HEIGHT;

        TURRET_X_ON_TILE = 5;
        TURRET_Y_ON_TILE = TILE_HEIGHT - 5;
    }
}
