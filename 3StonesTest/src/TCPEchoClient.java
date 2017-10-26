
import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner;
import three_stones.views.Board;
//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes>java TCPEchoClient 192.168.56.1 4455
public class TCPEchoClient {

    public static void main(String[] args) throws IOException {
        Board board = new Board();
        System.out.println(board.toString());
        int counter = 0;
        Scanner kb = new Scanner(System.in);

        String server = getIpAddress();

        int servPort = 50000;
        Socket socket = new Socket(server, servPort);
        while (counter < 30) { //this shuold be changed for a game over boolean
            int[] move = requestUserMove();    
            byte[] byteBuffer = convertIntToByteArrays(move);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(byteBuffer);	// Send the encoded string to the server

            // Receive the same string back from the server
            int totalBytesRcvd = 0;	// Total bytes received so far
            int bytesRcvd;		// Bytes received in last read
            while (totalBytesRcvd < byteBuffer.length) {
                if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
                        byteBuffer.length - totalBytesRcvd)) == -1) {
                    throw new SocketException("Connection close prematurely");
                }
                totalBytesRcvd += bytesRcvd;
            }

            int recvInts[] = convertBytesToIntArrays(byteBuffer);
            
            if (recvInts[0] == 3){  //3 is error code
                System.out.println("Invalid Move"); 
            } else {
                board.updateBoard(1, new int[]{move[1],move[2]}); //user's , should technical be done AFTERValidation, will need to fix this
                board.updateBoard(2, new int[]{recvInts[1],recvInts[2]});   
            }
//            board.updateBoard(1, new int[]{move[1],move[2]}); //user's , should technical be done AFTERValidation, will need to fix this
//            board.updateBoard(2, new int[]{recvInts[1],recvInts[2]});
            System.out.println(board.toString());
            counter++;
        }
        socket.close();
    }
    
    
    public static int[] requestUserMove(){
        Scanner kb = new Scanner(System.in);
        System.out.print("Enter X coordinate: ");
        int x= kb.nextInt();
        System.out.print("Enter Y coordinate: ");
        int y= kb.nextInt();
        int move[] = new int[3];
        move[0] = 1; //1 indicating a move from user
        move[1] = x;
        move[2] = y;
                
        return move;
    }
    
    public static byte[] convertIntToByteArrays(int[] move){
        byte[] bytes = new byte[3];
        for(int i = 0; i<3; i++){
            bytes[i] = (byte)move[i];
        }
        return bytes;
    }
    
    public static int[] convertBytesToIntArrays(byte[] bytes){
        int[] move = new int[3];
        for(int i = 0; i<3; i++){
            move[i] = bytes[i] & 0xFF; //converts to integer
        }
        return move;
    }
    
    public static String getIpAddress(){
        String localIp = "";
        try{
            localIp = InetAddress.getLocalHost().getHostAddress();
        } catch(Exception e) {
            System.out.println("Unable to get local host ip");
        }
        System.out.println("Choose the one of the following:");
        System.out.println("1. Use localhost");
        System.out.println("2. Enter ip address:");
        Scanner kb = new Scanner(System.in);
        int userChoice = kb.nextInt();
        switch(userChoice){
            case 1: 
                return localIp;
            case 2:
                return getUserEnteredIp();
            default:
                return null;
        }
    }
    
    public static String getUserEnteredIp(){
        Scanner kb = new Scanner(System.in);
        return kb.nextLine();
    }
}
