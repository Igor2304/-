package com.paradox.gameboostlite;

import android.app.*;import android.os.*;import android.content.*;import android.content.pm.*;import android.graphics.Color;import android.net.Uri;import android.provider.Settings;import android.view.*;import android.widget.*;import java.util.*;

public class MainActivity extends Activity{
  LinearLayout list; PackageManager pm;
  int green=Color.rgb(124,255,107), bg=Color.rgb(16,17,20), card=Color.rgb(31,33,40);
  @Override public void onCreate(Bundle b){super.onCreate(b);pm=getPackageManager();build();}
  TextView tv(String s,int sp,int c){ TextView v=new TextView(this); v.setText(s); v.setTextSize(sp); v.setTextColor(c); v.setPadding(24,14,24,14); return v; }
  Button btn(String s){ Button b=new Button(this); b.setText(s); b.setTextColor(Color.BLACK); b.setAllCaps(false); return b; }
  void build(){ ScrollView sv=new ScrollView(this); LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(bg); root.setPadding(18,30,18,30); sv.addView(root);
    TextView title=tv("GameBoost Lite",28,green); title.setTypeface(null,1); root.addView(title);
    root.addView(tv("Честный игровой режим: запускает игру, держит экран активным, убирает уведомления через DND и ведёт к системным настройкам, которые реально влияют на лаги.",16,Color.WHITE));
    LinearLayout row=new LinearLayout(this); row.setOrientation(LinearLayout.VERTICAL); root.addView(row);
    Button dnd=btn("1) Дать доступ к режиму Не беспокоить"); dnd.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))); row.addView(dnd);
    Button batt=btn("2) Отключить экономию батареи для GameBoost"); batt.setOnClickListener(v->{ try{ Intent i=new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS); i.setData(Uri.parse("package:"+getPackageName())); startActivity(i);}catch(Exception e){startActivity(new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS));}}); row.addView(batt);
    Button usage=btn("3) Открыть доступ к статистике использования"); usage.setOnClickListener(v->startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))); row.addView(usage);
    root.addView(tv("Выбери игру:",20,green)); list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); root.addView(list); setContentView(sv); loadApps(); }
  void loadApps(){ list.removeAllViews(); Intent intent=new Intent(Intent.ACTION_MAIN,null); intent.addCategory(Intent.CATEGORY_LAUNCHER); List<ResolveInfo> apps=pm.queryIntentActivities(intent,0); Collections.sort(apps,new ResolveInfo.DisplayNameComparator(pm));
    for(ResolveInfo r:apps){ String pkg=r.activityInfo.packageName; if(pkg.equals(getPackageName())) continue; String name=r.loadLabel(pm).toString(); addApp(name,pkg); }
  }
  void addApp(String name,String pkg){ LinearLayout item=new LinearLayout(this); item.setOrientation(LinearLayout.VERTICAL); item.setPadding(22,18,22,18); item.setBackgroundColor(card); LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2); lp.setMargins(0,10,0,10); item.setLayoutParams(lp);
    TextView n=tv(name,18,Color.WHITE); TextView p=tv(pkg,12,Color.LTGRAY); Button launch=btn("Запустить с профилем"); launch.setOnClickListener(v->{ Intent i=new Intent(this,GameSessionActivity.class); i.putExtra("pkg",pkg); i.putExtra("name",name); startActivity(i); }); item.addView(n); item.addView(p); item.addView(launch); list.addView(item); }
}
