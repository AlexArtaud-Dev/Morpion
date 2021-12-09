package artaudal.morpion.models;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApp extends Application {

    private String playerNameAgainstIA;
    private ComputerLevel compLevel;
    private String playerNameOne;
    private String playerNameTwo;
    private String playerOneBase64Image;
    private String playerTwoBase64Image;
    private Bitmap playerOneBitmapImage;
    private Bitmap playerTwoBitmapImage;
    private String playerNameWin;
    private String playerNameLoose;
    private int sizeOfGame;
    private int typeOfGame;
    private int gameResult; // 1 si victoire - 0 si match nul

    @Override
    public void onCreate(){
        super.onCreate();
    }


    public String getPlayerNameAgainstIA() {
        return playerNameAgainstIA;
    }

    public void setPlayerNameAgainstIA(String playerNameAgainstIA) {
        this.playerNameAgainstIA = playerNameAgainstIA;
    }

    public String getPlayerNameOne() {
        return playerNameOne;
    }

    public void setPlayerNameOne(String playerNameOne) {
        this.playerNameOne = playerNameOne;
    }

    public String getPlayerNameTwo() {
        return playerNameTwo;
    }

    public void setPlayerNameTwo(String playerNameTwo) {
        this.playerNameTwo = playerNameTwo;
    }

    public String getPlayerOneBase64Image() {
        return playerOneBase64Image;
    }

    public void setPlayerOneBase64Image(String playerOneBase64Image) {
        this.playerOneBase64Image = playerOneBase64Image;
    }

    public String getPlayerTwoBase64Image() {
        return playerTwoBase64Image;
    }

    public void setPlayerTwoBase64Image(String playerTwoBase64Image) {
        this.playerTwoBase64Image = playerTwoBase64Image;
    }

    public String getPlayerNameWin() {
        return playerNameWin;
    }

    public void setPlayerNameWin(String playerNameWin) {
        this.playerNameWin = playerNameWin;
    }

    public String getPlayerNameLoose() {
        return playerNameLoose;
    }

    public void setPlayerNameLoose(String playerNameLoose) {
        this.playerNameLoose = playerNameLoose;
    }

    public int getGameResult() {
        return gameResult;
    }

    public ComputerLevel getCompLevel() {
        return compLevel;
    }

    public void setCompLevel(ComputerLevel compLevel) {
        this.compLevel = compLevel;
    }


    public void setGameResult(int gameResult) {
        this.gameResult = gameResult;
    }

    public Bitmap getPlayerOneBitmapImage() {
        return playerOneBitmapImage;
    }

    public void setPlayerOneBitmapImage(Bitmap playerOneBitmapImage) {
        this.playerOneBitmapImage = playerOneBitmapImage;
    }

    public Bitmap getPlayerTwoBitmapImage() {
        return playerTwoBitmapImage;
    }

    public void setPlayerTwoBitmapImage(Bitmap playerTwoBitmapImage) {
        this.playerTwoBitmapImage = playerTwoBitmapImage;
    }

    public int getTypeOfGame() {
        return typeOfGame;
    }

    public void setTypeOfGame(int typeOfGame) {
        this.typeOfGame = typeOfGame;
    }

    public int getSizeOfGame() {
        return sizeOfGame;
    }

    public void setSizeOfGame(int sizeOfGame) {
        this.sizeOfGame = sizeOfGame;
    }

}
