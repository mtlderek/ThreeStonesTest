
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import three_stone.logic.Game;

//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes>java TCPEchoServer 4455
//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes
public class TCPEchoServer {

    private static final int BUFSIZE = 32;	// Size of receive buffer

    public static void main(String[] args) throws IOException {
        
//        sendCodes
        int HUMAN = 1;
        int ROBOT = 2;
        int ERROR = 3;
        int WIN = 6; //user win
        int LOSS = 7; //user loss
        int DRAW = 8;
        int QUIT = 5;
        int NEWGAME = 4;
        Game game = new Game();
        int servPort = 50000;
        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        int messageCount = 0;
        int recvMsgSize;  // Size of received message
        byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer

        // Run forever, accepting and servicing connections
        int closecounter = 0;
//         Socket clntSock = servSock.accept();	// Get client connection
        for (;;) {
            Socket clntSock = servSock.accept();	// Get client connection

            System.out.println("Handling client at "
                    + clntSock.getInetAddress().getHostAddress() + " on port "
                    + clntSock.getPort());

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
//          int move[] = game.robotMove();
            
            while ((recvMsgSize = in.read(byteBuffer)) != -1) { //This represents a game
//                out.write(byteBuffer, 0, recvMsgSize);

                int recvInts[] = convertBytesToIntArrays(byteBuffer);
                //before this next line we need to validate.
                int move[] = new int[5];
                if (recvInts[0] == 4) { //user selects new game
                    game.reset();
                    move = new int[] {NEWGAME,0,1};
                } else if(game.isValidMove(new int[]{recvInts[1],recvInts[2]})){ //validates user move
                    game.humanMove(new int[]{recvInts[1],recvInts[2]});
                    move = game.robotMove();
                    //check here if game is over
                    boolean gameOver = game.isGameOver();
                    if(gameOver){
                        System.out.print("Game is over");
                        if(game.getScore()[0] > game.getScore()[1] ){
                            // ROBOT WINS
                            out.write(convertMoveToByteArray(move, LOSS, game),0,recvMsgSize);                            
                        } else if (game.getScore()[1] > game.getScore()[0] ) {
                            // HUMAN WINS
                            out.write(convertMoveToByteArray(move, WIN, game),0,recvMsgSize);
                        } else {
                            //DRAW
                            out.write(convertMoveToByteArray(move, DRAW, game),0,recvMsgSize);
                        }
//                        out.write(convertMoveToByteArray(move, GAMEOVER, game),0,recvMsgSize);
                        game.reset();
//                      
                    } else {
                        out.write(convertMoveToByteArray(move, ROBOT, game),0,recvMsgSize);
                    }
//                    out.write(convertMoveToByteArray(move, ROBOT),0,recvMsgSize);
                } else {
//                    send back error code as move
                    move = new int[] {3,0,1,0,0}; //code for invalid move
                    out.write(convertMoveToByteArray(move, ERROR, game),0,recvMsgSize);
                }
              
//                game.humanMove(new int[]{recvInts[1],recvInts[2]});
//                move[] = game.robotMove();
                
                System.out.println("received: " + recvInts[1] + " " + recvInts[2]);
                System.out.println("sending: " + move[0] + " " + move[1]);
            }
            closecounter ++;
            if(closecounter == 30){
                clntSock.close();
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

}
