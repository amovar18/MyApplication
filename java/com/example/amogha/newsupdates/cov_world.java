package com.example.amogha.newsupdates;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class cov_world extends android.support.v4.app.Fragment {
    private String TAG = cov_world.class.getSimpleName();
    ArrayList<HashMap<String, String>> datalist;
    private ProgressDialog pDialog;
    TextView c, r, d,nc,nr,nd;
    private ListView lv;
    SwipeRefreshLayout swipe;
    String country, total_deaths ,total_recovered , total_confirmed  ;
    String world_confirmed_new,world_confirmed,world_recovered_new,world_recovered,world_deaths_new,world_deaths;
    // URL to get contacts JSON
    private static String url = "https://api.covid19api.com/summary";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.cov_world_fragment,container,false);
        datalist = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.world_list_item);
        swipe=(SwipeRefreshLayout) view.findViewById(R.id.cov_world_fragment);
        new cov_world.worldgetdata().execute();
        c = (TextView) view.findViewById(R.id.wc);
        r = (TextView) view.findViewById(R.id.wr);
        d = (TextView) view.findViewById(R.id.wd);
        nc = (TextView) view.findViewById(R.id.wcn);
        nr = (TextView) view.findViewById(R.id.wrn);
        nd = (TextView) view.findViewById(R.id.wdn);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Covid-19 World");
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new cov_world.worldgetdata().execute();
                swipe.setRefreshing(false);
            }
        });
    }

    private class worldgetdata extends AsyncTask<Void, Void, Void> {
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
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONObject g = jsonObj.getJSONObject("Global");
                    world_confirmed=g.getString("TotalConfirmed");
                    world_confirmed_new=g.getString("NewConfirmed");;
                    world_recovered=g.getString("TotalRecovered");;
                    world_recovered_new=g.getString("NewRecovered");;
                    world_deaths=g.getString("TotalDeaths");;
                    world_deaths_new=g.getString("NewDeaths");;
                    JSONArray data = jsonObj.getJSONArray("Countries");
                    // looping through All Contacts
                    for (int i = 1; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        country = c.getString("Country");
                        total_confirmed = c.getString("TotalConfirmed");
                        total_deaths = c.getString("TotalDeaths");
                        total_recovered = c.getString("TotalRecovered");
                        HashMap<String, String> countrywise_data = new HashMap<>();

                        // adding each child node to HashMap key => value
                        countrywise_data.put("country",country);
                        countrywise_data.put("total_confirmed"," " +total_confirmed);
                        countrywise_data.put("total_recovered"," "+ total_recovered);
                        countrywise_data.put("total_deaths", " "+ total_deaths);
                        // adding contact to contact list
                        datalist.add(countrywise_data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            //Updating parsed JSON data into ListView
            ListAdapter adapter = new SimpleAdapter(getActivity(), datalist, R.layout.worldlistitem, new String[]{"country", "total_confirmed","total_recovered","total_deaths"}, new int[]{R.id.country, R.id.wconfirm, R.id.wrecovered,R.id.wdeath});
            lv.setAdapter(adapter);
            nc.setText("+"+world_confirmed_new);
            nr.setText("+"+world_recovered_new);
            nd.setText("+"+world_deaths_new);
            c.setText(world_confirmed);
            r.setText(world_recovered);
            d.setText(world_deaths);
        }
    }
}
