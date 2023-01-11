package com.kelompok6.uascovidtracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kelompok6.uascovidtracker.CountriesDetail;
import com.kelompok6.uascovidtracker.R;
import com.kelompok6.uascovidtracker.model.CountriesModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/* Adapter to display items in RecyclerView */

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {
    ArrayList<CountriesModel> mArrayCountries;
    ArrayList<CountriesModel> Array;
    Context context;

    public CountriesAdapter(ArrayList<CountriesModel> mArrayCountries, Context context) {
        this.mArrayCountries = mArrayCountries;
        this.context = context;
        this.Array = new ArrayList<>();
        this.Array.addAll(mArrayCountries);
    }

    @NonNull
    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_list_item,parent,false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull CountriesAdapter.ViewHolder holder, int position) {

        CountriesModel countriesModel = mArrayCountries.get(position);

        holder.tvCountryName.setText(countriesModel.getmCountry());
        holder.tvCountryCase.setText(shortenInt(Integer.parseInt(countriesModel.getmCase())));
        holder.tvCountryDeath.setText(shortenInt(Integer.parseInt(countriesModel.getmDeath())));
        holder.tvCountryRecovered.setText(shortenInt(Integer.parseInt(countriesModel.getmRecovered())));
        Glide.with(context).load(countriesModel.getmFlag()).apply(new RequestOptions()).override(160, 160).into(holder.imgCountryFlag);



        holder.tvCountryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CountriesDetail.class);

                //send parameters to CountryCovidDetail Activity
                intent.putExtra("country_name", countriesModel.getmCountry());
                intent.putExtra("country_flag", countriesModel.getmFlag());

                context.startActivity(intent);


            }
        });

        holder.imgCountryFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CountriesDetail.class);

                //send parameters to CountryCovidDetail Activity
                intent.putExtra("country_name", countriesModel.getmCountry());
                intent.putExtra("country_flag", countriesModel.getmFlag());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayCountries.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCountryName, tvCountryCase, tvCountryDeath, tvCountryRecovered;
        ImageView imgCountryFlag;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCountryName = itemView.findViewById(R.id.txtCountryName);
            tvCountryCase = itemView.findViewById(R.id.txtCountryCase);
            tvCountryDeath = itemView.findViewById(R.id.txtCountryDeath);
            tvCountryRecovered = itemView.findViewById(R.id.txtCountryRecovered);
            imgCountryFlag = itemView.findViewById(R.id.imgCountryFlag);

        }
    }

    public void searchfilter(String TextAlphabet) {
        TextAlphabet = TextAlphabet.toLowerCase(Locale.getDefault());
        mArrayCountries.clear();
        if (TextAlphabet.length() == 0) {
            mArrayCountries.addAll(Array);
        }
        else {
            for (CountriesModel ap : Array) {
                if (ap.getmCountry().toLowerCase(Locale.getDefault()).contains(TextAlphabet)) {
                    mArrayCountries.add(ap);
                }
            }
        }
        notifyDataSetChanged();
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
