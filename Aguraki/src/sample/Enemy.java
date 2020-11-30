package sample;

import javafx.scene.image.Image;

import static sample.Aguraki.HEIGHT;
import static sample.Aguraki.score;

public class Enemy extends Player {
    int SPEED = (score/5)+2;

    /**
     * Constructeur d'ennemi
     * @param posX position X
     * @param posY position Y
     * @param size taille
     * @param image image de l'ennemi
     */
    public Enemy(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }

    /**
     * Mise à niveau de l'ennemi (vie / mort)
     */
    public void update() {
        super.update();
        if(!exploding && !destroyed)
            posY += SPEED;
        if(posY > HEIGHT)
            destroyed = true;
    }
}
