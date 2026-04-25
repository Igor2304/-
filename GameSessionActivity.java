package com.paradox.gameboostlite;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.view.*;import android.widget.*;

public class GameSessionActivity extends Activity{
  String pkg,name; NotificationManager nm; int oldFilter=-1;
  @Override public void onCreate(Bundle b){super.onCreate(b); pkg=getIntent().getStringExtra("pkg"); name=getIntent().getStringExtra("name"); nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE); getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); ui(); }
  void ui(){ LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,45,28,28); root.setBackgroundColor(Color.rgb(16,17,20)); setContentView(root);
    TextView title=new TextView(this); title.setText("Профиль: "+name); title.setTextSize(25); title.setTypeface(null,1); title.setTextColor(Color.rgb(124,255,107)); root.addView(title);
    TextView text=new TextView(this); text.setText("Что включено:\n• экран не гаснет\n• при наличии разрешения включается DND\n• игра запускается напрямую\n\nПосле выхода DND вернётся назад. CPU/GPU приложение без root не трогает — Android не даёт."); text.setTextColor(Color.WHITE); text.setTextSize(16); text.setPadding(0,20,0,20); root.addView(text);
    Button start=new Button(this); start.setText("Старт игры"); start.setAllCaps(false); start.setOnClickListener(v->launchGame()); root.addView(start);
    Button stop=new Button(this); stop.setText("Выключить игровой профиль"); stop.setAllCaps(false); stop.setOnClickListener(v->{restoreDnd();finish();}); root.addView(stop);
  }
  @Override protected void onResume(){super.onResume(); applyDnd();}
  @Override protected void onDestroy(){restoreDnd(); super.onDestroy();}
  void applyDnd(){ if(Build.VERSION.SDK_INT>=23 && nm!=null && nm.isNotificationPolicyAccessGranted()){ try{ oldFilter=nm.getCurrentInterruptionFilter(); nm.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);}catch(Exception ignored){} } }
  void restoreDnd(){ if(Build.VERSION.SDK_INT>=23 && nm!=null && oldFilter!=-1 && nm.isNotificationPolicyAccessGranted()){ try{ nm.setInterruptionFilter(oldFilter);}catch(Exception ignored){} oldFilter=-1; } }
  void launchGame(){ Intent launch=getPackageManager().getLaunchIntentForPackage(pkg); if(launch!=null){ launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(launch);} else Toast.makeText(this,"Не могу открыть игру",Toast.LENGTH_SHORT).show(); }
}
