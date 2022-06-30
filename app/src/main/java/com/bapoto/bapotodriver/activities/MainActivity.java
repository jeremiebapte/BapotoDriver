package com.bapoto.bapotodriver.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.adapters.ReservationAdapter;
import com.bapoto.bapotodriver.databinding.ActivityMainBinding;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.utilities.Constants;
import com.bapoto.bapotodriver.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection("reservations");
    private ReservationAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setupRecyclerView();
    }

    private void setListeners() {
        binding.imageOpenChat.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileDriverActivity.class);
            startActivity(intent);
            

        });
    }


    // RESERVATIONS DISPONIBLES
    private void setupRecyclerView() {
        Query query = reservationRef.whereEqualTo("isAccepted",false);


        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query,Reservation.class)
                .build();

        adapter = new ReservationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.reservationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> alertAcceptRide());

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void alertAcceptRide() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        PreferenceManager preferenceManager = new PreferenceManager(this);
        // set title
        alertDialogBuilder.setTitle("RESERVATION");
        alertDialogBuilder.setIcon(R.drawable.ic_thumb_up);

        // set dialog message
        alertDialogBuilder
                .setMessage("Accepter cette réservation ?")
                .setCancelable(false)
                .setPositiveButton("Oui !", (dialog, id) -> {
                    // if this button is clicked, close
                    // current activity
                    adapter.setOnItemClickListener((documentSnapshot, position) -> {
                        String docId = documentSnapshot.getId();
                        updateRide(docId,preferenceManager.getString(Constants.KEY_USER_ID));

                    });
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

    // ASSIGNER RESERVATION
    private void updateRide(String pathId, String driver) {

        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                        .document(pathId);
        documentReference.update(
                Constants.KEY_ACCEPTED_BY,driver,
                Constants.IS_ACCEPTED,true,
                Constants.KEY_ACCEPTED_THE, new Date()
        );
    }

}

// TODO faire card view pour afficher les reservations dispos