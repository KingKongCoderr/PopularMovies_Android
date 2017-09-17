package com.example.nandeesh_randomaspirer.popularmovies.widget;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.nandeesh_randomaspirer.popularmovies.Movie;
import com.example.nandeesh_randomaspirer.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by nande on 9/16/2017.
 */

public class WidgetService extends RemoteViewsService {
    
    
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetServiceFactory(getApplicationContext());
    }
    
    public class WidgetServiceFactory implements RemoteViewsFactory, LoaderManager.LoaderCallbacks{
        
        Context context;
        String img_url = "https://images-na.ssl-images-amazon.com/images/I/91wP24TfG1L._SY445_.jpg";
        String img_url2 = "https://images-na.ssl-images-amazon.com/images/M/MV5BNDMxNTQ0NjIwOV5BMl5BanBnXkFtZTgwODE5NjA5MjI@._V1_UX182_CR0,0,182,268_AL_.jpg";
        Movie dummy_array[] = {new Movie("Android",img_url,"programming consistently",5.0,
                "releasing soon",null),
                new Movie("Android",img_url2,"programming consistently",5.0,
                        "releasing soon",null)
        };
        
    
        public WidgetServiceFactory(Context context) {
            this.context = context;
        }
    
        @Override
        public void onCreate() {
            
        }
    
        @Override
        public void onDataSetChanged() {
        
        }
    
        @Override
        public void onDestroy() {
        
        }
    
        @Override
        public int getCount() {
            return dummy_array.length;
        }
    
        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            try {
                Bitmap bitmap = Picasso.with(getBaseContext())
                        .load(dummy_array[i].getImg_url())
                        .get();
                remoteViews.setImageViewBitmap(R.id.item_iv,bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
    
            return remoteViews;
        }
    
        @Override
        public RemoteViews getLoadingView() {
            return null;
        }
    
        @Override
        public int getViewTypeCount() {
            return 1;
        }
    
        @Override
        public long getItemId(int i) {
            return i;
        }
    
        @Override
        public boolean hasStableIds() {
            return true;
        }
        
        //loader call backs
    
        @Override
        public Loader onCreateLoader(int i, Bundle bundle) {
            return null;
        }
    
        @Override
        public void onLoadFinished(Loader loader, Object o) {
        
        }
    
        @Override
        public void onLoaderReset(Loader loader) {
        
        }
    }
}
