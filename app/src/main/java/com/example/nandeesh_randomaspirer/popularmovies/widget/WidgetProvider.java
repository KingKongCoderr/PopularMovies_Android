package com.example.nandeesh_randomaspirer.popularmovies.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.nandeesh_randomaspirer.popularmovies.R;

/**
 * Created by nande on 9/16/2017.
 */

public class WidgetProvider extends AppWidgetProvider {
    
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName component = new ComponentName(context, WidgetProvider.class);
        appWidgetManager.updateAppWidget(component , buildRemoteViews(context, appWidgetIds));
    }
    
    public RemoteViews buildRemoteViews(Context context, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        
        Intent serviceIntent = new Intent(context, WidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.movies_sv, serviceIntent);
        
        //setup intent template for handling click events.
        
        
        return remoteViews;
    }
    
}
