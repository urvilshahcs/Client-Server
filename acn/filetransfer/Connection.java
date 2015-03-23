package com.example.filetransfer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.os.Environment;

public class Connection extends AsyncTask<Void, Void, Void>{
	public Socket sock;
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		try {
			sock = new Socket("192.168.43.126", 8888);
			receiveFile();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public  void receiveFile() {
        try {
        	String fileName2 = "abebooks.docx";
        	String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        	System.out.println(path);
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);
            File file = new File(path, fileName2);
            System.out.println("SecondCheckPoint");
            if(!file.exists())
            {
            	file.createNewFile();
            }
            
            System.out.println("Third Checkpoint");
//            fileName = clientData.readUTF();
            FileOutputStream output = new FileOutputStream(file);
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            System.out.println("Hello");
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) 
            {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }
            System.out.println("FinalCheck");
            output.close(); 
            in .close();
            
//            System.out.println("File " + fileName + " received from Server.");
            sock.close();
        } catch (Exception ex) {
          //  Logger.getLogger(CLIENTConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
}
}
