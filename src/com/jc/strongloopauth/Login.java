package com.jc.strongloopauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Login extends AsyncTask<String, Void, String> {
	
	private String email;
	private String password;
	private String localAccessToken;
	
	public Login (String email, String password){
		this.email = email;
		this.password = password;
	}

	// network io cannot be done in main thread
	@Override
	protected String doInBackground(String... arg0) {

		// post request w/ login info
		this.localAccessToken = login(email, password);
		
		// get request for profile
		//getProfile();

		return localAccessToken;
	}
	
	protected void onPostExecute(String result){
		this.localAccessToken = result;
	}

	// login
	public String login(String email, String password) {
		HttpClient httpclient = new DefaultHttpClient();
		
		// change ip accordingly
		HttpPost httppost = new HttpPost("http://192.168.0.117:3000/api/users/login");

		try {
			// add header data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			// read response
			HttpEntity entity = response.getEntity();
			InputStream input = entity.getContent();
			
			// buffer the response and write to a printable string
			String res = getStringFromInputStream(input);
			
			try {
				JSONObject jsonObj = new JSONObject(res);
				Log.i("success", "first: " + jsonObj.getString("id"));
				String localAccessToken = jsonObj.getString("id");
				return localAccessToken;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
			return cpe.getLocalizedMessage();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return ioe.getLocalizedMessage();
		}
	}

	// get request for profile
	public void getProfile() {
		HttpResponse response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			
			// change ip accordingly
			request.setURI(new URI("http://192.168.0.117:3000/profile"));
			response = client.execute(request);
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return response;
	}
	
	// convert input stream from response buffer to string
	public static String getStringFromInputStream(InputStream is){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e){
			e.printStackTrace();
		} finally {
			if (br != null){
				try {
					br.close();
				} catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public String getLocalAccessToken(){
		return localAccessToken;
	}
	
	public void setLocalAccessToken(String localAccessToken){
		this.localAccessToken = localAccessToken;
	}

}
