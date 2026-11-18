package com.example.pookiplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pookiplanner.R;
import com.example.pookiplanner.models.CoffeeHouse;

import java.util.ArrayList;
import java.util.List;

public class CoffeeHouseAdapter extends RecyclerView.Adapter<CoffeeHouseAdapter.Holder> {

    private List<CoffeeHouse> items = new ArrayList<>();

    public void setItems(List<CoffeeHouse> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coffee, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int position) {
        CoffeeHouse c = items.get(position);
        h.name.setText(c.getName());
        h.area.setText(c.getArea());
        h.hours.setText(c.getHours());
        h.rating.setRating(c.getRating());
        h.wifi.setVisibility(c.isHasWifi() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        TextView name, area, hours, wifi;
        RatingBar rating;
        Holder(View v) {
            super(v);
            name = v.findViewById(R.id.textCoffeeName);
            area = v.findViewById(R.id.textCoffeeArea);
            hours = v.findViewById(R.id.textCoffeeHours);
            wifi = v.findViewById(R.id.textWifi);
            rating = v.findViewById(R.id.ratingCoffee);
        }
    }
}
