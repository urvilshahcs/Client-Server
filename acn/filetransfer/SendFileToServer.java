package com.example.filetransfer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SendFileToServer extends Activity{
	Button b1,b2;
	String pathSelect;
	FileDialog fileDialog; 
	File f;
	private static Socket sock;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.file);
		
		try {
			sock = new Socket("192.168.43.126", 8888);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		b1= (Button) findViewById(R.id.bBrowse);
		b2= (Button) findViewById(R.id.bSend);
		b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
	            fileDialog = new FileDialog(SendFileToServer.this, mPath);
	            //fileDialog.setFileEndsWith(".txt");
	            fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
	                public void fileSelected(File file) {
	                	f=file;
	                    Log.d(getClass().getName(), "selected file " + file.toString());
	                    
	                }
	            });
	            fileDialog.showDialog();
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//CLIENTConnection.clientSelection=1;
				try {
		            System.err.print("Enter file name: ");
		            
		            File myFile = f;
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
		            System.out.println("File " + f.getName() + " sent to Server.");
		        } catch (Exception e) {
		            System.err.println("File does not exist!");
		        }
			}
		});

	}

	
}
