package sample;

import javafx.scene.image.Image;

public class Enemy extends Player {
    int SPEED = (score/5)+2;

    public Enemy(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }

    public void update() {
        super.update();
        if(!exploding && !destroyed) posY += SPEED;
        if(posY > HEIGHT) destroyed = true;
    }
}
