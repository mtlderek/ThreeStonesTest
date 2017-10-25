
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import three_stone.logic.Game;

//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes>java TCPEchoServer 4455
//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes
public class TCPEchoServer {

    private static final int BUFSIZE = 32;	// Size of receive buffer

    public static void main(String[] args) throws IOException {

        Game game = new Game();
//        if (args.length != 1) // Test for correct # of args
//        {
//            throw new IllegalArgumentException("Parameter(s): <Port>");
//        }
//
////        int servPort = Integer.parseInt(args[0]);
        int servPort = 50000;

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        int messageCount = 0;
        int recvMsgSize;						// Size of received message
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
            
            while ((recvMsgSize = in.read(byteBuffer)) != -1) { //previously -1
//                out.write(byteBuffer, 0, recvMsgSize);
                
                
                int recvInts[] = convertBytesToIntArrays(byteBuffer);
                game.humanMove(new int[]{recvInts[1],recvInts[2]});
                int move[] = game.robotMove();
                out.write(convertMoveToByteArray(move),0,recvMsgSize);
                System.out.println("received: " + recvInts[1] + " " + recvInts[2]);
                System.out.println("sending: " + move[0] + " " + move[1]);
            }
            closecounter ++;
//            System.out.println("Counter = " + counter);
            if(closecounter == 30){
                clntSock.close();
            }
//            clntSock.close();						// Close the socket. This client is finished.
//      byteBuffer = null;
        }
//    System.out.print("received: " + new String(byteBuffer));

        /* NOT REACHED */
    }

    public static byte[] convertMoveToByteArray(int[] move){
        int codedArray[] = new int[3];
        codedArray[0] = 1;
        codedArray[1] = move[0];
        codedArray[2] = move[1];
        return convertIntToByteArrays(codedArray);
    }
    public static byte[] convertIntToByteArrays(int[] move) {
        byte[] bytes = new byte[3];
        for (int i = 0; i < 3; i++) {
            bytes[i] = (byte) move[i];
        }
        return bytes;
    }

    public static int[] convertBytesToIntArrays(byte[] bytes) {
        int[] move = new int[3];
        for (int i = 0; i < 3; i++) {
            move[i] = bytes[i] & 0xFF; //converts to integer
        }
        return move;
    }
}
