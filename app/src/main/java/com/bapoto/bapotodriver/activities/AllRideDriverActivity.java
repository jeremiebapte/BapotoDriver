package com.bapoto.bapotodriver.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.adapters.AllRideAdapter;
import com.bapoto.bapotodriver.databinding.ActivityAllRideDriverBinding;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.utilities.Constants;
import com.bapoto.bapotodriver.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AllRideDriverActivity extends AppCompatActivity {

    private ActivityAllRideDriverBinding binding;
    private AllRideAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRideDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        Query query = reservationRef.whereEqualTo("acceptedBy",preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo("isDone",true);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query,Reservation.class)
                .build();

        adapter = new AllRideAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.AllreservationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

}