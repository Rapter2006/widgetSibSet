package com.example.viewwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

public class MyWidget extends AppWidgetProvider {
	public static String widgetLogin;
	public static String widgetPassword;
	

  
 

  
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  
    SharedPreferences sp = context.getSharedPreferences(
        ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
    for (int id : appWidgetIds) {
      updateWidget(context, appWidgetManager, sp, id);
    }
  }

  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    super.onDeleted(context, appWidgetIds);
   

    // Удаляем Preferences
    Editor editor = context.getSharedPreferences(
        ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE).edit();
    for (int widgetID : appWidgetIds) {
      editor.remove(ConfigActivity.WIDGET_PASSWORD + widgetID);
      editor.remove(ConfigActivity.WIDGET_LOGIN + widgetID);
    }
    editor.commit();
  }

  @Override
  public void onDisabled(Context context) {
    super.onDisabled(context);

  }

  static void updateWidget(Context context, AppWidgetManager appWidgetManager,
      SharedPreferences sp, int widgetID) {
    
    new HTMLpageGetter(context, appWidgetManager, sp, widgetID).execute();
  }
}