package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static sample.Aguraki.score;

public class Shot {
    public boolean toRemove;

    int posX, posY, speed = 10;
    static final int size = 6;

    /**
     * Constructeur d'un tir
     * @param posX position X
     * @param posY position Y
     */
    public Shot(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * Déplacement du tir dans l'interface graphique par rapport à la vitesse
     */
    public void update() {
        posY-=speed;
    }

    /**
     * Dessine le tir dans l'interface
     * @param gc interface graphique (canvas)
     */
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        if (score >=50 && score<=70 || score>=120) {
            gc.setFill(Color.YELLOWGREEN);
            speed = 50;
            gc.fillRect(posX-5, posY-10, size+10, size+30);
        } else {
            gc.fillOval(posX, posY, size, size);
        }
    }

    /**
     * HitBox du tir
     * @param Player Joueur ou ennemi, pour récuperer sa position et sa taille
     * @return Si oui le tir a touché le joueur ou l'ennemi sinon le tir passe à côté.
     */
    public boolean colide(Player Player) {
        int distance = Aguraki.distance(this.posX + size / 2, this.posY + size / 2,
                Player.posX + Player.size / 2, Player.posY + Player.size / 2);
        return distance  < Player.size / 2 + size / 2;
    }
}
