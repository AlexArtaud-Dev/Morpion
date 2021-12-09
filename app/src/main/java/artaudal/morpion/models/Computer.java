package artaudal.morpion.models;

import java.util.Random;

public class Computer {

    private char botChar = 'X';
    private Partie game;
    private ComputerLevel level;
    private Integer[] previousMove;
    private boolean isFirstToPlay;
    private Integer[] positionToPlay;
    private Integer[] firstPlayerMove;
    private boolean gotFirstMove = false;
    private int checkType = -1;

    public Computer(Partie game){
        this.game = game;
        previousMove = new Integer[2];
        firstPlayerMove = new Integer[2];
        previousMove[0] = 1;
        previousMove[1] = 1;
        isFirstToPlay = true;
    }

    private Integer[] getBestMove(){
        Integer[] previousPlayerMoves = new Integer[2];
        Integer[] bestMove = new Integer[2];
        if (!gotFirstMove){
            firstPlayerMove[0] = game.getLastPlayerMoveAgainstIA()[0];
            firstPlayerMove[1] = game.getLastPlayerMoveAgainstIA()[1];
            gotFirstMove = true;
        }
        if ((game.isOccupied(1,0) || game.isOccupied(2,1) || game.isOccupied(1,2) || game.isOccupied(0,1)) && checkType == -1){
            checkType = 0;
        }
        if ((game.isOccupied(0,0) || game.isOccupied(2,2) || game.isOccupied(0,2) || game.isOccupied(2,0)) && checkType == -1){
            checkType = 1;
        }

        if (checkType == 0){
            if (game.isOccupied(1,0) || game.isOccupied(2,1) || game.isOccupied(1,2) || game.isOccupied(0,1)) {
                // Si le joueur n'a pas fait un move dans les angles en tant que premier move
                previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];

                // Si le joueur riposte en placant en 1-0
                if (firstPlayerMove[0] == 1 && firstPlayerMove[1] == 0) {
                    if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(0, 0)) {
                        bestMove[0] = 0;
                        bestMove[1] = 0;
                    } else {
                        previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                        previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                        if ((previousPlayerMoves[0] != 2 || previousPlayerMoves[1] != 2) && !game.isOccupied(2, 2)) {
                            bestMove[0] = 2;
                            bestMove[1] = 2;
                        } else {
                            if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 2 && !game.isOccupied(0, 2)) {
                                bestMove[0] = 0;
                                bestMove[1] = 2;
                            } else {
                                previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 1 && !game.isOccupied(2, 0)) {
                                    bestMove[0] = 2;
                                    bestMove[1] = 0;
                                } else {
                                    if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 0 && !game.isOccupied(0, 1)) {
                                        bestMove[0] = 0;
                                        bestMove[1] = 1;
                                    } else {
                                        bestMove[0] = 2;
                                        bestMove[1] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                // Si le joueur riposte en placant en 0-1
                if (firstPlayerMove[0] == 0 && firstPlayerMove[1] == 1) {
                    if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(0, 2)) {
                        bestMove[0] = 0;
                        bestMove[1] = 2;
                    } else {
                        previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                        previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                        if ((previousPlayerMoves[0] != 2 || previousPlayerMoves[1] != 0) && !game.isOccupied(2, 0)) {
                            bestMove[0] = 2;
                            bestMove[1] = 0;
                        } else {
                            if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 0 && !game.isOccupied(2, 2)) {
                                bestMove[0] = 2;
                                bestMove[1] = 2;
                            } else {
                                previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 2 && !game.isOccupied(0, 0)) {
                                    bestMove[0] = 0;
                                    bestMove[1] = 0;
                                } else {
                                    if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 0 && !game.isOccupied(1, 2)) {
                                        bestMove[0] = 1;
                                        bestMove[1] = 2;
                                    } else {
                                        bestMove[0] = 0;
                                        bestMove[1] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
                // Si le joueur riposte en placant en 2-1
                if (firstPlayerMove[0] == 2 && firstPlayerMove[1] == 1) {
                    if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(2, 0)) {
                        bestMove[0] = 2;
                        bestMove[1] = 0;
                    } else {
                        previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                        previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                        if ((previousPlayerMoves[0] != 0 || previousPlayerMoves[1] != 2) && !game.isOccupied(0, 2)) {
                            bestMove[0] = 0;
                            bestMove[1] = 2;
                        } else {
                            if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 2 && !game.isOccupied(0, 0)) {
                                bestMove[0] = 0;
                                bestMove[1] = 0;
                            } else {
                                previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 0 && !game.isOccupied(2, 2)) {
                                    bestMove[0] = 2;
                                    bestMove[1] = 2;
                                } else {
                                    if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 2 && !game.isOccupied(1, 0)) {
                                        bestMove[0] = 1;
                                        bestMove[1] = 0;
                                    } else {
                                        bestMove[0] = 2;
                                        bestMove[1] = 2;
                                    }
                                }
                            }
                        }
                    }
                }
                // Si le joueur riposte en placant en 1-2
                if (firstPlayerMove[0] == 1 && firstPlayerMove[1] == 2) {
                    if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(2, 2)) {
                        bestMove[0] = 2;
                        bestMove[1] = 2;
                    } else {
                        previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                        previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                        if ((previousPlayerMoves[0] != 0 || previousPlayerMoves[1] != 0) && !game.isOccupied(0, 0)) {
                            bestMove[0] = 0;
                            bestMove[1] = 0;
                        } else {
                            if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 0 && !game.isOccupied(2, 0)) {
                                bestMove[0] = 2;
                                bestMove[1] = 0;
                            } else {
                                previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 1 && !game.isOccupied(0, 2)) {
                                    bestMove[0] = 0;
                                    bestMove[1] = 2;
                                } else {
                                    if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 2 && !game.isOccupied(2, 1)) {
                                        bestMove[0] = 2;
                                        bestMove[1] = 1;
                                    } else {
                                        bestMove[0] = 0;
                                        bestMove[1] = 2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }else{
            // Si le joueur a fait un move dans les angles en tant que premier move
            previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
            previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];

            // Si le premier move du joueur est l'angle 0-0
            if (firstPlayerMove[0] == 0 && firstPlayerMove[1] == 0){
                if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(0,1)){
                    bestMove[0] = 0;
                    bestMove[1] = 1;
                }else{
                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                    if (previousPlayerMoves[0] != 2 && previousPlayerMoves[1] != 1 && !game.isOccupied(2,1)){
                        bestMove[0] = 2;
                        bestMove[1] = 1;
                    }else{
                        if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 1 && !game.isOccupied(2,0)){
                            bestMove[0] = 2;
                            bestMove[1] = 0;
                        }else{
                            previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                            previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                            if ((previousPlayerMoves[0] != 0 || previousPlayerMoves[1] != 2) && !game.isOccupied(0,2)){
                                bestMove[0] = 0;
                                bestMove[1] = 2;
                            }else{
                                if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 2 && !game.isOccupied(2,2)){
                                    bestMove[0] = 2;
                                    bestMove[1] = 2;
                                }else{
                                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                    if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 0 && !game.isOccupied(1,2)){
                                        bestMove[0] = 1;
                                        bestMove[1] = 2;
                                    }else{
                                        if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 2 && !game.isOccupied(1,0)){
                                            bestMove[0] = 1;
                                            bestMove[1] = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Si le premier move du joueur est l'angle 2-0
            if (firstPlayerMove[0] == 2 && firstPlayerMove[1] == 0){
                if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(2,1)){
                    bestMove[0] = 2;
                    bestMove[1] = 1;
                }else{
                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                    if (previousPlayerMoves[0] != 0 && previousPlayerMoves[1] != 1 && !game.isOccupied(0,1)){
                        bestMove[0] = 0;
                        bestMove[1] = 1;
                    }else{
                        if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 1 && !game.isOccupied(0,0)){
                            bestMove[0] = 0;
                            bestMove[1] = 0;
                        }else{
                            previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                            previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                            if ((previousPlayerMoves[0] != 0 || previousPlayerMoves[1] != 2) && !game.isOccupied(0,2)){
                                bestMove[0] = 0;
                                bestMove[1] = 2;
                            }else{
                                if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 2 && !game.isOccupied(0,2)){
                                    bestMove[0] = 0;
                                    bestMove[1] = 2;
                                }else{
                                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                    if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 0 && !game.isOccupied(1,2)){
                                        bestMove[0] = 1;
                                        bestMove[1] = 2;
                                    }else{
                                        if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 2 && !game.isOccupied(1,0)){
                                            bestMove[0] = 1;
                                            bestMove[1] = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Si le premier move du joueur est l'angle 2-2
            if (firstPlayerMove[0] == 2 && firstPlayerMove[1] == 2){
                if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(2,1)){
                    bestMove[0] = 2;
                    bestMove[1] = 1;
                }else{
                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                    if (previousPlayerMoves[0] != 0 && previousPlayerMoves[1] != 1 && !game.isOccupied(0,1)){
                        bestMove[0] = 0;
                        bestMove[1] = 1;
                    }else{
                        if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 1 && !game.isOccupied(0,2)){
                            bestMove[0] = 0;
                            bestMove[1] = 2;
                        }else{
                            previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                            previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                            if ((previousPlayerMoves[0] != 2 || previousPlayerMoves[1] != 0) && !game.isOccupied(2,0)){
                                bestMove[0] = 2;
                                bestMove[1] = 0;
                            }else{
                                if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 0 && !game.isOccupied(0,0)){
                                    bestMove[0] = 0;
                                    bestMove[1] = 0;
                                }else{
                                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                    if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 0 && !game.isOccupied(1,2)){
                                        bestMove[0] = 1;
                                        bestMove[1] = 2;
                                    }else{
                                        if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 2 && !game.isOccupied(1,0)){
                                            bestMove[0] = 1;
                                            bestMove[1] = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Si le premier move du joueur est l'angle 0-2
            if (firstPlayerMove[0] == 0 && firstPlayerMove[1] == 2){
                if (previousMove[0] == 1 && previousMove[1] == 1 && !game.isOccupied(0,1)){
                    bestMove[0] = 0;
                    bestMove[1] = 1;
                }else{
                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                    if (previousPlayerMoves[0] != 2 && previousPlayerMoves[1] != 1 && !game.isOccupied(2,1)){
                        bestMove[0] = 2;
                        bestMove[1] = 1;
                    }else{
                        if (previousPlayerMoves[0] == 2 && previousPlayerMoves[1] == 1 && !game.isOccupied(2,2)){
                            bestMove[0] = 2;
                            bestMove[1] = 2;
                        }else{
                            previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                            previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                            if ((previousPlayerMoves[0] != 0 || previousPlayerMoves[1] != 0) && !game.isOccupied(0,0)){
                                bestMove[0] = 0;
                                bestMove[1] = 0;
                            }else{
                                if (previousPlayerMoves[0] == 0 && previousPlayerMoves[1] == 0 && !game.isOccupied(2,0)){
                                    bestMove[0] = 2;
                                    bestMove[1] = 0;
                                }else{
                                    previousPlayerMoves[0] = game.getLastPlayerMoveAgainstIA()[0];
                                    previousPlayerMoves[1] = game.getLastPlayerMoveAgainstIA()[1];
                                    if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 0 && !game.isOccupied(1,2)){
                                        bestMove[0] = 1;
                                        bestMove[1] = 2;
                                    }else{
                                        if (previousPlayerMoves[0] == 1 && previousPlayerMoves[1] == 2 && !game.isOccupied(1,0)){
                                            bestMove[0] = 1;
                                            bestMove[1] = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }



       // System.out.println("Best Move Bot : " + bestMove[0] + "/" + bestMove[1]);
        return bestMove;
    }

    private Integer[] getRandomAvailableMove(){
        Integer[] move = new Integer[2];
        Random r = new Random();
        boolean moveAvailable = false;


        while (moveAvailable == false){
            move[0] = (r.nextInt(4-1) + 1) - 1;
            move[1] = (r.nextInt(4-1) + 1) - 1;
            if (!game.isOccupied(move[0], move[1])){
                moveAvailable = true;
            }
        }
        return move;
    }

    public int playComputerMove(){
        int horizontal;
        int vertical;
        int result;
        if (!isFirstToPlay){
            positionToPlay = getBestMove();
            horizontal = positionToPlay[0];
            vertical = positionToPlay[1];
            result = game.IAPlayAgainstPlayer(horizontal, vertical);
            previousMove[0] = positionToPlay[0];
            previousMove[1] = positionToPlay[1];
        }else{
            result = game.IAPlayAgainstPlayer(1, 1);
            previousMove[0] = 1;
            previousMove[1] = 1;
            isFirstToPlay = false;
        }
        return result;
    }

    public int playRandomComputerMove(){
        int horizontal;
        int vertical;
        int result;
        positionToPlay = getRandomAvailableMove();
        horizontal = positionToPlay[0];
        vertical = positionToPlay[1];
        result = game.IAPlayAgainstPlayer(horizontal, vertical);
        previousMove[0] = positionToPlay[0];
        previousMove[1] = positionToPlay[1];
        return result;
    }

    public Integer[] getPreviousMove(){
        return previousMove;
    }

    public ComputerLevel getLevel() {
        return level;
    }

    public void setLevel(ComputerLevel level) {
        this.level = level;
    }

}
