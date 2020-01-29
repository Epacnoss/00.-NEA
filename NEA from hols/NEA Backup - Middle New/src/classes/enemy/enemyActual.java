package classes.enemy;

import classes.Entity.Entity;
import classes.Entity.entityType;
import classes.square.squareCollection;
import classes.square.types.Square;
import classes.util.Coordinate;
import classes.util.dir;
import main.main;

public class enemyActual extends Entity {

    private int distPerFrame;

    private enemyTemplate template;

    private int currentHP, currentSpd;
    private boolean isDead;

    private boolean hasHit;

    private squareCollection squares;
    private int currentStep;
    private Coordinate currentCoord;

    private Thread runThread;

    public enemyActual(enemyTemplate eTemplate, squareCollection squares) {
        super(squares.getStart(), eTemplate.getFn(), entityType.enemy, new Coordinate(main.TILE_WIDTH / 2 - main.ENEMY_WIDTH / 2, 5));

        template = eTemplate;
        currentHP = template.getHp();

        currentSpd = template.getSpd();

        currentStep = 0;
        currentCoord = squares.getEnemyPath().get(currentStep);

        this.squares = squares;

        distPerFrame = 0;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                changePosInTile(new Coordinate(getXYInTile().getX() + 1, getXYInTile().getY()));
            }
        };

        runThread = new Thread(r);
        runThread.start();
    }

    @Override
    public void step (long msSinceLast) {
        if(currentStep == squares.getEnemyPath().size())
            hasHit = true;

        if(hasHit)
            return;

        double FramesPerSec = (msSinceLast / 1000.0) * currentSpd;

        currentCoord = squares.getEnemyPath().get(currentStep);

        dir direction = getXYInArr().directionTo(currentCoord);
        System.out.println(direction + " - " + getXYInArr().toString() + " -> " + currentCoord.toString() + "\t\t" + currentStep);

        Coordinate XYInArrNu = new Coordinate(0, 0);
        Coordinate XYInIndividualTileNu = new Coordinate(0, 0);
        int distInPx;

        int av = ((main.TILE_WIDTH * main.TILE_HEIGHT) / 2) / 200;

        switch (direction) {
            case N:
                distInPx = ((int) Math.floor(FramesPerSec * av));

                XYInIndividualTileNu = new Coordinate(getXYInTile().getX(), getXYInTile().getY() - distInPx);
                XYInArrNu = getXYInArr().clone();

                if (XYInIndividualTileNu.getY() < 0) {
                    XYInArrNu = new Coordinate(XYInArrNu.getX(), XYInArrNu.getY() - 1);
                    int overflow = (main.TILE_HEIGHT + XYInIndividualTileNu.getY() < 0 ? //So if we get any nuts values, it is fine
                            XYInIndividualTileNu.getY() - main.TILE_HEIGHT :
                            main.TILE_HEIGHT); //Edge case...
                    XYInIndividualTileNu = new Coordinate(XYInIndividualTileNu.getX(), overflow);
                }

                break;
            case S:
                distInPx = ((int) Math.floor(FramesPerSec * av));

                XYInIndividualTileNu = new Coordinate(getXYInTile().getX(), getXYInTile().getY() + distInPx);
                XYInArrNu = getXYInArr().clone();

                if (XYInIndividualTileNu.getY() > main.TILE_HEIGHT) {
                    XYInArrNu = new Coordinate(XYInArrNu.getX(), XYInArrNu.getY() + 1);
                    int overflow = (XYInIndividualTileNu.getY() - main.TILE_HEIGHT * 2 < 0 ? //So if we get any nuts values, it is fine
                            XYInIndividualTileNu.getY() - main.TILE_HEIGHT :
                            0);
                    XYInIndividualTileNu = new Coordinate(XYInIndividualTileNu.getX(), overflow);
                }

                break;
            case E:
                distInPx = ((int) Math.floor(FramesPerSec * av));

                XYInIndividualTileNu = new Coordinate(getXYInTile().getX() + distInPx, getXYInTile().getY());
                XYInArrNu = getXYInArr().clone();

                if (XYInIndividualTileNu.getX() > main.TILE_WIDTH) {
                    XYInArrNu = new Coordinate(XYInArrNu.getX() + 1, XYInArrNu.getY());
                    int overflow = (XYInIndividualTileNu.getX() - main.TILE_WIDTH * 2 < 0 ? //So if we get any nuts values, it is fine
                            XYInIndividualTileNu.getX() - main.TILE_WIDTH :
                            0);
                    XYInIndividualTileNu = new Coordinate(overflow, XYInIndividualTileNu.getY());

                }

                break;
            case W:
                distInPx = ((int) Math.floor(FramesPerSec * av));

                XYInIndividualTileNu = new Coordinate(getXYInTile().getX() - distInPx, getXYInTile().getY());
                XYInArrNu = getXYInArr().clone();

                if (XYInIndividualTileNu.getX() < 0) {
                    XYInArrNu = new Coordinate(XYInArrNu.getX() - 1, XYInArrNu.getY());
                    int overflow = ((main.TILE_WIDTH * 2 - XYInIndividualTileNu.getX() < 0) ? //So if we get any nuts values, it is fine
                            (main.TILE_WIDTH - XYInIndividualTileNu.getX()) :
                            (main.TILE_WIDTH));
                    XYInIndividualTileNu = new Coordinate(overflow, XYInIndividualTileNu.getY());
                }
                break;

        }

        changeTile(XYInArrNu);
        changePosInTile(XYInIndividualTileNu);

        if(XYInArrNu.equals(currentCoord) && XYInIndividualTileNu.isWithinBounds(main.BOUND, new Coordinate(main.TILE_WIDTH / 2, main.TILE_HEIGHT / 2), direction))
            currentStep++;
    }

    public boolean isHasHit() {
        return hasHit;
    }

///    public void incrementStep () {
///        currentStep++;
///    }
///
///    public Coordinate getTarget () {
///        return currentCoord;
///    }

    public void damage (int dmg) {
        currentHP -= dmg;
    }
}