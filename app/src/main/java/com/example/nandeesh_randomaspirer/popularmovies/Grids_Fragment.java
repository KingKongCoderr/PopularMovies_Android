package com.example.nandeesh_randomaspirer.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Grids_Fragment extends Fragment {
    GridView mgridView;
    ArrayAdapter<Movie> mAdapter;
    List<Movie> list;
    
    public Grids_Fragment() {
        
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent Settings_intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(Settings_intent);
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        if (isOnline())
            loadImages();
        else {
            Toast.makeText(getContext(), "No Network", Toast.LENGTH_LONG).show();
        }
        
    }
    
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);
        
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setHasOptionsMenu(true);
        
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grids, container, false);
        
        
        mgridView = (GridView) view.findViewById(R.id.grid_view);
        list = new ArrayList<Movie>();
        
        mAdapter = new ArrayAdapter<Movie>(getContext(), R.layout.grid_item, R.id.text_view, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                
                TextView txtView = (TextView) v.findViewById(R.id.text_view);
                ImageView imgView = (ImageView) v.findViewById(R.id.grid_item_imgview);
                
                
                String base_img_url = "http://image.tmdb.org/t/p/";
                String img_size = "w342";
                String img_path = list.get(position).getBackdrop_url();
                txtView.setText(list.get(position).getMov_title());
                Picasso.with(this.getContext()).load(base_img_url + img_size + img_path).into(imgView);
                
                
                return v;
            }
        };
        
        mgridView.setAdapter(mAdapter);
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
                Movie obj_detail = list.get(position);
                String detail_title = obj_detail.getMov_title();
                String detail_synopsis = obj_detail.getPlot_synopsis();
                double detail_rating = obj_detail.getRating();
                String detail_releasedate = obj_detail.getRelease_date();
                String detail_img_url = obj_detail.getImg_url();
                String detail_backdrop_url = obj_detail.getBackdrop_url();
                
                Intent detail_intent = new Intent(getActivity(), DetailActivity.class);
                detail_intent.putExtra("title", detail_title);
                detail_intent.putExtra("synopsis", detail_synopsis);
                detail_intent.putExtra("rating", detail_rating);
                detail_intent.putExtra("releasedate", detail_releasedate);
                detail_intent.putExtra("imgurl", detail_img_url);
                detail_intent.putExtra("backdrop", detail_backdrop_url);
                startActivity(detail_intent);
                
            }
        });
        return view;
    }
    
    
    public void loadImages() {
        FetchImagesTask backgroundtask = new FetchImagesTask();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order_url = pref.getString(getString(R.string.pref_sortingOrder_key), getString(R.string.pref_nowPlaying));
        Log.d("order", order_url);
        backgroundtask.execute(order_url);
        
    }
    
    public class FetchImagesTask extends AsyncTask<String, Void, Movie[]> {
        
        public final String Log_tag = FetchImagesTask.class.getSimpleName();
        
        @Override
        protected Movie[] doInBackground(String... params) {
            
            if (params.length == 0) {
                return null;
            }
            //ArrayList<Movie> movies_list=new ArrayList<>();
            BufferedReader reader = null;
            HttpURLConnection urlConnection = null;
            
            String movieJsonstr = "";
            
            String api_key = "031db1b9b1671f6a20497703858dc72f";
            String baseUrl = "http://api.themoviedb.org/3/";
            
            
            try {
                
                //  URI builturi= Uri.parse(base_url).buildUpon().appendQueryParameter()
                URL url = new URL(baseUrl + params[0] + "api_key=" + api_key);
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.d("params", url.toString());
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                
                InputStream input = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (input == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(input));
                
                String line;
                while ((line = reader.readLine()) != null) {
                    //Log.d("deb","inside while");
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonstr = buffer.toString();
                
            } catch (Exception e) {
                Log.d("network code", "Error in network code");
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e("buffered reader", "Error closing reader", e);
                    }
                }
            }
            
            
            try {
                Log.d("hello", movieJsonstr);
                return getMoviesFromJson(movieJsonstr);
            } catch (JSONException e) {
                Log.e(Log_tag, e.getMessage(), e);
            }
            return null;
        }
        
        @Override
        protected void onPostExecute(Movie[] movies) {
            
            if (movies != null) {
                mAdapter.clear();
                for (Movie obj : movies) {
                    mAdapter.add(obj);
                }
            }
            
            
        }
        
        private Movie[] getMoviesFromJson(String jsonData) throws JSONException {
            ArrayList<Movie> movies = new ArrayList<>();
            
            JSONObject jsonInput = new JSONObject(jsonData);
            JSONArray resultsArray = jsonInput.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                
                
                JSONObject movie_obj = resultsArray.getJSONObject(i);
                
                String img_url;
                String backdrop_url;
                String plot_synopsis;
                String release_date;
                String title;
                double rating;
                
                
                img_url = (movie_obj.getString("poster_path"));
                plot_synopsis = movie_obj.getString("overview");
                release_date = movie_obj.getString("release_date");
                title = movie_obj.getString("title");
                rating = Double.parseDouble(movie_obj.getString("vote_average"));
                backdrop_url = movie_obj.getString("backdrop_path");
                movies.add(new Movie(title, img_url, plot_synopsis, rating, release_date, backdrop_url));
                
                
            }
            
            
            Movie[] movies_array = new Movie[movies.size()];
            movies_array = movies.toArray(movies_array);
            return movies_array;
        }
    }
    
}
