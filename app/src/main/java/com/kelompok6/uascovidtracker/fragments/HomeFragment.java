package com.kelompok6.uascovidtracker.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kelompok6.uascovidtracker.R;


import org.json.JSONObject;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private TextView tvTotalCase, tvTotalDeath, tvTotalRecovered, tvLastUpdated;
    private PieChart Piechart;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedFragment = inflater.inflate(R.layout.fragment_home, container, false);

        tvTotalCase = inflatedFragment.findViewById(R.id.txtTotalCase);
        tvTotalDeath = inflatedFragment.findViewById(R.id.txtTotalDeath);
        tvTotalRecovered = inflatedFragment.findViewById(R.id.txtTotalRecovered);
        tvLastUpdated = inflatedFragment.findViewById(R.id.txtLastUpdated);

        Piechart = inflatedFragment.findViewById(R.id.piechart);

        getData();

        return inflatedFragment;
    }

    private void setPieChart() {
        Piechart.addPieSlice(
                new PieModel(
                        "Cases",
                        Integer.parseInt(tvTotalCase.getText().toString()),
                        Color.parseColor("#FF9800")
                )
        );
        Piechart.addPieSlice(
                new PieModel(
                        "Death",
                        Integer.parseInt(tvTotalDeath.getText().toString()),
                        Color.parseColor("#F44336")
                )
        );
        Piechart.addPieSlice(
                new PieModel(
                        "Recovered",
                        Integer.parseInt(tvTotalRecovered.getText().toString()),
                        Color.parseColor("#11D319")
                )
        );
        Piechart.startAnimation();
    }

    private String getLastUpdated(Long miliseconds) {
        SimpleDateFormat formatDate = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss aaa");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(miliseconds);
        return formatDate.format(calendar.getTime());
    }

    private void getData() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String URL = "https://disease.sh/v3/covid-19/continents/asia";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject data = new JSONObject(response.toString());
                    tvTotalCase.setText(data.getString("cases"));
                    tvTotalDeath.setText(data.getString("deaths"));
                    tvTotalRecovered.setText(data.getString("recovered"));
                    tvLastUpdated.setText("Last updated "+getLastUpdated(data.getLong("updated")));

                    setPieChart();

                    tvTotalCase.setText(shortenInt(Integer.parseInt(data.getString("cases"))));
                    tvTotalDeath.setText(shortenInt(Integer.parseInt(data.getString("deaths"))));
                    tvTotalRecovered.setText(shortenInt(Integer.parseInt(data.getString("recovered"))));
                    tvLastUpdated.setText("Last updated "+getLastUpdated(data.getLong("updated")));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error response", error.toString());
            }
        });

        queue.getCache().clear();
        queue.add(stringRequest);

    }

    public String shortenInt(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.0").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat("#,##0").format(numValue);
        }
    }
}