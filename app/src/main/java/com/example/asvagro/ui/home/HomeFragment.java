package com.example.asvagro.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.asvagro.CustomAdapter;
import com.example.asvagro.HackSmileModelClass;
import com.example.asvagro.ImageAdapter;
import com.example.asvagro.MyAdapter;
import com.example.asvagro.R;
import com.example.asvagro.Tutorial;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    ListView listView;
    private static final String JSON_URL = "https://asvagro.com/wp-json/wp/v2/product";
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
        listView = (ListView)view.findViewById(R.id.listView);
        tutorialList = new ArrayList<>();
        //this method will fetch and parse the data
        loadTutorialList();
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPage);
        ImageAdapter adapterView = new ImageAdapter(getActivity());
        mViewPager.setAdapter(adapterView);
        ArrayList<HackSmileModelClass> items = new ArrayList<>();
        CustomAdapter adapter = new CustomAdapter(getActivity(), items);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(adapter);

// let’s create 10 random items

        //for (int i = 0; i < items.size(); i++) {
        items.add(new HackSmileModelClass(R.drawable.fruits, "Fruits " ));
        items.add(new HackSmileModelClass(R.drawable.veg, "Vegetables " ));
        items.add(new HackSmileModelClass(R.drawable.dairy, "Dairy Products " ));
        items.add(new HackSmileModelClass(R.drawable.grocery, "Groceries " ));

        adapter.notifyDataSetChanged();
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
                                Tutorial tutorial = new Tutorial(tutorialsObject.getJSONObject("title").getString("rendered"), tutorialsObject.getString("link"),tutorialsObject.getString("_price"));

                                //adding the tutorial to tutoriallist
                                tutorialList.add(tutorial);
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