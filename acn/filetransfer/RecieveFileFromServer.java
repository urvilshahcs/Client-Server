package com.example.filetransfer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RecieveFileFromServer extends Activity{
	EditText textOut;
	TextView textIn;
	public String path;
	private static Socket sock;
	private static String fileName;
	private static BufferedReader stdin;
	private static PrintStream os;

	/** Called when the activity is first created. */
	@
	Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		textOut = (EditText) findViewById(R.id.textout);
		Button buttonSend = (Button) findViewById(R.id.send);
		textIn = (TextView) findViewById(R.id.textin);
		//buttonSend.setOnClickListener(buttonSendOnClickListener);
		buttonSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					sock = new Socket("192.168.43.126", 8888);
					//					new Connection().execute();
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//CLIENTConnection.clientSelection=2;
				//	        	receiveFile(textOut.getText().toString());
				textOut = (EditText) findViewById(R.id.textout);
				path = textOut.toString();
				
				receiveFile();
				Toast.makeText(getApplicationContext(), "Received", Toast.LENGTH_SHORT).show();
			}
		});
	}

	
	public static void sendFile() {
		try {
			System.err.print("Enter file name: ");
			fileName = stdin.readLine();

			File myFile = new File(fileName);
			byte[] mybytearray = new byte[(int) myFile.length()];

			FileInputStream fis = new FileInputStream(myFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			//bis.read(mybytearray, 0, mybytearray.length);

			DataInputStream dis = new DataInputStream(bis);
			dis.readFully(mybytearray, 0, mybytearray.length);

			OutputStream os = sock.getOutputStream();

			//Sending file name and file size to the server
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(myFile.getName());
			dos.writeLong(mybytearray.length);
			dos.write(mybytearray, 0, mybytearray.length);
			dos.flush();
			System.out.println("File " + fileName + " sent to Server.");
		} catch (Exception e) {
			System.err.println("File does not exist!");
		}
	}



	//    public static void receiveFile(String fileName) {
	public void receiveFile() {

		try {
			String fileName2 = "abebooks.txt";
			String path = Environment.getExternalStorageDirectory().getAbsolutePath();
			System.out.println(path);
			int bytesRead;
			InputStream in = sock.getInputStream();

			DataInputStream clientData = new DataInputStream( in );
			File file = new File(path,fileName2);
		     System.out.println("SecondCheckPoint"); 
			if(!file.exists())
			{
				file.createNewFile();
			}

			System.out.println("Third Checkpoint");
			//fileName = clientData.readUTF();
			FileOutputStream output = new FileOutputStream(file);
			long size = clientData.readLong();
			byte[] buffer = new byte[1024];
			System.out.println("Hello");
			while ((size > 0) && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
			{
				output.write(buffer, 0, bytesRead);
				size -= bytesRead;
			}
			output.close(); 
			in .close();

			//		            System.out.println("File " + fileName + " received from Server.");
			sock.close();
			System.out.println("After sock close");
		} catch (Exception ex) {
			//  Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
		}

		//    	new Connection().execute();
	}
};

