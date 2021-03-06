package classes.square;

import CfgReader.CfgReader;
import classes.util.Coordinate;
import main.main;

import java.security.UnrecoverableEntryException;
import java.util.HashMap;

public class sqaureParser {

    private Square[][] squares;
    private HashMap<Character, String> fns;
    private CfgReader r;
    private Coordinate satan;
    private Coordinate home;

    public sqaureParser(CfgReader r) {
        this.r = r;

        int w = main.NUM_OF_TILES_WIDTH;
        int h = main.NUM_OF_TILES_HEIGHT;
        fns = new HashMap<>();
        setUpHashMap();

        char[] tbp = r.get("mapDeets", "map").toString().toCharArray();
        squares = new Square[w][h];


        int x = 0;
        int y = 0;

        for (char c : tbp) {
            String fn = fns.get(c);
            Square newBoi;

            Coordinate ici = new Coordinate(x, y);

            switch (fn) {
                case "general_big.png":
                    newBoi = new pathSquare(ici);
                    break;
                case "happy_big.png":
                    newBoi = new homeBase(ici);
                    home = ici;
                    break;
                case "satan_big.png":
                    newBoi = new enemyStartSquare(ici);
                    satan = ici;
                    break;
                case "turret_base_big.png":
                    newBoi = new turretSquare(ici);
                    break;
                default:
                    newBoi = new cornerSquare(fn, ici);
                    break;
            }

            squares[x][y] = newBoi;

            x++;

            if(w == x)
            {
                x = 0;
                y++;
            }
        }
    }

    private void setUpHashMap () {
        HashMap<String, Object> mod = r.getModule("refs");

        Object[] chars = mod.keySet().toArray();
        Object[] fnsFromR = mod.values().toArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i].toString().charAt(0);
            String fn = fnsFromR[i].toString();

            fns.put(c, fn);
        }
    }

    public Square[][] getSquares() {
        return squares;
    }
}
