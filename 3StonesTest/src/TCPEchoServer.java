
import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream
import three_stone.logic.Game;

public class TCPEchoServer {

    private static final int BUFSIZE = 32;	// Size of receive buffer

    public static void main(String[] args) throws IOException {

        Game game = new Game();
        if (args.length != 1) // Test for correct # of args
        {
            throw new IllegalArgumentException("Parameter(s): <Port>");
        }

        int servPort = Integer.parseInt(args[0]);

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        int messageCount = 0;
        int recvMsgSize;						// Size of received message
        byte[] byteBuffer = new byte[BUFSIZE];	// Receive buffer

        // Run forever, accepting and servicing connections
        for (;;) {
            Socket clntSock = servSock.accept();	// Get client connection

            System.out.println("Handling client at "
                    + clntSock.getInetAddress().getHostAddress() + " on port "
                    + clntSock.getPort());

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
            int move[] = game.robotMove();
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
//                out.write(byteBuffer, 0, recvMsgSize);
                out.write(convertMoveToByteArray(game.robotMove()),0,recvMsgSize);
            }
            int recvInts[] = convertBytesToIntArrays(byteBuffer);
            System.out.print("received: " + recvInts[1] + " " + recvInts[2]);
            System.out.print("sending: " + move[0] + " " + move[1]);
            
            clntSock.close();						// Close the socket. This client is finished.
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
