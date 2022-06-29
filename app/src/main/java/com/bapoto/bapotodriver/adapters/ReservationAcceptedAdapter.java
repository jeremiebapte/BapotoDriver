package com.bapoto.bapotodriver.adapters;

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

public class ReservationAcceptedAdapter extends FirestoreRecyclerAdapter<Reservation,ReservationAcceptedAdapter.ReservationAcceptedHolder> {



    public ReservationAcceptedAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationAcceptedHolder holder, int position, @NonNull Reservation model) {
            holder.tvDate.setText(model.getDate());
            holder.tvPickUp.setText(model.getPickUp());
            holder.tvDropOff.setText(model.getDropOff());
            holder.tvHour.setText(model.getHour());
            holder.tvPrice.setText(model.getPrice());
            holder.tvDateAccepted.setText(model.getDayAccepted());
    }

    @NonNull
    @Override
    public ReservationAcceptedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resa_accepted,
            parent,false);
    return new ReservationAcceptedHolder(view);
    }


    public static class ReservationAcceptedHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvPickUp,tvDropOff,tvHour,tvPrice,tvDateAccepted;

        public ReservationAcceptedHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPickUp = itemView.findViewById(R.id.tvpickUp);
            tvDropOff = itemView.findViewById(R.id.tvDropOff);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDateAccepted = itemView.findViewById(R.id.tvAcceptedDate);
        }
    }
}
