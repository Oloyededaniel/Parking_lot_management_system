package com.example.parkinglot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParkingLotActivity extends AppCompatActivity implements ParkingLotAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ParkingLotAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_lot);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> parkingLotList = Arrays.asList(
                "Parking Lot A",
                "Parking Lot B",
                "Parking Lot C",
                "Parking Lot H",
                "Parking Lot N",
                "Parking Lot ET"
        );

        adapter = new ParkingLotAdapter(this, parkingLotList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ParkingDetailsActivity.class);
        intent.putExtra("parkingLot", adapter.getItem(position));
        startActivity(intent);
    }
}
