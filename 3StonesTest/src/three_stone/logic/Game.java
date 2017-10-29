/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package three_stone.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import three_stones.views.Board;

/**
 *
 * @author derek
 */
public class Game {
    private final int HUMAN = 1;
    private final int ROBOT = 2;
    private Board board;
    private Random random;
    
    public Game(){
        board = new Board();
        random = new Random();
    }
    
    public int[] robotMove(){
//        List<int[]> moves = new ArrayList<int[]>();
//        moves = getAvailableMoves();
//        int selection = random.nextInt(moves.size());
//        board.updateBoard(ROBOT, moves.get(selection));
////        return moves.get(selection);
        int move[] = getBestMove();
        board.updateBoard(ROBOT, move);
        board.toString();
        System.out.print("does this print?");
        return move;
    }
    
    public void humanMove(int[] position){
        board.updateBoard(HUMAN, position);
    }
    
    public void printState(){
        System.out.println(board.toString());
    }
    
    public List<int[]> getAvailableMoves(){
        //TODO: determine best moves by points and blockages.
        List<int[]> moves = board.getAvailableMoves();
        List<int[]> movesInAxis = new ArrayList<int[]>();
        int[] lastPlayed = board.getLastPlayed();
      
        for(int[] potentialMove : moves){
            if (potentialMove[0] == lastPlayed[0]){ //check for matching Y
               movesInAxis.add(potentialMove);
            }
            if (potentialMove[1] == lastPlayed[1]){ //check for matchingX
               movesInAxis.add(potentialMove);
            }
        }
        
        if (movesInAxis.isEmpty()) {
            return moves;  
        }
        return movesInAxis;
    }
    
    public boolean isValidMove(int[] move){
        List<int[]> availableMoves = getAvailableMoves();
        for(int[] i : availableMoves){
//            System.out.println("" + i[0] + "," + i[1]);
            if( i[0] == move[0] && i[1] == move[1] ){
                return true;
            }
        }
        return false;
    }
    
    /**
     * If there are no available moves then all possible moves have been exhausted.
     * game is over.
     * 
     * @return Boolean indicating whether the game is over or not.
     */
    public boolean isGameOver(){
        List<int[]> availableMoves = getAvailableMoves();
        return availableMoves.isEmpty();
    }
    
    public void reset() {
        board.reset();
    }
    
    public int[] getScore(){
        return new int[]{board.getServerScore(), board.getPlayerScore()};
    }
    
    public int[] getBestMove(){

        

        int bestPointMove[] = getMoveWithMostPoints(ROBOT);
        int bestBlockMove[] = getMoveWithMostPoints(HUMAN);
        int bestPairingMove[] = getBestPairingMove(); 
        int chosenMove[];

        
        if(bestBlockMove == null){
            if(bestPointMove == null){
                if(bestPairingMove == null){
                    List<int[]> moves = getAvailableMoves();
                    int moveIndex = random.nextInt(moves.size());
                    chosenMove = moves.get(moveIndex);
                    System.out.println("Using Random");
                } else {
                    System.out.println("Using Pairing");
                    chosenMove = bestPairingMove;
                }
            } else {
                chosenMove = bestPointMove;
                System.out.println("Using Best point move");
            }
        } else {
            chosenMove = bestBlockMove;
            System.out.println("Using Blocking");
        }
        

        
        return chosenMove;
//        return bestPointMove;
    } 
    
    public int[] getMoveWithMostPoints(int player){
        int currentPlayerHolder = board.getPlayer();
        int currentLastPlayedHolder[] = board.getLastPlayed();
        //alter board so we can get the variosu scores for all hypothecial moves
        int highestPointValue = 0;
        board.setPlayer(player);
        
        
        List<int[]> possibleMoves = getAvailableMoves();
        int pointsToBeGained[] = new int[possibleMoves.size()];
        int counter = 0;
        for (int[] move : possibleMoves){
//            board.setLastPlayed(new int[] {move[1], move[0]});
            board.setLastPlayed(new int[] {move[1], move[0]});
            pointsToBeGained[counter] = board.checkScore(move[1], move[0]);
            counter++;
        }
        
        //get highest point value
        for (int i= 0 ; i < pointsToBeGained.length; i++) {
            if(pointsToBeGained[i] > highestPointValue) {
                highestPointValue = pointsToBeGained[i];
            }
        }
        
        //Make array of highest values
        List<int[]> highestValuedMoves = new ArrayList<int[]>();
        for (int j = 0; j < pointsToBeGained.length; j++){
            if (pointsToBeGained[j] == highestPointValue){
                highestValuedMoves.add(possibleMoves.get(j));
            }
        }
        board.setPlayer(currentPlayerHolder);
        board.setLastPlayed(currentLastPlayedHolder);
        if(highestPointValue == 0){ // none of the moves earned points
            return null;
        }
        
        //Now choose randomly among them
        int chosenMoveIndex = random.nextInt(highestValuedMoves.size());
        
        return highestValuedMoves.get(chosenMoveIndex);
    }
    
    public int[] getBestPairingMove(){
        
        
        List<int[]> possibleMoves = getAvailableMoves();
        List<int[]> movesWithNeighboringROBOT = new ArrayList<>();
        
        for(int move[] : possibleMoves){
            if (hasNeighbouringROBOT(move)){
                movesWithNeighboringROBOT.add(move);
            }
        }
        
        
        if(movesWithNeighboringROBOT.isEmpty()){
            return null;
        } else {
            int chosenMoveIndex = random.nextInt(movesWithNeighboringROBOT.size());
            return movesWithNeighboringROBOT.get(chosenMoveIndex);
        }
        
    }
    
    public boolean hasNeighbouringROBOT(int[] move){
//        System.out.println("TEST: Game.hasNeighbouringROBOT: " + move[0] + " " + move[1]);
        if (board.getValueAt(move[0]-1, move[1]-1) == 2){ return true;} //top left
        if (board.getValueAt(move[0], move[1]-1) == 2){ return true;}   // top center
        if (board.getValueAt(move[0]+1, move[1]-1) == 2){ return true;} // top right
        if (board.getValueAt(move[0]-1, move[1]) == 2){ return true;}  // middle left
        if (board.getValueAt(move[0]+1, move[1]) == 2){ return true;}  // midle right
        if (board.getValueAt(move[0]-1, move[1]+1)  == 2){ return true;}// bottom left
        if (board.getValueAt(move[0], move[1]+1)  == 2){ return true;}  // bottom center
        if (board.getValueAt(move[0]+1, move[1]+1)  == 2){ return true;} // bottom right
        return false;
    }
    
}
