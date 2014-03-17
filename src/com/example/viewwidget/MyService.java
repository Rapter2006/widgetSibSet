package com.example.viewwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service
{

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        buildUpdate(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void buildUpdate(Intent intent)
    {
    	
        Log.d("OLOLO", "OLOLO!!!");
        SharedPreferences sp = this.getSharedPreferences(
            ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, MyWidget.class));
        
        // Log.d("sp", sp.getString(ConfigActivity.WIDGET_LOGIN + appWidgetId, null));
        for (int id : widgetIds) {
        	new HTMLpageGetter(this, widgetManager, sp, id).execute();
          }
        
       
        
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}