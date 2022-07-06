package com.bapoto.bapotodriver.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class AdminReservationAdapter extends FirestoreRecyclerAdapter<Reservation,AdminReservationAdapter.AdminReservationHolder> {



    public AdminReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdminReservationHolder holder, int position, @NonNull Reservation model) {
        User user = new User();
        holder.tvPickUp.setText(model.getPickUp());
                holder.tvDropOff.setText(model.getDropOff());
                holder.tvDate.setText(model.getDate());
                holder.tvHour.setText(model.getHour());
                holder.tvPrice.setText(model.getPrice());
                holder.tvdriver.setText((CharSequence) model.getDriver());
                holder.tvDateAccepted.setText(model.getDayAccepted().toDate().toString());
    }

    @NonNull
    @Override
    public AdminReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_admin_reservation
        ,parent,false);
        return new AdminReservationHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public static class AdminReservationHolder extends RecyclerView.ViewHolder {
        TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice ,tvdriver,tvDateAccepted;


        public AdminReservationHolder(@NonNull View itemView) {
            super(itemView);
            tvPickUp = itemView.findViewById(R.id.tvpickUp);
            tvDropOff = itemView.findViewById(R.id.tvDropOff);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvdriver = itemView.findViewById(R.id.tvDriver);
            tvDateAccepted= itemView.findViewById(R.id.tvDateAccepted);

        }
    }




}
