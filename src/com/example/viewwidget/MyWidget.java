package com.example.viewwidget;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

public class MyWidget extends AppWidgetProvider {
    private PendingIntent service = null;

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager,
      int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
    
    final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    final Calendar TIME = Calendar.getInstance();
    TIME.set(Calendar.MINUTE, 0);
    TIME.set(Calendar.SECOND, 0);
    TIME.set(Calendar.MILLISECOND, 0);

    final Intent i = new Intent(context, MyService.class);
    if (service == null)
    {
        service = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    alarmManager.setRepeating(AlarmManager.RTC, TIME.getTime().getTime(), 1000 * 60 * 5, service);

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
    final AlarmManager m = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);  
    if (service != null)
    {
        m.cancel(service);
    }

  }
}