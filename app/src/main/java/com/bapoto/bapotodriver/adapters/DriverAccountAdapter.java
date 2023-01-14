package com.bapoto.bapotodriver.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DriverAccountAdapter extends FirestoreRecyclerAdapter <User, DriverAccountAdapter.DriverAccountHolder>{



    public DriverAccountAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DriverAccountHolder holder, int position, @NonNull User model) {
        holder.tvAccount.setText(String.valueOf(model.account));
        holder.tvNbOfRides.setText(String.valueOf(model.numberOfRide));
    }

    @NonNull
    @Override
    public DriverAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver_account,
                parent,false);
        return new DriverAccountHolder(v);
    }

    static class DriverAccountHolder extends RecyclerView.ViewHolder {
        TextView tvAccount, tvNbOfRides;

        public DriverAccountHolder(@NonNull View itemView) {
            super(itemView);
            tvAccount = itemView.findViewById(R.id.tvAccount);
            tvNbOfRides = itemView.findViewById(R.id.tvWeeklyRide);
        }
    }
}
