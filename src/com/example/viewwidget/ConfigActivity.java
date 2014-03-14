package com.example.viewwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends Activity {
    private int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Intent resultValue;
  
    final String LOG_TAG = "myLogs";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_LOGIN = "widget_login";
    public final static String WIDGET_PASSWORD = "widget_password";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // извлекаем ID конфигурируемого виджета
    Intent intent = getIntent();
    Bundle extras = intent.getExtras();
    if (extras != null) {
      widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    // и проверяем его корректность
    if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
    }
    
    // формируем intent ответа
    resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
    
    // отрицательный ответ
    setResult(RESULT_CANCELED, resultValue); 
    
    setContentView(R.layout.config);
  }
  
  
  public void onClick(View v) {
	  EditText loginEditText = (EditText) findViewById(R.id.loginEditText);
	  EditText passwordText = (EditText) findViewById(R.id.passwordEditText);
	  final String loginString = loginEditText.getText().toString();
	  final String passwordString = passwordText.getText().toString();
	  SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
	  Editor editor = sp.edit();
	  editor.putString(WIDGET_LOGIN + widgetID, loginString);
	  editor.putString(WIDGET_PASSWORD + widgetID, passwordString);
	  editor.commit();
	  AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
	  MyWidget.updateWidget(this, appWidgetManager, sp, widgetID); 
	    // положительный ответ 
	  setResult(RESULT_OK, resultValue);
	    
	  finish();
  }
 
}