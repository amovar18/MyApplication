package com.example.amogha.newsupdates;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class cov_india extends Fragment {
    private String TAG = cov_india.class.getSimpleName();
    ArrayList<HashMap<String, String>> datalist;
    private ProgressDialog pDialog;
    TextView c,a,r,d;
    private ListView lv;
    private SwipeRefreshLayout swipe;
    String state;
    int deaths,active,confirmed,recovered,total_deaths=0,total_recovered=0,total_confirmed=0,total_active=0;
    // URL to get contacts JSON
    private static String url = "https://api.covid19india.org/data.json";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.cov_india_fragment,container,false);
        datalist = new ArrayList<>();
        lv = (ListView) view.findViewById(R.id.india_list_item);
        swipe=(SwipeRefreshLayout)view.findViewById(R.id.cov_india_fragment);
        new getdata().execute();
        c = (TextView) view.findViewById(R.id.tc);
        a = (TextView) view.findViewById(R.id.ta);
        r = (TextView) view.findViewById(R.id.tr);
        d = (TextView) view.findViewById(R.id.td);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Covid-19 India");
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                total_deaths=total_recovered=total_confirmed=total_active=0;
                new cov_india.getdata().execute();
                swipe.setRefreshing(false);
            }
        });
    }

    private class getdata extends AsyncTask<Void, Void, Void> {
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
                    JSONArray data = jsonObj.getJSONArray("statewise");
                    // looping through All Contacts
                    for (int i = 1; i < data.length(); i++) {
                        JSONObject c = data.getJSONObject(i);

                        state = c.getString("state");
                        active = c.getInt("active");
                        confirmed= c.getInt("confirmed");
                        recovered = c.getInt("recovered");
                        deaths = c.getInt("deaths");
                        total_deaths= total_deaths+deaths;
                        total_recovered =total_recovered+recovered;
                        total_active=total_active+active;
                        total_confirmed=total_deaths+total_recovered+total_active;
                        HashMap<String, String> statewise_data= new HashMap<>();

                        // adding each child node to HashMap key => value
                        statewise_data.put("state", state);
                        statewise_data.put("confirm", ""+confirmed);
                        statewise_data.put("active", ""+active);
                        statewise_data.put("recovered","" +recovered);
                        statewise_data.put("deaths", ""+deaths);

                        // adding contact to contact list
                        datalist.add(statewise_data);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {Toast.makeText(getActivity(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(getActivity(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
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
            ListAdapter adapter = new SimpleAdapter(getActivity(), datalist, R.layout.listitem, new String[]{"state", "confirm", "active","recovered","deaths"}, new int[]{R.id.state, R.id.confirm, R.id.active,R.id.recovered,R.id.death});
            lv.setAdapter(adapter);
            c.setText(String.valueOf(total_confirmed));
            a.setText(String.valueOf(total_active));
            r.setText(String.valueOf(total_recovered));
            d.setText(String.valueOf(total_deaths));
        }
    }
}


