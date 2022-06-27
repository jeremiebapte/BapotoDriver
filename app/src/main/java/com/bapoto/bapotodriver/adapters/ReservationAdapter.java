package com.bapoto.bapotodriver.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.activities.ProfileDriverActivity;
import com.bapoto.bapotodriver.models.Admin;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.models.User;
import com.bapoto.bapotodriver.utilities.Constants;
import com.bapoto.bapotodriver.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ReservationAdapter extends FirestoreRecyclerAdapter <Reservation, ReservationAdapter.ReservationHolder>{


    public ReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationHolder holder, int position, @NonNull Reservation model) {
        holder.tvPickUp.setText(model.getPickUp());
        holder.tvDropOff.setText(model.getDropOff());
        holder.tvDate.setText(model.getDate());
        holder.tvHour.setText(model.getHour());
        holder.tvPrice.setText(model.getPrice());
    }

    @NonNull
    @Override
    public ReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_reservation,
                parent,false);
        return new ReservationHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    static class ReservationHolder extends RecyclerView.ViewHolder {
    TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice,tvMessageAccepted;
    Button okBtn;
    private PreferenceManager preferenceManager;
        private Admin admin;
        private Date date;

        public ReservationHolder(View itemView) {
            super(itemView);
            tvPickUp = itemView.findViewById(R.id.tvpickUp);
            tvDropOff = itemView.findViewById(R.id.tvDropOff);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            okBtn = itemView.findViewById(R.id.acceptRide);
            tvMessageAccepted = itemView.findViewById(R.id.rideAccepted);
            okBtn.setOnClickListener(view -> {
                alertAcceptRide();
            });
        }

        private void alertAcceptRide() {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());

            // set title
            alertDialogBuilder.setTitle("Réservation");
            alertDialogBuilder.setIcon(R.drawable.ic_thumb_up);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Accepter cette réservation?")
                    .setCancelable(false)
                    .setPositiveButton("Oui !", (dialog, id) -> {
                        // if this button is clicked, close
                        // current activity
                        assignRide();
                        updateRide();

                    })
                    .setNegativeButton("Non", (dialog, id) -> {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }

        private void updateRide() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

        }

        private void assignRide() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MMMM/yyyy");
            java.util.Date date = new java.util.Date();
            PreferenceManager preferenceManager = new PreferenceManager(itemView.getContext());
            HashMap<String, Object> ride = new HashMap<>();
            ride.put(Constants.KEY_ACCEPTED_BY,preferenceManager.getString(Constants.KEY_NAME));
            ride.put(Constants.KEY_DATE,dateFormat.format(date));
            db.collection(Constants.KEY_COLLECTION_RIDE_ACCEPTED)
                    .add(ride)
                    .addOnSuccessListener(documentReference -> {
                        preferenceManager.putBoolean(Constants.IS_ACCEPTED,true);
                    });
        if(preferenceManager.getBoolean(Constants.IS_ACCEPTED)) {
          showAcceptedRideMessage();

        }
        }

        private void showAcceptedRideMessage() {
            okBtn.setVisibility(View.INVISIBLE);
            tvMessageAccepted.setVisibility(View.VISIBLE);

        }

    }



}






