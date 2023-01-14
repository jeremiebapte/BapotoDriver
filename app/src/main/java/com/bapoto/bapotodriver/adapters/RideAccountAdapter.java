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

public class RideAccountAdapter extends FirestoreRecyclerAdapter <User, RideAccountAdapter.RideAccountHolder>  {

    public RideAccountAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RideAccountHolder holder, int position, @NonNull User model) {
        holder.tvRideAccount.setText(String.valueOf(model.numberOfRide));
    }

    @NonNull
    @Override
    public RideAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_profile_driver,
                parent,false);
        return new RideAccountHolder(v);
    }

    class RideAccountHolder extends RecyclerView.ViewHolder {
        TextView tvRideAccount;

        public RideAccountHolder(@NonNull View itemView) {
            super(itemView);
            tvRideAccount = itemView.findViewById(R.id.tvWeeklyRide);
        }
    }


}
