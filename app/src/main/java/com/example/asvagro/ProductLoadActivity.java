package com.example.asvagro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductLoadActivity extends AppCompatActivity {
String url;
GridView gridView;
    List<Tutorial> tutorialList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load);

        url=getIntent().getStringExtra("link");
        gridView = findViewById(R.id.listView);
        tutorialList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTutorialList();
    }

    private void loadTutorialList() {
//        //getting the progressbar
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.INVISIBLE);


                        try {
                            //getting the whole json object from the response
//                            JSONObject obj = new JSONObject(response);

                            //we have the array named tutorial inside the object
                            //so here we are getting that json array
                            JSONArray tutorialsArray = new JSONArray(response);

                            //now looping through all the elements of the json array
                            for (int i = 0; i < tutorialsArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject tutorialsObject = tutorialsArray.getJSONObject(i);

                                //creating a tutorial object and giving them the values from json object
                                Tutorial tutorial = new Tutorial(tutorialsObject.getJSONObject("title").getString("rendered"), tutorialsObject.getJSONObject("guid").getString("rendered"),tutorialsObject.getString("id"));

                                //adding the tutorial to tutoriallist
                                tutorialList.add(tutorial);
                            }

                            //creating custom adapter object
                            MyAdapter adapter = new MyAdapter(tutorialList, getApplicationContext());

                            //adding the adapter to listview
                            gridView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("myname",e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occur
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}