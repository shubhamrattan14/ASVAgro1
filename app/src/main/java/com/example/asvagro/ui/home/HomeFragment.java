package com.example.asvagro.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.asvagro.CustomAdapter;
import com.example.asvagro.HackSmileModelClass;
import com.example.asvagro.ImageAdapter;
import com.example.asvagro.MyAdapter;
import com.example.asvagro.ProductLoadActivity;
import com.example.asvagro.R;
import com.example.asvagro.Tutorial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

CardView fruit,diary,grocery, vegetable ;
    GridView listView;
    private static final String JSON_URL = "https://asvagro.com/wp-json/wp/v2/product";
    private static final String FRUIT_URL = "https://asvagro.com/wp-json/wp/v2/product";
    private static final String DIARY_URL = "https://asvagro.com/wp-json/wp/v2/product";
    private static final String GROCERY_URL = "https://asvagro.com/wp-json/wp/v2/product";
    private static final String VEGETABLE_URL = "https://asvagro.com/wp-json/wp/v2/media/";
    //the tutorial list where we will store all the tutorial objects after parsing json
    List<Tutorial> tutorialList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (GridView) view.findViewById(R.id.listView);
        fruit =  view.findViewById(R.id.fruit);
        vegetable =  view.findViewById(R.id.vegetable);
        diary = view.findViewById(R.id.dairy);
        grocery =  view.findViewById(R.id.grocery);
        tutorialList = new ArrayList<>();
        //this method will fetch and parse the data
        loadTutorialList();
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ProductLoadActivity.class);
                i.putExtra("link",FRUIT_URL);
                startActivity(i);
            }
        });
        diary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ProductLoadActivity.class);
                i.putExtra("link",DIARY_URL);
                startActivity(i);
            }
        });
        grocery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ProductLoadActivity.class);
                i.putExtra("link",GROCERY_URL);
                startActivity(i);
            }
        });
        vegetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ProductLoadActivity.class);
                i.putExtra("link",VEGETABLE_URL);
                startActivity(i);
            }
        });
    }

    private void loadTutorialList() {
//        //getting the progressbar
        final ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL,
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
                                try {
                                    Tutorial tutorial = new Tutorial(tutorialsObject.getJSONObject("title").getString("rendered"), tutorialsObject.getString("link"),"12");

                                    //adding the tutorial to tutoriallist
                                    tutorialList.add(tutorial);
                                }
                                catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("myname",e.getMessage());
                                    }
                            }

                            //creating custom adapter object
                            MyAdapter adapter = new MyAdapter(tutorialList,getActivity(). getApplicationContext());

                            //adding the adapter to listview
                            listView.setAdapter(adapter);

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
                        Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);



    }
}