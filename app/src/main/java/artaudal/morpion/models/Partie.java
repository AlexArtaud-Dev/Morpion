package artaudal.morpion.models;

public class Partie {

    private int result = -1; // -1 si la partie est en cours, 0 si la partie est un match null, 1 si le joueurs 1 est gagnat et 2 si le joueur 2
    private int playerID;
    private int numberMove = 9;
    private char[][] gameboard;
    private int horizontal = 3;
    private int vertical = 3;
    private Integer[] lastPlayerMoveAgainstIA;

    public Partie(Integer horizontal, Integer vertical, int playerID){
        this.playerID = playerID;
        lastPlayerMoveAgainstIA= new Integer[2];
        if (horizontal != null && vertical != null) {
            this.gameboard = new char[horizontal][vertical];
            this.numberMove = vertical * horizontal;
            for (int i = 0; i< horizontal; i++){
                for (int j = 0; j< vertical; j++){
                    this.gameboard[i][j] = ' ';
                }
            }
        }else{
            this.gameboard = new char[this.horizontal][this.vertical];
        }

        if (horizontal != null) {
            this.horizontal = horizontal;
        }
        if (vertical != null) {
            this.vertical = vertical;
        }
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getNumberMove() {
        return numberMove;
    }

    public void setNumberMove(int numberMove) {
        this.numberMove = numberMove;
    }

    public char[][] getGameboard() {
        return gameboard;
    }

    public void setGameboard(char[][] gameboard) {
        this.gameboard = gameboard;
    }

    public int getHorizontal() {
        return horizontal;
    }

    public int getVertical() {
        return vertical;
    }

    public boolean isOccupied(int horizontal, int vertical){
        char[][] gameboard = this.getGameboard();
        char gameboardValue = gameboard[horizontal][vertical];
        boolean occupied = false;
        if (gameboardValue != ' '){
            occupied = true;
        }
        return occupied;
    }

    public char getChar(int horizontal, int vertical){
        char[][] gameboard = this.getGameboard();
        char gameboardValue = gameboard[horizontal][vertical];
        return gameboardValue;
    }

    public int play(int horizontal, int vertical){
        // Check if the number of move are not going after the total of move possible
        if(getNumberMove() > 0){
            // Mark in the gameboard a character corresponding to the playing player (X (player1) or O (player2))
            if(this.playerID%2 != 0){
                gameboard[horizontal][vertical] = 'X';
            }else{
                gameboard[horizontal][vertical] = 'O';
            }

            // Make minus 1 the number of move
            setNumberMove(--numberMove);

            if(isFinished(horizontal, vertical)) {
                setResult(playerID);
            }
            else if(getNumberMove()==0) {
                setResult(0);
            }
            else {
                if(this.playerID%2 != 0) {
                    setPlayerID(2);
                }
                else {
                    setPlayerID(1);
                }
            }
        }
        return getResult();
    }

    public int playerPlayAgainstIA (int horizontal, int vertical){
        if(getNumberMove() > 0){
            gameboard[horizontal][vertical] = 'O';
            setNumberMove(--numberMove);

            if(isFinished(horizontal, vertical)) {
                setResult(2);
            }
            else if(getNumberMove()==0) {
                setResult(0);
            }
            else {
                setPlayerID(1);
            }
        }
        lastPlayerMoveAgainstIA[0] = horizontal;
        lastPlayerMoveAgainstIA[1] = vertical;
        return getResult();
    }

    public int IAPlayAgainstPlayer (int horizontal, int vertical){
        if(getNumberMove() > 0){
            gameboard[horizontal][vertical] = 'X';
            setNumberMove(--numberMove);

            if(isFinished(horizontal, vertical)) {
                setResult(1);
            }
            else if(getNumberMove()==0) {
                setResult(0);
            }
            else {
                setPlayerID(2);
            }
        }
        return getResult();
    }

    public Integer[] getLastPlayerMoveAgainstIA(){
        return this.lastPlayerMoveAgainstIA;
    }

    public void reset(){
            this.numberMove = 9;
            setPlayerID(1);
            setResult(-1);
            for(int i=0; i<gameboard.length;i++) {
                for(int j=0; j<gameboard.length;j++) {
                    gameboard[i][j] = '\0';
                }
            }
    }

    public boolean isFinished(int horizontal, int vertical){
        int countRow = 0;
        int countCol = 0;
        int countLDiag = 0;
        int countRDiag = 0;
        char character;

        if(getPlayerID() %2 !=0){
            character = 'X';
        } else{
            character = 'O';
        }


        for(int i=0; i<gameboard.length;i++) {
            if(gameboard[horizontal][i] == character)
                countRow++;
            if(gameboard[i][vertical] == character)
                countCol++;
            if(gameboard[i][i] == character)
                countRDiag++;
            if(gameboard[gameboard.length-1-i][i] == character)
                countLDiag++;
        }

        if(countCol==gameboard.length || countRow==gameboard.length
                || countLDiag == gameboard.length || countRDiag == gameboard.length)
            return true;

        return false;
    }

}
