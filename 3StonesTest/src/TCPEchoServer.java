
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import three_stone.logic.Game;

/**
 * Responsible for listen on port 50000, connecting and maintaining 
 * a connection with 1 or more clients. Also responsible for sending and 
 * receiving messages to/from the client(s).
 * 
 * @author derek
 */
public class TCPEchoServer {

    private static final int BUFSIZE = 32;	// Size of receive buffer
    private static final int HUMAN = 1;
    private static final int ROBOT = 2;
    private static final int ERROR = 3;
    private static final int WIN = 6; //user win
    private static final int LOSS = 7; //user loss
    private static final int DRAW = 8;
    private static final int QUIT = 5;
    private static final int NEWGAME = 4;

    /**
     * Responsible for creating and managing a game of Three Stones. As well
     * as communicating with clients.
     * 
     * @param args
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        Game game = new Game();
        int servPort = 50000;
        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);
        int recvMsgSize;  // Size of received message
        byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer

        for (;;) {
            game.reset(); 
            Socket clntSock = servSock.accept();	// Get client connection

            System.out.println("Handling client at "
                    + clntSock.getInetAddress().getHostAddress() + " on port "
                    + clntSock.getPort());

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            while ((recvMsgSize = in.read(byteBuffer)) != -1) { //This represents a game

                int recvInts[] = convertBytesToIntArrays(byteBuffer);
                
                int move[] = new int[5];
                if(game.isValidMove(new int[]{recvInts[1],recvInts[2]})){ //validates user move
                    game.humanMove(new int[]{recvInts[1],recvInts[2]});
                    move = game.robotMove();
                    int sendCode = handleValidMove(game);
                    out.write(convertMoveToByteArray(move, sendCode, game),0,recvMsgSize);
                    if(sendCode > 6){game.reset();} // is this necessary
                } else {
//                    send back error code as move
                    move = new int[] {3,0,1,0,0}; //code for invalid move
                    out.write(convertMoveToByteArray(move, ERROR, game),0,recvMsgSize);
                }
                
                //System.out.println("received: " + recvInts[0] + " " + recvInts[1] + " " + recvInts[2]);
                //System.out.println("sending: " + move[0] + " " + move[1]);
            }
        }
    }

    /**
     * Converts int array into a byte array that will be sent to the client.
     * 
     * @param move 
     * @param sendCode is the kind of message 1 or 2 is a move, 3 is error, 6
     *      is a win for client, etc.
     * @param game current instance of the game, used to retrieve scores.
     * @return Message intended for client.
     */
    public static byte[] convertMoveToByteArray(int[] move, int sendCode, Game game){
        int codedArray[] = new int[5];
        codedArray[0] = sendCode;
        codedArray[1] = move[0];
        codedArray[2] = move[1];
        codedArray[3] = game.getScore()[0];
        codedArray[4] = game.getScore()[1];
        return convertIntToByteArrays(codedArray);
    }
    
    /**
     * Convert an integer array into a byte array which can be sent to the 
     * client.
     * 
     * @param move is the server's move on the game board, can also be used to
     *      indicate or server intentions.
     * @return byte array that the server will receive.
     */
    public static byte[] convertIntToByteArrays(int[] move) {
        byte[] bytes = new byte[5];
        for (int i = 0; i < 5; i++) {
            bytes[i] = (byte) move[i];
        }
        return bytes;
    }

     /**
     * Convert a byte array into an int array
     * 
     * @param bytes an array that is a coded message from the client.
     * @return an integer array.
     */
    public static int[] convertBytesToIntArrays(byte[] bytes) {
        int[] move = new int[5];
        for (int i = 0; i < 5; i++) {
            move[i] = bytes[i] & 0xFF; //converts to integer
        }
        return move;
    }
    
    /**
     * Using the provided score, this method determine the final result of the 
     * game. 
     * @param robotScore
     * @param humanScore
     * @return integer representing the result
     */
    public static int determineVictor(int robotScore, int humanScore){
        if (robotScore > humanScore) {
            return LOSS;
        } else if (robotScore < humanScore) {
            return WIN;
        } else {
            return DRAW;
        }
    }
    /**
     * Handles user's move given that it is a valid move.
     * 
     * @param game instance of current game
     * @return an int that is the message code used to indicate how the client
     *      should handle the message.
     */
    public static int handleValidMove(Game game){
        if (game.isGameOver()) {
            System.out.print("Game is over");
            int matchResult = determineVictor(game.getScore()[0], game.getScore()[1]);
            return matchResult;         
        } else {
            return ROBOT;
        }
    }
}
