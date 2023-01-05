package com.bapoto.bapotodriver.activities.admin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.adapters.AllRideAdapter;
import com.bapoto.bapotodriver.databinding.ActivityAllRideAdminBinding;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.models.User;
import com.bapoto.bapotodriver.utilities.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class AllRideAdminActivity extends AppCompatActivity {

    private ActivityAllRideAdminBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private AllRideAdapter adapter;
    private String m_Text = "";


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRideAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        setupRecyclerView();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setListeners() {

        binding.imageOpenChat.setOnClickListener(view -> {
            Intent intent = new Intent(this, ProfileAdminActivity.class);
            startActivity(intent);
            finish();
        });

        binding.imageOpenAllRide.setOnClickListener(view -> {
            Intent intent = new Intent(this, AllRideAdminActivity.class);
            startActivity(intent);
            finish();
        });
    }



    private void setupRecyclerView() {
        Query query = reservationRef.orderBy("date", Query.Direction.DESCENDING)
                                    .whereEqualTo("isDone",true);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query, Reservation.class)
                .build();

        adapter = new AllRideAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.AdminAllreservationRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener((documentSnapshot, position) -> alertAddToAccountDriver());
    }

    private void alertAddToAccountDriver() {
        adapter.setOnItemClickListener(((documentSnapshot, position) -> {
            AtomicReference<String> docId = new AtomicReference<>(documentSnapshot.getId());
            Reservation resa = documentSnapshot.toObject(Reservation.class);
            User driver = documentSnapshot.toObject(User.class);
            int driverAccount = Objects.requireNonNull(driver).account;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Montant pour : " + Objects.requireNonNull(resa).getDriver());

// Set up the input
            final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

// Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                m_Text = input.getText().toString();
                int result = Integer.parseInt(m_Text);
                int finalResult = (driverAccount + result);

                db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(resa.getDriverId())
                        .update(Constants.KEY_ACCOUNT, FieldValue.increment(finalResult));
                db.collection(Constants.KEY_COLLECTION_USERS)
                        .document(resa.getDriverId())
                        .update(Constants.NUMBER_RIDE, FieldValue.increment(1));


            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();


        }));
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