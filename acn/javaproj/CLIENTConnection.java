import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.lang.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CLIENTConnection implements Runnable {

    private Socket clientSocket;
    private BufferedReader in = null;
    public String path;
    int Port;
    public CLIENTConnection(int port) {
    	 Port=port;
    }

    @Override
    public void run() {
        try {
        	ServerSocket z=new ServerSocket(Port);
            
        	//For command line input 1,2 send and recieve resp.
            //in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
         while(true){
        	 System.out.println("waiting for connection");
        	clientSocket=z.accept();
        	System.out
			.println("Accepted connection : " + clientSocket);
        	String clientSelection="2";
            //while ((clientSelection = in.readLine()) != null) {
           // while(){
                switch (clientSelection) {
                    case "1":
                        receiveFile();
                        break;
                    case "2":
                        String outGoingFileName;
                       // while ((outGoingFileName = in.readLine()) != null) {
                            sendFile("E:/abebooks.txt");
                       // }

                        break;
                    default:
                        System.out.println("Incorrect command received.");
                        break;
                }
                //in.close();
                //break;
            //}
         }
        } catch (Exception ex) {
            Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void receiveFile() {
        try {
            int bytesRead;

            DataInputStream clientData = new DataInputStream(clientSocket.getInputStream());

            String fileName = "abe123test.docx";//clientData.readUTF();
            OutputStream output = new FileOutputStream(fileName);//"received_from_client_" +
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            clientData.close();

            System.out.println("File "+" received from client.");
        } catch (IOException ex) {
            System.err.println("Client error. Connection closed.");
        }
    }

    public void sendFile(String fileName) {
        try {
            //handle file read
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            //handle file send over socket
            OutputStream os = clientSocket.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File "+fileName+" sent to client.");
        } catch (Exception e) {
            System.err.println("File does not exist!");
        } 
    }
}