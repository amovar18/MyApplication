package com.example.amogha.newsupdates;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

public class getnews extends android.support.v4.app.Fragment{
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<HashMap<String, String>> datalist;
    ArrayList<String>urllist;
    Context context;
    private ProgressDialog pDialog;
    String headline,excerpt,publish_time,news_url,news_provider;
    private ListView lv;
    SwipeRefreshLayout swipe;
    private static String url = "https://api.smartable.ai/coronavirus/news/US";
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        datalist=new ArrayList<HashMap<String, String>>();
        urllist=new ArrayList<String>();
        View view=inflater.inflate(R.layout.news_details,container,false);
        lv=(ListView) view.findViewById(R.id.news);
        context=getActivity();
        swipe=(SwipeRefreshLayout) view.findViewById(R.id.news_details_fragment);
        new getnewsdata().execute();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Covid-19 News");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urllist.get(position)));
                startActivity(intent);
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new getnewsdata().execute();
                swipe.setRefreshing(false);
            }
        });
    }
    private class getnewsdata extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            httphandler sh = new httphandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeService(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray data = jsonObj.getJSONArray("news");
                    // looping through All Contacts
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);
                        JSONObject p=c.getJSONObject("provider");
                        headline=c.getString("title");
                        news_provider=p.getString("name");
                        news_url=c.getString("webUrl");
                        excerpt=c.getString("excerpt");
                        publish_time=c.getString("publishedDateTime");
                        HashMap<String, String> news_data= new HashMap<>();

                        // adding each child node to HashMap key => value
                        news_data.put("headline", headline);
                        news_data.put("summary", excerpt);
                        news_data.put("publish_time", publish_time);
                        news_data.put("news_provider",news_provider);
                        urllist.add(news_url);
                        // adding contact to contact list
                        datalist.add(news_data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(context, "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            pDialog.dismiss();
            //Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(context, datalist, R.layout.news_card, new String[]{"headline","summary","publish_time","news_provider"}, new int[]{R.id.headline,R.id.summary,R.id.time,R.id.newsprovider});
            lv.setAdapter(adapter);
        }
    }
}