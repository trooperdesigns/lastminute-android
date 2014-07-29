package com.jc.strongloopauth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ProfileActivity extends Activity {
	
	TextView profileName;
	Button logout;
	
	private String localAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        
        profileName = (TextView) findViewById(R.id.profileName);
        logout = (Button) findViewById(R.id.logout);
        
        logout.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				new Logout(localAccessToken).execute();
				
			}
        	
        });
        
        Intent goProfile = getIntent();
        this.localAccessToken = goProfile.getStringExtra("com.jc.strongloopauth");
        //Log.i("profile", localAccessToken);
        
        profileName.setText("Logged in as: " + localAccessToken);
        
        
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
