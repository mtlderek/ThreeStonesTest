
import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner;
import three_stones.views.Board;
//C:\Users\derek\Documents\3StonesTest\ThreeStonesTest\3StonesTest\build\classes>java TCPEchoClient 192.168.56.1 4455
public class TCPEchoClient {

    //types of messages
    private static final int HUMAN = 1; // represents the user, or users move
    private static final int ROBOT = 2; // represents the server, or server's move
    private static final int ERROR = 3;
    private static final int WIN = 6;  //user win
    private static final int LOSS = 7; //user loss
    private static final int DRAW = 8;
    private static final int QUIT = 5;
    private static final int NEWGAME = 4;
//    private static final int GAMEOVER = 9;
    
    //defines what each index int move[] or recvInts[] represent
    private static final int MESSAGE_TYPE = 0;
    private static final int X_COORDINATE = 1;
    private static final int Y_COORDINATE = 2;
    private static final int ROBOT_SCORE = 3;
    private static final int HUMAN_SCORE = 4;
    
    public static void main(String[] args) throws IOException {
        Board board = new Board();
        System.out.println(board.toString());
        Scanner kb = new Scanner(System.in);
        String server = getIpAddress();
        int servPort = 50000;
        Socket socket = new Socket(server, servPort);
        boolean gameOver = false;
        
        while (gameOver == false) { //this should be changed for a game over boolean
            int[] move;
            byte[] byteBuffer;
            
            move = requestUserMove();    
            if(move[0] == QUIT){ //user opted to quit
                socket.close();
                System.out.println("Connection closed.");
                System.exit(0);
            }
            
            byteBuffer = convertIntToByteArrays(move);

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
            if (recvInts[MESSAGE_TYPE] >= 6){ //Game over
                board.updateBoard(ROBOT, new int[]{recvInts[X_COORDINATE],recvInts[Y_COORDINATE]}); // updates ROBOT's last move
                displayResult(recvInts[MESSAGE_TYPE]); //WIN, LOSS< or DRAW
                if(userPlayAgain()){ // user wants to play again
                    System.out.println(board.toString());
                    System.out.println ("SCORE\nROBOT: " + recvInts[ROBOT_SCORE] 
                            + " - YOU: " + recvInts[HUMAN_SCORE]);
                    System.out.println("User requested to play again");
                    board.reset();
                    System.out.print(board.toString());
                } else { //user wants to quit
                    System.out.println("User requested to quit");
                    socket.close();
                    System.out.println("Connection closed.");
                    System.exit(0);
                }
            } else if (recvInts[MESSAGE_TYPE] == ERROR){  
                System.out.println("Invalid Move");
            } else { //user's move is valid
                board.updateBoard(HUMAN, new int[]{move[X_COORDINATE],move[Y_COORDINATE]}); 
                board.updateBoard(ROBOT, new int[]{recvInts[X_COORDINATE],recvInts[Y_COORDINATE]});  
                System.out.println(board.toString());
                System.out.println ("SCORE\nROBOT: " + recvInts[ROBOT_SCORE] + 
                        " - YOU: " + recvInts[HUMAN_SCORE]);
            }
        }
        socket.close();
    }
    
    
    public static int[] requestUserMove(){
        Scanner kb = new Scanner(System.in);
        System.out.println("Enter 9 as a coordinate at anytime to quit");
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
        
        if (x == 9 || y == 9){ //user quit
            return userQuit();
        } else {
            return move;
        }
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
        System.out.println("Would you like to play again");
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
    
    private static int[] userQuit(){
        return new int[]{QUIT,0,0,0,0};
    }
    
    private static void displayResult(int msgCode){
        System.out.println("Game is Over");
        switch (msgCode) {
            case WIN :
                System.out.println("YOU WIN!");
                break;
            case LOSS:
                System.out.println("YOU LOSE!");
                break;
            case DRAW:
                System.out.println("It's a draw!");
                break;
            default:
                System.out.println("An error has occured could not feth result");
                break;
        }
    }
}
