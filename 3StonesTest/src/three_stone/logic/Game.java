package three_stone.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import three_stones.views.Board;

/**
 * Game of 3 stones. Contains game board, representing the playing surface as 
 * well as 2 players.
 * 
 * @author Derek McLean
 */
public class Game {
    private final int HUMAN = 1;
    private final int ROBOT = 2;
    private Board board;
    private Random random;
    private int turn;
    
    public Game(){
        board = new Board();
        random = new Random();
        this.turn = 0;
    }
    
    /**
     * Makes a move for the server.
     * @return 
     */
    public int[] robotMove(){
        int move[] = getBestMove();
        board.updateBoard(ROBOT, move);
        board.toString();
        return move;
    }
    
    /**
     * Used to update the board after a client move.
     * 
     * @param position The client's move.
     */
    public void humanMove(int[] position){
        board.updateBoard(HUMAN, position);
    }
    
    /**
     * Displays the current state of the game board.
     */
    public void printState(){
        System.out.println(board.toString());
    }
    
    /** Gets all available moves that are in the same axes as the previous move made.
     * 
     * @return List contain possible moves.
     */
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
    
    /**
     * Determines if a selected moves if allowed. 
     * 
     * @param move
     * @return 
     */
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
        boolean over = availableMoves.isEmpty();
        if (turn >= 30) {
            over = true;
        }
        return over;
    }
    
    /**
     * Resets the game to its initial state.
     */
    public void reset() {
        board.reset();
        turn = 0;
    }
    
    /**
     * Gets the current score.
     * 
     * @return  return an array representing the score. Item at index 0 is the 
     * server's score and item at index 1 is the client score.
     */
    public int[] getScore(){
        return new int[]{board.getServerScore(), board.getPlayerScore()};
    }
    
    /**
     * Chooses the best move for the server. Highest priority is block client 
     * from scoring, if that is not possible then server will pick the move that 
     * will gain the most points possible. If no points are possible then it 
     * will choose a move that will create a line of 2 adjacent tiles, setting 
     * itself up to get points in a later move. If that is also not possible
     * then it will simple choose a random move from available moves.
     * @return 
     */
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
    
    /**
     * Gets the move that will garner the most points for a specified player.
     * 
     * @param player Player whose turn it is.
     * @return A randomly selected move, selected from the most rewarding moves,
     *      if no point are available then returns null.
     */
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
    
    /**
     * Method is used if there is no way the server can gain point or block client
     * from gaining point. It searches among available moves for a spot where it
     * can create a line of 2-in-a-row, thus setting up future opportunities for
     * itself.
     * 
     * @return a randomly select move that will create a line of 2 tiles. 
     * returns null if not possible.
     */
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
    
    /**
     * Used to determine if any of the surrounding tiles are owned by the server.
     * 
     * @param move tile that is being tested. 
     * @return boolean indicating if one of the adjacent tiles is own by the
     *      server
     */
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

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
    
    
    
}
