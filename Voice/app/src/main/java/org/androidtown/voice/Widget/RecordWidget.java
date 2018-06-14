//package org.androidtown.voice.Widget;
//
//import android.app.PendingIntent;
//import android.appwidget.AppWidgetManager;
//import android.appwidget.AppWidgetProvider;
//import android.content.Context;
//import android.content.Intent;
//import android.widget.RemoteViews;
//
//import org.androidtown.voice.MainActivity;
//import org.androidtown.voice.R;
//
///**
// * Implementation of App Widget functionality.
// */
//public class RecordWidget extends AppWidgetProvider {
//
//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_record);
//        Intent intent= new Intent(context, MainActivity.class);
//        PendingIntent pe=PendingIntent.getActivity(context, 0, intent, 0);
//        views.setOnClickPendingIntent(R.id.record, pe);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }
//
//    @Override
//    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
//        // There may be multiple widgets active, so update all of them
//        for (int appWidgetId : appWidgetIds) {
//            updateAppWidget(context, appWidgetManager, appWidgetId);
//        }
//    }
//
//    @Override
//    public void onEnabled(Context context) {
//        // Enter relevant functionality for when the first widget is created
//    }
//
//    @Override
//    public void onDisabled(Context context) {
//        // Enter relevant functionality for when the last widget is disabled
//    }
//}
//

package org.androidtown.voice.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import org.androidtown.voice.MainActivity;
import org.androidtown.voice.R;

/**
 * Created by Sansung on 2016-08-15.
 */
public class RecordWidget extends AppWidgetProvider {
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //위젯 갱신 주기에 따라 위젯을 갱신할 때 호출되는 메소드
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        for(int i = 0; i < appWidgetIds.length; i++) {
            updateAppWidget(context, appWidgetManager, appWidgetIds[i]);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        //현재 시간 정보를 가져오기 위한 Calendar
//        Calendar mCalendar = Calendar.getInstance();
//        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);

        //RemoteViews를 이용해 Text설정
        RemoteViews updateViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_record);

//        updateViews.setTextViewText(R.id.widgetTextView,
//                mFormat.format(mCalendar.getTime()));

        //레이아웃 클릭하면 홈페이지로 이동동
//        Intent intent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://www.naver.com/"));
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                intent, 0);
        updateViews.setOnClickPendingIntent(R.id.record, pendingIntent);

        //위젯 업데이트
        appWidgetManager.updateAppWidget(appWidgetId, updateViews);
    }



    @Override
    public void onEnabled(Context context) {
        //위젯이 처음 생성될 때 호출되는 메소드
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        //위젯의 마지막 인스턴스가 제거될 때 호출되는 메소드
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        //위젯이 사용자에 의해 제거될 때 호출되는 메소드
        super.onDeleted(context, appWidgetIds);
    }
}