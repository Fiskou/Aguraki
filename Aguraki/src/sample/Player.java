package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player {

    final Image EXPLOSION_IMG = new Image("images/explosion.png");
    final int EXPLOSION_W = 128;
    final int EXPLOSION_ROWS = 3;
    final int EXPLOSION_COL = 3;
    final int EXPLOSION_H = 128;
    final int EXPLOSION_STEPS = 15;

    int posX, posY, size;
    boolean exploding, destroyed;
    Image img;
    int explosionStep = 0;

    /**
     * Constructeur du joueur
     * @param posX position X
     * @param posY position Y
     * @param size taille
     * @param image image du joueur
     */
    public Player(int posX, int posY, int size, Image image) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        img = image;
    }

    /**
     * Setter position X
     * @param posX position X de base
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @return position X
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @return Un tir du joueur à partir de sa position
     */
    public Shot shoot() {
        return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
    }

    /**
     * Mise à jour du joueur (vie / mort)
     */
    public void update() {
        if(exploding)
            explosionStep++;
        destroyed = explosionStep > EXPLOSION_STEPS;
    }

    /**
     * Dessine le joueur dans l'interface
     * @param gc interface graphique (canvas)
     */
    public void draw(GraphicsContext gc) {
        if(exploding) {
            gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, (explosionStep / (EXPLOSION_ROWS * 1.0)) * EXPLOSION_H + 1,
                    EXPLOSION_W, EXPLOSION_H,
                    posX, posY, size, size);
        }
        else {
            gc.drawImage(img, posX, posY, size, size);
        }
    }

    /**
     * HitBox du joueur
     * @param other Ennemi récupéré pour avoir sa position et sa taille
     * @return Si oui le joueur est touché sinon l'ennemi passe à côté.
     */
    public boolean colide(Player other) {
        int d = Aguraki.distance(this.posX + size / 2, this.posY + size /2,
                other.posX + other.size / 2, other.posY + other.size / 2);
        return d < other.size / 2 + this.size / 2 ;
    }

    /**
     * Déclanche l'explosion du joueur
     */
    public void explode() {
        exploding = true;
        explosionStep = -1;
    }
}
