package com.example.parkinglot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ParkingLotAdapter extends RecyclerView.Adapter<ParkingLotAdapter.ViewHolder> {

    private Context context;
    private List<String> parkingLotList;
    private OnItemClickListener listener;

    public ParkingLotAdapter(Context context, List<String> parkingLotList) {
        this.context = context;
        this.parkingLotList = parkingLotList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_parking_lot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.parkingLotName.setText(parkingLotList.get(position));
    }

    @Override
    public int getItemCount() {
        return parkingLotList.size();
    }

    public String getItem(int position) {
        return parkingLotList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView parkingLotName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parkingLotName = itemView.findViewById(R.id.parkingLotName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}


