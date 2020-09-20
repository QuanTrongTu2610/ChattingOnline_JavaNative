package com.example.chattingonlineapplication.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingonlineapplication.HandleEvent.ICountryListClickListener;
import com.example.chattingonlineapplication.R;
import com.example.chattingonlineapplication.Webservice.Model.CountryModel;

import java.util.ArrayList;
import java.util.List;

public class ListCountryAdapter extends RecyclerView.Adapter implements Filterable {

    private static final int RESULT_COUNTRY_CODE = 200;

    private ArrayList<CountryModel> lstCountry;
    private ArrayList<CountryModel> lstCountryFilter;
    private Context context;

    public ListCountryAdapter(Context context, ArrayList<CountryModel> lstCountry) {
        this.lstCountry = lstCountry;
        this.context = context;
        this.lstCountryFilter = new ArrayList<>(lstCountry);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.country_code_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final CountryModel country = lstCountry.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvCountryCode.setText("+" + String.valueOf(country.getCallingCodes()[0]));
        viewHolder.tvCountryName.setText(country.getName());
        viewHolder.setClickListener(new ICountryListClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Long Click: " + lstCountry.get(position).getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Intent data = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("countryObject", lstCountry.get(position));
                    data.putExtras(bundle);
                    ((Activity) context).setResult(RESULT_COUNTRY_CODE, data);
                    ((Activity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstCountry.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<CountryModel> filteredList = new ArrayList<>();
                if (charSequence.length() == 0 || charSequence == null) {
                    filteredList.addAll(lstCountryFilter);
                } else {
                    String filter = charSequence.toString().toLowerCase().trim();
                    for (CountryModel item : lstCountryFilter) {
                        if (item.getName().toLowerCase().contains(filter)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                lstCountry.clear();
                lstCountry.addAll((ArrayList<CountryModel>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }


    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public View layoutCountryItem;
        public TextView tvCountryName;
        public TextView tvCountryCode;
        private ICountryListClickListener iCountryListClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvCountryName = itemView.findViewById(R.id.tvCountryName);
            this.tvCountryCode = itemView.findViewById(R.id.tvCountryCode);
            this.layoutCountryItem = itemView.findViewById(R.id.layoutCountryItem);

            layoutCountryItem.setOnClickListener(this);
            layoutCountryItem.setOnLongClickListener(this);
        }

        public void setClickListener(ICountryListClickListener iCountryListClickListener) {
            this.iCountryListClickListener = iCountryListClickListener;
        }

        @Override
        public void onClick(View view) {
            iCountryListClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            iCountryListClickListener.onClick(view, getAdapterPosition(), true);
            return true;
        }
    }
}
