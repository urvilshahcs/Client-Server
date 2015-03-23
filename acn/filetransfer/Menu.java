package com.example.filetransfer;



import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menu extends ListActivity {
	String [] s={"SendFileToServer","RecieveFileFromServer"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		System.out.println("In oncreate of menu.java");
		setListAdapter(new ArrayAdapter<String>(Menu.this,android.R.layout.simple_list_item_1,s));
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String openClass=s[position];
		
		Class toOpen=null;
		try {
			toOpen=Class.forName("com.example.filetransfer."+openClass);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent newIntent=new Intent(Menu.this,toOpen);
		startActivity(newIntent);
	}

	
	
	
}
