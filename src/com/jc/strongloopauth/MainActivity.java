package com.jc.strongloopauth;

import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	Button login;
	EditText loginEmail;
	EditText loginPassword;
	private String localAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        login = (Button) findViewById(R.id.login);
        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPassword = (EditText) findViewById(R.id.loginPassword);
        
        // login button
        login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Context context = getApplicationContext();
				CharSequence text = "access token: ";
				int duration = Toast.LENGTH_SHORT;
				
				// do network io in bg thread - can't be on main thread
				AsyncTask<String, Void, String> login = new Login(loginEmail.getText().toString(), loginPassword.getText().toString()).execute();
				try {
					if(login.get() != null){
						Log.i("success", "result: " + login.get());
						text = text + login.get();
						localAccessToken = login.get();
						
						Intent goProfile = new Intent(getApplicationContext(), ProfileActivity.class);
				        goProfile.putExtra("com.jc.strongloopauth", localAccessToken);
				        startActivity(goProfile);
				        
				        //finish();
						
					} else {
						text = "login unsuccessful";
					}
					
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					text = "Error: " + e.getLocalizedMessage();
					
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					text = "Error: " + e.getLocalizedMessage();
					
				}
				//localAccessToken = Login.getLocalAccessToken();
				//Log.i("success", localAccessToken);
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
		});
        
        
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
