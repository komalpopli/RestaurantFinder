package com.example.komal.restaurantfinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncTaskFetchPlaces extends AsyncTask<String,String,List<PlaceItem>> {
    public static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json";
    private Context activityContext;
    RecyclerView recyclerView;
    JSONObject jsonObject;

    private JSONParser jsonParser = new JSONParser();
    private List<PlaceItem> mItems = new ArrayList<PlaceItem>();
    public AsyncTaskFetchPlaces(Context activityContext,RecyclerView recyclerView){
      this.activityContext=activityContext;
      this.recyclerView=recyclerView;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressDialog progressDialog=new ProgressDialog(activityContext);
        progressDialog.setMessage("Loading Places");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected List<PlaceItem> doInBackground(String... args) {
        String pName,pAddress,pPhone,pWebsite;

        HashMap<String, String> params = new HashMap<>();
        params.put("location", args[0]+","+args[1]);
        params.put("key","AIzaSyDb7dWcxqkiOMODHB_4n_c__lIZC1gRoCE");
        params.put("radius",args[2]);
        params.put("type",args[3]);
        try {
             jsonObject = jsonParser.makeHttpRequest(PLACES_SEARCH_URL, "GET", params);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            if(jsonObject.getString("status").equals("OK")){
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            if(jsonArray.length()>0){
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    pName=jsonObject1.getString("name");
                    pAddress=jsonObject1.getString("vicinity");
                    pWebsite=jsonObject1.getString("website");
                    pPhone=jsonObject1.getString("formatted_phone_number");
                    mItems.add(new PlaceItem(pName,pAddress,pPhone,pWebsite));
                }
            }
        }
            else if(jsonObject.getString("status").equals("ZERO_RESULTS")){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mItems;
    }

    @Override
    protected void onPostExecute(List<PlaceItem> placeItems) {
        super.onPostExecute(placeItems);
        PlacesRecyclerViewAdapter placesRecyclerViewAdapter=new PlacesRecyclerViewAdapter(placeItems,activityContext);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(activityContext);
        recyclerView.setAdapter(placesRecyclerViewAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

    }
}
