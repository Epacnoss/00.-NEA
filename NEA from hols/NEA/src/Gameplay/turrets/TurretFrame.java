package Gameplay.turrets;

import Gameplay.player.PlayerManager;
import classes.Entity.Entity;
import classes.turret.turretActual;
import classes.turret.turretTemplate;
import classes.util.Coordinate;
import main.main;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class TurretFrame {

    private Coordinate mostRecent;
    private String mostRecentType;

    private JFrame window;
    private JPanel panel;

    private ArrayList<JButton> btns;
    private ArrayList<Coordinate> usedSquares;
    private ArrayList<Coordinate> freeSquares;
    private int currentIndex;

    public static final Coordinate NULL_COORD = new Coordinate(100000, 10000);
    public static final String NULL_STR = "NOPE!";


    private Icon messageIcn; //TODO: Fix this

    private ArrayList<Entity> turretActuals;

    public TurretFrame(ArrayList<Coordinate> usedSquares, ArrayList<Coordinate> freeSquares, Dimension size, Collection<turretTemplate> templates_collection, PlayerManager pm) {
        currentIndex = 0;
        try {
            URL url = new URL(main.ICON_LOCATIONS + "XYIcon.png");

            if(url == null)
                throw new MalformedURLException("No URL found...");

            messageIcn = new ImageIcon(url, "Icon showing X and Y with crosshairs circle.");
        }catch (Exception e) {
            messageIcn = new ImageIcon();
        }

        ArrayList<turretTemplate> turrets = new ArrayList<>();
        turrets.addAll(templates_collection);

        this.usedSquares = usedSquares;
        this.freeSquares = freeSquares;

        window = new JFrame("Apex Turrets - Turret Window");
        window.setLocation(size.width, 0);
        window.setPreferredSize(size);
        window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        panel = new JPanel();
        panel.setAutoscrolls(true);

        turretActuals = new ArrayList<>();


        btns = new ArrayList<>();
        panel.setLayout(new GridLayout(turrets.size() + 2, 1));

        for (int i = 0; i < turrets.size(); i++) {
            turretTemplate tt = turrets.get(i);
            String name = tt.getName();


            JButton sBtn = new JButton("Buy " + name);
            btns.add(sBtn);
            panel.add(sBtn);

            sBtn.addActionListener(e -> {

                if(freeSquares.size() == 0)
                {
                    mostRecent = NULL_COORD;

                    JOptionPane.showMessageDialog(panel, "Unfortunately, there are no turret spaces left. Good luck!", "No free space.", JOptionPane.ERROR_MESSAGE, messageIcn);

                    return;
                }

                int result = JOptionPane.showConfirmDialog(panel, tt.toString(), "Confirm buy Turret: " + tt.getName(), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, messageIcn);

                if(JOptionPane.YES_OPTION == result) {
                    Object location = JOptionPane.showInputDialog(panel, "Please enter a location", "Where would you like your tower?", JOptionPane.QUESTION_MESSAGE, messageIcn, ((Object[]) freeSquares.toArray()), 0);

                    String resInStr = location + "";

                    if(resInStr == null)
                        return;


                    mostRecent = Coordinate.parseFromTS(resInStr);
                    mostRecentType = sBtn.getText().substring(4); //TODO: Add selling capability
                }
            });
        }

        JButton sellBtn = new JButton("Sell tower?");
        sellBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(panel, "Sell turret?", "Do you want to sell a turret - Beware, you may not get back the full investment.", JOptionPane.OK_CANCEL_OPTION);

            if(result == JOptionPane.OK_OPTION) {
                ArrayList<String> sellableTowers = new ArrayList<>();
                ArrayList<turretActual> turretsThatGoWith = new ArrayList<>();
                for (Coordinate coordinate : usedSquares) {
                    turretActual ta = null;
                    String fin;

                    try {
                        for(Entity entity : turretActuals) {
                            if(entity.getXYInArr().equals(coordinate))
                            {
                                ta = ((turretActual) entity);
                                break;
                            }
                        }

                        if(ta == null)
                            throw new TurretNotFoundException();

                        fin = coordinate.toString() + " - " + ta.getTurret().getSellValue();
                        turretsThatGoWith.add(ta);

                    } catch (TurretNotFoundException ex) {
                        fin = "idk";
                    }
                    sellableTowers.add(fin);
                }

                Object location = JOptionPane.showInputDialog(panel, "Please enter a location", "Which Tower would you like to sell. Resale values and coordinates listed.", JOptionPane.QUESTION_MESSAGE, messageIcn, ((Object[]) sellableTowers.toArray()), 0);
                String strVersion = location.toString();

                //region getting turret
                int endTSIndex = strVersion.indexOf(']');

                String indexStr = strVersion.substring(endTSIndex + 4);
                int index = Integer.parseInt(indexStr);

                turretActual turretToSell = turretsThatGoWith.get(index);
                //endregion

                //region getting coord
                int endCoordIndex = strVersion.indexOf(']');
                Coordinate coordThatGoesWith = Coordinate.parseFromTS(strVersion.substring(0, endCoordIndex));
                //endregion

                usedSquares.remove(coordThatGoesWith);
                freeSquares.add(coordThatGoesWith);
                turretActuals.remove(turretToSell);

                pm.donateM(turretToSell.getTurret().getSellValue());

            }
        });

        JTextArea label = new JTextArea(getLabel(pm));
        label.setEditable(false);

        panel.add(sellBtn);
        panel.add(label);


        window.add(panel);

        window.pack();
        window.setVisible(true);

        pm.addBooleanChangeListener(e -> {
            label.setText(getLabel(pm));
            window.pack();
        });

    }

    private static String getLabel (PlayerManager pm) {
        return "Money: " + pm.getMoney() + "\nHearts remaining: " + pm.getHearts();
    }

    public Coordinate getMostRecent() {
        if(mostRecent != null)
            return mostRecent;
        else
            return NULL_COORD;
    }
    public String getMostRecentType() {
        if(mostRecentType != null && mostRecentType != "")
            return mostRecentType;
        else
            return NULL_STR;
    }

    public void setTurrets (ArrayList<Entity> turrets) {
        turretActuals = turrets;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
    public void incrementIndex () {
        currentIndex++;
    }
}
