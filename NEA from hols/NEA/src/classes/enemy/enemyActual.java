package classes.enemy;

import classes.Entity.Entity;
import main.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

public class enemyActual extends Entity {

    private enemyTemplate template;

    private int currentHP, currentSpd;
    private BufferedImage img;

    public enemyActual(enemyTemplate eTemplate) {
        int offsetY = main.TILE_HEIGHT / 2;
        int offsetX = main.TILE_WIDTH / 2;

        template = eTemplate;
        currentHP = template.getHp();
        currentSpd = template.getSpd();

        URL url;
        img = new BufferedImage(main.TILE_WIDTH, main.TILE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        try {

            url = new URL(main.ENEMY_IMAGES_LOC + eTemplate.getFn());
            URLConnection con = url.openConnection();
            Image temp = ImageIO.read(con.getInputStream()).getScaledInstance(main.TILE_WIDTH, main.TILE_HEIGHT, Image.SCALE_SMOOTH);
            img.getGraphics().drawImage(temp, 0, 0, null);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}