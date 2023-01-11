package com.kelompok6.uascovidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import javax.security.auth.callback.Callback;

public class CountriesDetail extends AppCompatActivity {
    TextView NameDetail, ConfirmedDetail, DeathDetail, RecoveredDetail;
    PieChart pieChart;
    Button btnReturn;
    WebView wv;
    String Flag;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries_detail);

        NameDetail = (TextView) findViewById(R.id.countrynameDetail);
        ConfirmedDetail = (TextView) findViewById(R.id.totalconfirmedDetail);
        DeathDetail     = (TextView) findViewById(R.id.totaldeathDetail);
        RecoveredDetail = (TextView) findViewById(R.id.totalrecoveredDetail);
        pieChart = (PieChart) findViewById(R.id.detailpie);
        btnReturn = (Button) findViewById(R.id.btnReturn);

        //passing parameter
        Intent intent = getIntent();
        NameDetail.setText(intent.getStringExtra("country_name"));

        Flag = intent.getStringExtra("country_flag");

        wv = (WebView) findViewById(R.id.webview);
        wv.setWebViewClient(new Callback());
        WebSettings webSettings = wv.getSettings();
        webSettings.setBuiltInZoomControls(false);

        wv.loadUrl(Flag);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        //call getDetailCovid
        getCovidCountryDetail();

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return (false);
        }
    }

    private void getCovidCountryDetail() {
        String url = "https://disease.sh/v3/covid-19/countries/" + NameDetail.getText().toString().toLowerCase().trim();
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    ConfirmedDetail.setText(jsonObject.getString("cases"));
                    DeathDetail.setText(jsonObject.getString("deaths"));
                    RecoveredDetail.setText(jsonObject.getString("recovered"));

                    setPieChart();

                    ConfirmedDetail.setText(shortenInt(Integer.parseInt(jsonObject.getString("cases"))));
                    DeathDetail.setText(shortenInt(Integer.parseInt(jsonObject.getString("deaths"))));
                    RecoveredDetail.setText(shortenInt(Integer.parseInt(jsonObject.getString("recovered"))));

                } catch (JSONException e) {
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

    private void setPieChart() {
        pieChart.addPieSlice(
                new PieModel(
                        "Cases",
                        Integer.parseInt(ConfirmedDetail.getText().toString()),
                        Color.parseColor("#FF9800")
                )
        );
        pieChart.addPieSlice(
                new PieModel(
                        "Death",
                        Integer.parseInt(DeathDetail.getText().toString()),
                        Color.parseColor("#F44336")
                )
        );
        pieChart.addPieSlice(
                new PieModel(
                        "Recovered",
                        Integer.parseInt(RecoveredDetail.getText().toString()),
                        Color.parseColor("#11D319")
                )
        );
        pieChart.startAnimation();
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