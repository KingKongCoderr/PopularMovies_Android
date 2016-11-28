package com.example.nandeesh_randomaspirer.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();


    }


    public static class SettingsFragment extends PreferenceFragment  {

        private SharedPreferences.OnSharedPreferenceChangeListener pref_chg_listener;
        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(pref_chg_listener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(pref_chg_listener);
        }


        private void pickPreferenceObject(Preference p) {
            if (p instanceof PreferenceCategory) {
                PreferenceCategory cat = (PreferenceCategory) p;
                for (int i = 0; i < cat.getPreferenceCount(); i++) {
                    pickPreferenceObject(cat.getPreference(i));
                }
            } else {
                initSummary(p);
            }
        }

        private void initSummary(Preference p) {

            if (p instanceof EditTextPreference) {
                EditTextPreference editTextPref = (EditTextPreference) p;
                p.setSummary(editTextPref.getText());
            }

            if(p instanceof ListPreference){
                Object value= ((ListPreference) p).getValue();
                String stringValue= value.toString();
                int index= ((ListPreference) p).findIndexOfValue(stringValue);
                if(index>=0){
                    p.setSummary(((ListPreference) p).getEntries()[index]);
                }
            }
            // More logic for ListPreference, etc...
        }




        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(getActivity());

            for(int i=0;i<getPreferenceScreen().getPreferenceCount();i++){
                pickPreferenceObject(getPreferenceScreen().getPreference(i));
            }
            pref_chg_listener= new SharedPreferences.OnSharedPreferenceChangeListener(){

                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                   // Log.d("inside","inside the listener with key:"+String.valueOf(R.string.pref_location_key));
                    //Preference pref=findPreference(key);
                   // pref.setSummary(sharedPreferences.getString(key, String.valueOf(R.string.pref_location_default)));
                    /*if(pref instanceof ListPreference){
                        ListPreference list_pref=(ListPreference) pref;
                        list_pref.setSummary(sharedPreferences.getString(key, String.valueOf(R.string.pref_location_default)));
                    }*/

                   /* if(key.equals("location")){
                        String val=sharedPreferences.getString(key, String.valueOf(R.string.pref_location_default));
                        Preference location_pref= findPreference(key);
                        location_pref.setSummary(sharedPreferences.getString(key, String.valueOf(R.string.pref_location_default)));
                   Log.d("inside","inside the listener with summary value:"+val);
                    }*/
                    if(key.equals("order")){
                        String stringValue= sharedPreferences.getString(key, String.valueOf(R.string.pref_nowPlaying));
                        Preference p=findPreference(key);
                        int index= ((ListPreference) p).findIndexOfValue(stringValue);
                        if(index>=0){
                            p.setSummary(((ListPreference) p).getEntries()[index]);
                        }
                    }



                }
            };
           // prefs.registerOnSharedPreferenceChangeListener(pref_chg_listener);

        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
             View view= super.onCreateView(inflater, container, savedInstanceState);
            return view;
        }


       /* @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d("inside","inside the implements listener");
            if(key.equals(R.string.pref_location_key)){
                String val=sharedPreferences.getString(key, String.valueOf(R.string.pref_location_default));
                Preference location_pref= findPreference(key);
                location_pref.setSummary(sharedPreferences.getString(key, String.valueOf(R.string.pref_location_default)));
                Log.d("inside","inside the listener with summary value:"+val);
            }

        }*/
    }
}
