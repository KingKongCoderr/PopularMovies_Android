package com.example.nandeesh_randomaspirer.popularmovies;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= super.onCreateView(inflater, container, savedInstanceState);

        list=new ArrayList<Movie>();
        mgridView=(GridView)view.findViewById(R.id.grid_view);
        mAdapter= new ArrayAdapter<Movie>(getContext(),R.layout.grid_item,R.id.grid_item_imgview,list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v= super.getView(position, convertView, parent);

                ImageView imgView=(ImageView)v.findViewById(R.id.grid_item_imgview);




                return v;
            }
        };



        return view;
    }



    public void loadImages(){
        FetchImagesTask backgroundtask=new FetchImagesTask();
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(getActivity());
        String order_url=pref.getString(getString(R.string.pref_sortingOrder_key),getString(R.string.pref_nowPlaying));
        backgroundtask.execute(order_url);

    }

    public class FetchImagesTask extends AsyncTask<String,Void,Movie[]>{

        public final String Log_tag= FetchImagesTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String... params) {
           //ArrayList<Movie> movies_list=new ArrayList<>();
            BufferedReader reader=null;
            HttpURLConnection urlConnection=null;

            String movieJsonstr="";

            final String api_key="031db1b9b1671f6a20497703858dc72f";
            final String baseUrl="http://api.themoviedb.org/3/";


            try{

              //  URI builturi= Uri.parse(base_url).buildUpon().appendQueryParameter()
                URL url=new URL(baseUrl+params[0]+"api_key="+api_key);
                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream input=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                if(input==null){
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(input));

                String line;
                while((line=reader.readLine())!=null){
                    //Log.d("deb","inside while");
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0){
                    return null;
                }
                movieJsonstr=buffer.toString();

            }catch (Exception e){
                Log.d("network code","Error in network code");
            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try{
                        reader.close();}catch (IOException e){
                        Log.e("buffered reader","Error closing reader",e);
                    }
                }
            }



            try {
                return getMoviesFromJson(movieJsonstr);
            }catch (JSONException e){
                Log.e(Log_tag,e.getMessage(),e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {



        }
        private Movie[] getMoviesFromJson(String jsonData)throws JSONException{
            ArrayList<Movie> movies=new ArrayList<>();

            JSONObject jsonInput=new JSONObject(jsonData);
            JSONArray  resultsArray= jsonInput.getJSONArray("results");
            for(int i=0;i<resultsArray.length();i++){


                JSONObject movie_obj= resultsArray.getJSONObject(i);

                String img_url;
                String plot_synopsis;
                String release_date;
                String title;
                double rating;

                img_url= movie_obj.getJSONObject("poster_path").toString();
                plot_synopsis=movie_obj.getJSONObject("overview").toString();
                release_date=movie_obj.getJSONObject("release_date").toString();
                title=movie_obj.getJSONObject("title").toString();
                rating=Double.parseDouble(movie_obj.getJSONObject("vote_average").toString());







            }





            Movie[] movies_array=new Movie[movies.size()];
            movies_array= movies.toArray(movies_array);
            return movies_array;
        }
    }

}
