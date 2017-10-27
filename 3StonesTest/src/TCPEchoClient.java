
import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner;
import three_stones.views.Board;
//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes>java TCPEchoClient 192.168.56.1 4455
public class TCPEchoClient {

    int HUMAN = 1;
        int ROBOT = 2;
        int ERROR = 3;
        int WIN = 6; //user win
        int LOSS = 7; //user loss
        int DRAW = 8;
        int QUIT = 5;
        int NEWGAME = 4;
        int GAMEOVER = 9;
    public static void main(String[] args) throws IOException {
        
        
        Board board = new Board();
        System.out.println(board.toString());
        int counter = 0;
        Scanner kb = new Scanner(System.in);

        String server = getIpAddress();

        int servPort = 50000;
        Socket socket = new Socket(server, servPort);
        boolean gameOver = false;
        boolean firstMove = true;
        while (gameOver == false) { //this should be changed for a game over boolean
            int[] move;
            byte[] byteBuffer;

            move = requestUserMove();    
            byteBuffer = convertIntToByteArrays(move);

            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write(byteBuffer);	// Send the encoded string to the server
            System.out.println("HERE");
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
            if (recvInts[0] >= 6){ //GAME OVER
                board.updateBoard(2, new int[]{recvInts[1],recvInts[2]}); // updates ROBOTS last move
                System.out.println("Game is Over");
                if(recvInts[0] == 6){
                    System.out.println ("YOU WIN!");
                } else if (recvInts[0] == 7){
                    System.out.println ("YOU LOSE!");
                } else {
                    System.out.println ("It's a draw!");
                }
                if(userPlayAgain()){
                    System.out.println(board.toString());
                    System.out.println ("SCORE\nROBOT: " + recvInts[3] + " - YOU: " + recvInts[4]);
                    
                    System.out.println("User requested to play again");
                    board.reset();
                    System.out.print(board.toString());
                } else {
                    System.out.println("User requested to quit");
                }
            } else if (recvInts[0] == 3){  //3 is error code
                System.out.println("Invalid Move");
            } else if (recvInts[0] == 6){
                System.out.println("New Game has begun");
            } else {
                board.updateBoard(1, new int[]{move[1],move[2]}); //user's , should technical be done AFTERValidation, will need to fix this
                board.updateBoard(2, new int[]{recvInts[1],recvInts[2]});  
                System.out.println(board.toString());
                System.out.println ("SCORE\nROBOT: " + recvInts[3] + " - YOU: " + recvInts[4]);
            }
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
        int move[] = new int[5];
        move[0] = 1; //1 indicating a move from user
        move[1] = x;
        move[2] = y;
        move[3] = 0;
        move[4] = 0;
                
        return move;
    }
    
    public static byte[] convertIntToByteArrays(int[] move){
        byte[] bytes = new byte[5];
        for(int i = 0; i<5; i++){
            bytes[i] = (byte)move[i];
        }
        return bytes;
    }
    
    public static int[] convertBytesToIntArrays(byte[] bytes){
        int[] move = new int[5];
        for(int i = 0; i<5; i++){
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
    
    private static boolean userPlayAgain(){
        System.out.println("Game is Over, would you like to play again");
        System.out.println("1. Yes");
        System.out.println("2. No, I quit.");
        Scanner kb = new Scanner(System.in);
        int answer = kb.nextInt();
        System.out.println("You chose: "+answer);
        if(answer == 1){ 
            return true;
        } else {
            return false;
        }
    }
    
    private byte[] requestNewGame(){
        int[] userMessageInt = new int[]{NEWGAME,0,0,0,0};
        return convertIntToByteArrays(userMessageInt);
    }
    
    private byte[] userQuit(){
        int[] userMessageInt = new int[]{GAMEOVER,0,0,0,0};
        return convertIntToByteArrays(userMessageInt);
    }
}
