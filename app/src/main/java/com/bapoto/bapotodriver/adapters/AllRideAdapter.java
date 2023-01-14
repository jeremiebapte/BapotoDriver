package com.bapoto.bapotodriver.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.models.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AllRideAdapter extends FirestoreRecyclerAdapter<Reservation, AllRideAdapter.AllRideHolder> {

    public AllRideAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllRideAdapter.AllRideHolder holder, int position, @NonNull Reservation model) {
        holder.tvPickUp.setText(model.getPickUp());
        holder.tvDropOff.setText(model.getDropOff());
        holder.tvDate.setText((CharSequence) model.getDate());
        holder.tvHour.setText(model.getHour().toString());
        holder.tvPrice.setText(model.getPrice());
        holder.tvDriver.setText(model.getDriver());

        if (model.getDayAccepted() != null) {
            Timestamp dt = model.getDayAccepted();
            int seconds = (int) dt.getSeconds();
            Date date = new Date(seconds * 1000L);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm");
            String resaTime = sdf.format(date);
            holder.tvDateAccepted.setText(resaTime);

        } else {
            holder.tvDateAccepted.setText("");
        }
    }

    @NonNull
    @Override
    public AllRideAdapter.AllRideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_reservation,
                parent,false);
        return new AllRideHolder(v);
    }

    class AllRideHolder extends RecyclerView.ViewHolder {
        TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice,tvDateAccepted,tvDriver;


        public AllRideHolder(@NonNull View itemView) {
            super(itemView);
            tvPickUp = itemView.findViewById(R.id.tvpickUpAll);
            tvDropOff = itemView.findViewById(R.id.tvDropOffAll);
            tvDate = itemView.findViewById(R.id.tvDateAll);
            tvHour = itemView.findViewById(R.id.tvHourAll);
            tvPrice = itemView.findViewById(R.id.tvPriceAll);
            tvDriver = itemView.findViewById(R.id.tvDriverAll);
            tvDateAccepted = itemView.findViewById(R.id.tvDateAcceptedAll);
        }
    }
}
