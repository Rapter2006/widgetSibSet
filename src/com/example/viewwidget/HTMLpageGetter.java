package com.example.viewwidget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class HTMLpageGetter extends AsyncTask<Void, Void, Void> {
	private String resultValueString = "";
	private Context context;
	private int widgetId;
	private String schet = "";
	private SharedPreferences sp;
	private Document doc;
	private AppWidgetManager appWidgetManager;
	private String LOGIN = "login";
	private String PASSWORD = "password";
	private String ATHER_INFO = "retpath";
	private String ATHER_INFO_VALUE = "http://211.ru/?auth=1";
	private String MAIN_SITE_PAGE = "http://header.211.ru/";
	private String BALANSE_PAGE = "http://passport.211.ru/cabinet/balance";
	
	public String getStringFromResponse(HttpResponse response) {
		String responseBody = "";
		try {
			InputStream in = response.getEntity().getContent();
		    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		    StringBuilder str = new StringBuilder();
		    String line = null;
		    while((line = reader.readLine()) != null)
		    {
		        str.append(line);
		    }
		    in.close();
		    responseBody = str.toString();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	    return responseBody;
	}
	
	
	public HTMLpageGetter(Context context, AppWidgetManager appWidgetManager, SharedPreferences sp, int id) {
		this.context = context;
		this.widgetId = id;
		this.sp = sp;
		this.appWidgetManager = appWidgetManager;
	}
	
	@Override 
	protected Void doInBackground(Void... params) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(MAIN_SITE_PAGE);
		HttpGet httpGet = new HttpGet(BALANSE_PAGE);
		 // Читаем параметры Preferences
		boolean start = true;
	    final String widgetLogin = sp.getString(ConfigActivity.WIDGET_LOGIN + widgetId, null);
	    if (widgetLogin == null) start = false;
	    final String widgetPassword = sp.getString(ConfigActivity.WIDGET_PASSWORD + widgetId, null);
	    if (widgetPassword == null) start = false;
	    if(start) {
			try {
			    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    
			    Log.d("WIDGET", "WIDGET");
			    nameValuePairs.add(new BasicNameValuePair(LOGIN, widgetLogin));
			    nameValuePairs.add(new BasicNameValuePair(PASSWORD, widgetPassword));
			    nameValuePairs.add(new BasicNameValuePair(ATHER_INFO, ATHER_INFO_VALUE));
			    String responseBody = "";
			    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			   
			    HttpResponse response = httpclient.execute(httpPost); 
			   
			    CookieStore store = ((DefaultHttpClient) httpclient).getCookieStore();
			    HttpContext ctx = new BasicHttpContext();
			    ctx.setAttribute(ClientContext.COOKIE_STORE, store);
			    response = httpclient.execute(httpGet, ctx);
	
			    responseBody = getStringFromResponse(response);
				 
			    doc = Jsoup.parse(responseBody);
			    
			    Element accountValue = doc.select("div.article").select("h1").first();
			    resultValueString += accountValue.text();
		        
			    Element account = doc.select("div[class=item user-desk-plan]").select("strong").first();  
			    schet += account.text();
			} catch (ClientProtocolException e) {
			} catch (IOException e) { 
		   }
		}
		return null;
	}
	
	private void setWidgetColor(double value, RemoteViews widgetView) {
		if(value >= 50) {
			 widgetView.setInt(R.id.tv, "setBackgroundColor", Color.BLUE);
		}
		else if(value < 50 && value > 30) {
			 widgetView.setInt(R.id.tv, "setBackgroundColor", Color.GRAY);
		}
		else {
			widgetView.setInt(R.id.tv, "setBackgroundColor", Color.RED);
		}
	}
	
	private double parseStringGetManey(String widgetString) {
		double result = 0;
		Pattern p = Pattern.compile("(\\d+(?:\\,\\d+))");
		Matcher m = p.matcher(widgetString);
		while(m.find()) {
		    result = Double.parseDouble(m.group(1).replace(",", "."));
		}
		return result;
	}
	  @Override
	    protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	   
	      RemoteViews widgetView = new RemoteViews(context.getPackageName(),
	          R.layout.widget);
	      if(!resultValueString.equals("")) {
		      double valueAccount = parseStringGetManey(resultValueString);
		      setWidgetColor(valueAccount, widgetView);
		      widgetView.setTextViewText(R.id.tv, resultValueString);
		      widgetView.setTextViewText(R.id.accountValue, "Номер счета: " + schet);
		  
		      appWidgetManager.updateAppWidget(widgetId, widgetView);
	      }
	      
	    }
}