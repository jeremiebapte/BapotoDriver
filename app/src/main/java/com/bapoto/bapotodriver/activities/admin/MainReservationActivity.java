package com.bapoto.bapotodriver.activities.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.adapters.AdminReservationAdapter;

import com.bapoto.bapotodriver.databinding.ActivityMainReservationBinding;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.utilities.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainReservationActivity extends AppCompatActivity  {

    private ActivityMainReservationBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private AdminReservationAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setupRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setListeners() {
        binding.addReservationMain.setOnClickListener(view -> {
            Intent intent = new Intent(this, SendReservationActivity.class);
            startActivity(intent);
            finish();
        });
        binding.imageOpenChat.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileAdminActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // VOIR TOUTES LES RESERVATIONS
    private void setupRecyclerView() {
        Query query = reservationRef.orderBy("date", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query,Reservation.class)
                .build();

        adapter = new AdminReservationAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.AdminreservationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAbsoluteAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
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
