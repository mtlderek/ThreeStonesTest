
import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner;

public class TCPEchoClient {

    public static void main(String[] args) throws IOException {
        int counter = 0;
        Scanner kb = new Scanner(System.in);
        verifyCorrectNumOfArgs(args);
        String server = args[0]; // Server name or IP address

        int servPort = (args.length == 2) ? Integer.parseInt(args[1]) : 7; //default port = 7?

        // Create socket that is connected to server on specified port
//        Socket socket = new Socket(server, servPort);
//        System.out.println("Connected to server...sending echo string");
//
//        InputStream in = socket.getInputStream();
//        OutputStream out = socket.getOutputStream();
            
        while (counter < 10) {
            System.out.print("Enter message: ");
        String newMsg = kb.nextLine();
        byte[] byteBuffer = newMsg.getBytes();
            Socket socket = new Socket(server, servPort);
        System.out.println("Connected to server...sending echo string");

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
            out.write(byteBuffer);						// Send the encoded string to the server

            // Receive the same string back from the server
            int totalBytesRcvd = 0;						// Total bytes received so far
            int bytesRcvd;								// Bytes received in last read
            while (totalBytesRcvd < byteBuffer.length) {
                if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
                        byteBuffer.length - totalBytesRcvd)) == -1) {
                    throw new SocketException("Connection close prematurely");
                }
                totalBytesRcvd += bytesRcvd;
            }

            System.out.println("Received: " + new String(byteBuffer));
            counter++;
            socket.close();
        }
//        socket.close();
    }

    public static void verifyCorrectNumOfArgs(String[] args) {
        if ((args.length < 1) || (args.length > 2)) { // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Server> "
                    + " [<Port>]");
        }
    }
    
//    public static byte[] requestUserMove(){
//        Scanner kb = new Scanner(System.in);
//        System.out.print("Enter X coordinate: ");
//        int x= kb.nextInt();
//        System.out.print("Enter Y coordinate: ");
//        int y= kb.nextInt();
//        int move[] = new int[3];
//        move[0] = 1; //1 indicating a move
//        move[1] = x;
//        move[2] = y;
//                
//        byte[] byteBuffer = new byte[3];
//        byteBuffer[0] = (byte)(move[0]>>8);
//        
//        byte[0] = move[0].getBytes();
//    }
}
