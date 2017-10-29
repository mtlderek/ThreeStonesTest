
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import three_stone.logic.Game;

//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes>java TCPEchoServer 4455
//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes
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

    public static byte[] convertMoveToByteArray(int[] move, int sendCode, Game game){
        int codedArray[] = new int[5];
        codedArray[0] = sendCode;
        codedArray[1] = move[0];
        codedArray[2] = move[1];
        codedArray[3] = game.getScore()[0];
        codedArray[4] = game.getScore()[1];
        return convertIntToByteArrays(codedArray);
    }
    
    public static byte[] convertIntToByteArrays(int[] move) {
        byte[] bytes = new byte[5];
        for (int i = 0; i < 5; i++) {
            bytes[i] = (byte) move[i];
        }
        return bytes;
    }

    public static int[] convertBytesToIntArrays(byte[] bytes) {
        int[] move = new int[5];
        for (int i = 0; i < 5; i++) {
            move[i] = bytes[i] & 0xFF; //converts to integer
        }
        return move;
    }
    
    public static int determineVictor(int robotScore, int humanScore){
        if (robotScore > humanScore) {
            return LOSS;
        } else if (robotScore < humanScore) {
            return WIN;
        } else {
            return DRAW;
        }
    }
    
    public static int handleValidMove(Game game){
        if (game.isGameOver()) {
            System.out.print("Game is over");
            int matchResult = determineVictor(game.getScore()[0], game.getScore()[1]);
//            game.reset();
            return matchResult;         
        } else {
            return ROBOT;
        }
    }
}
