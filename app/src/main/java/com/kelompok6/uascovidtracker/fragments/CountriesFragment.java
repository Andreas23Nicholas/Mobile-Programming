package com.kelompok6.uascovidtracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompok6.uascovidtracker.R;
import com.kelompok6.uascovidtracker.adapter.CountriesAdapter;
import com.kelompok6.uascovidtracker.model.CountriesModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CountriesFragment extends Fragment {

    RecyclerView countryRecyclerView;
    ArrayList<CountriesModel> mArrayCountries = new ArrayList<>();
    private static final String TAG = CountriesFragment.class.getSimpleName();

    CountriesAdapter countriesAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedFragment = inflater.inflate(R.layout.fragment_countries, container, false);

        countryRecyclerView = inflatedFragment.findViewById(R.id.rvCountries);
        countryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Setupbar();
        getCountryInfo();

        return inflatedFragment;
    }

    private void Setupbar() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu);
                MenuItem searchItem = menu.findItem(R.id.search);

                SearchView searchv = new SearchView(getActivity());
                searchv.setQueryHint("search country name");
                searchv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if (countriesAdapter!=null)
                        {
                            countriesAdapter.searchfilter(s);
                        }

                        return false;
                    }
                });
                searchItem.setActionView(searchv);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sortAsc:
                        Toast.makeText(getContext(), "Sort Ascending", Toast.LENGTH_SHORT).show();
                        mArrayCountries.clear();
                        getDataApiCountriesCasesSortAsc();
                        break;

                    case R.id.sortDesc:
                        Toast.makeText(getContext(), "Sort Descending", Toast.LENGTH_SHORT).show();
                        mArrayCountries.clear();
                        getDataApiCountriesCasesSortDesc();

                        break;
                }
                return true;
            }
        },getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }


    private void getCountryInfo() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String URL = "https://disease.sh/v3/covid-19/countries";

        mArrayCountries = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse" + response);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject info = data.getJSONObject("countryInfo");

                            if (data.getString("continent").equals("Asia")) {
                                mArrayCountries.add(new CountriesModel(data.getString("country"), data.getString("cases"), data.getString("deaths"), data.getString("recovered"), info.getString("flag")));
                            }

                            showRecycleView();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse" + error);
            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);

    }

    private void showRecycleView() {

        countriesAdapter = new CountriesAdapter(mArrayCountries,getActivity());
        countryRecyclerView.setAdapter(countriesAdapter);

    }

    private void getDataApiCountriesCasesSortAsc() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String URL = "https://disease.sh/v3/covid-19/countries";

        mArrayCountries = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse" + response);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject info = data.getJSONObject("countryInfo");

                            if (data.getString("continent").equals("Asia")) {
                                mArrayCountries.add(new CountriesModel(data.getString("country"), data.getString("cases"), data.getString("deaths"), data.getString("recovered"), info.getString("flag")));
                            }
                            Collections.sort(mArrayCountries, new Comparator<CountriesModel>() {
                                @Override
                                public int compare(CountriesModel countriesModel, CountriesModel t1) {
                                    return countriesModel.getmCountry().compareToIgnoreCase(t1.getmCountry());
                                }
                            });
                            showRecycleView();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse" + error);
            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    private void getDataApiCountriesCasesSortDesc() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String URL = "https://disease.sh/v3/covid-19/countries";

        mArrayCountries = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse" + response);
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject info = data.getJSONObject("countryInfo");

                            if (data.getString("continent").equals("Asia")) {
                                mArrayCountries.add(new CountriesModel(data.getString("country"), data.getString("cases"), data.getString("deaths"), data.getString("recovered"), info.getString("flag")));
                            }
                            Collections.reverse(mArrayCountries);
                            showRecycleView();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onResponse" + error);
            }
        });
        queue.getCache().clear();
        queue.add(stringRequest);
    }


}