package artaudal.morpion.database;

import java.io.Serializable;

public class Joueur implements Serializable {

    private String pseudo;
    private String image;
    private int victory;
    private int defeat;
    private int draw;


    public Joueur(){

    }
    public Joueur(String pseudo, String image, int victory, int defeat, int draw) {
        this.pseudo = pseudo;
        this.image = image;
        this.victory = victory;
        this.defeat = defeat;
        this.draw = draw;
    }


    /*
     * Getters and Setters
     * */

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public int getVictory() {
        return victory;
    }

    public void setVictory(int victory) {
        this.victory = victory;
    }

    public int getDefeat() {
        return defeat;
    }

    public void setDefeat(int defeat) {
        this.defeat = defeat;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
