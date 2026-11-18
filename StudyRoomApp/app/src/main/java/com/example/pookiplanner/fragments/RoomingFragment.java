package com.example.pookiplanner.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pookiplanner.PookiPlannerApplication;
import com.example.pookiplanner.R;
import com.example.pookiplanner.activities.AddBookingActivity;
import com.example.pookiplanner.adapters.CoffeeHouseAdapter;
import com.example.pookiplanner.database.DataProvider;
import com.example.pookiplanner.database.DatabaseHelper;
import com.example.pookiplanner.models.User;
import com.google.android.material.button.MaterialButton;

public class RoomingFragment extends Fragment {

    private CoffeeHouseAdapter adapter;
    private DatabaseHelper db;
    private String selectedGov = User.GOV_IRBID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rooming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(requireContext());
        long uid = ((PookiPlannerApplication) requireActivity().getApplication())
                .getPrefManager().getUserId();
        User user = db.getUserById(uid);
        if (user != null && user.getGovernorate() != null) {
            selectedGov = user.getGovernorate();
        }

        Spinner spinner = view.findViewById(R.id.spinnerGovernorate);
        String[] labels = {getString(R.string.irbid), getString(R.string.amman)};
        spinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, labels));
        spinner.setSelection(User.GOV_AMMAN.equals(selectedGov) ? 1 : 0);

        TextView titleCoffee = view.findViewById(R.id.textCoffeeTitle);
        RecyclerView recycler = view.findViewById(R.id.recyclerCoffee);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CoffeeHouseAdapter();
        recycler.setAdapter(adapter);

        boolean ar = "ar".equals(((PookiPlannerApplication) requireActivity().getApplication())
                .getPrefManager().getLanguage());

        Runnable refresh = () -> {
            titleCoffee.setText(getString(R.string.coffee_houses,
                    DataProvider.getGovernorateDisplayName(selectedGov, ar)));
            adapter.setItems(DataProvider.getCoffeeHouses(selectedGov));
        };
        refresh.run();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long id) {
                selectedGov = pos == 0 ? User.GOV_IRBID : User.GOV_AMMAN;
                refresh.run();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        MaterialButton bookBtn = view.findViewById(R.id.btnBookRoom);
        bookBtn.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), AddBookingActivity.class)));

        TextView note = view.findViewById(R.id.textJordanOnly);
        note.setText(R.string.jordan_only);
    }
}
