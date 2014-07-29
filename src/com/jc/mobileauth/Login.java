package com.jc.mobileauth;

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

import android.os.AsyncTask;
import android.util.Log;

public class Login extends AsyncTask<String, Void, Void> {
	
	private String email;
	private String password;
	
	public Login (String email, String password){
		this.email = email;
		this.password = password;
	}

	@Override
	protected Void doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		login(email, password);
		//getProfile();

		return null;
	}

	public String login(String email, String password) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("http://192.168.0.117:8080/login");

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs
					.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			
			HttpEntity entity = response.getEntity();
			InputStream input = entity.getContent();
			
			String res = getStringFromInputStream(input);
			
			Log.i("success", res);
			
			return res;

		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
		return "";
	}

	public void getProfile() {
		HttpResponse response = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(new URI("http://192.168.0.117:8080/profile"));
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

}
