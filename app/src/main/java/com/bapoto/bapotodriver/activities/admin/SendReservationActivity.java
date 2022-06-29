package com.bapoto.bapotodriver.activities.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import com.bapoto.bapotodriver.databinding.ActivitySendReservationBinding;
import com.bapoto.bapotodriver.utilities.Constants;
import com.bapoto.bapotodriver.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SendReservationActivity extends AppCompatActivity {

    private ActivitySendReservationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySendReservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.buttonSendResa.setOnClickListener(view -> sendReservationToDataBase());
    }

    private void sendReservationToDataBase() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        PreferenceManager preferenceManager = new PreferenceManager(this);
        HashMap<String, Object> reservation = new HashMap<>();
        reservation.put(Constants.KEY_NAME,binding.inputName.getText().toString());
        reservation.put(Constants.KEY_DATE,binding.inputDate.getText().toString());
        reservation.put(Constants.KEY_HOUR,binding.inputHour.getText().toString());
        reservation.put(Constants.KEY_PICK_UP,binding.inputPickUp.getText().toString());
        reservation.put(Constants.IS_ACCEPTED,false);
        reservation.put(Constants.KEY_DROP_OFF,binding.inputDropOff.getText().toString());
        reservation.put(Constants.KEY_INFOS,binding.inputInfos.getText().toString());
        reservation.put(Constants.KEY_PRICE,binding.inputPrice.getText().toString());
        database.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                .add(reservation)
                .addOnSuccessListener(documentReference -> {
                    preferenceManager.putString(Constants.KEY_RESERVATION_ID,documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
                    Intent intent = new Intent(this, ProfileAdminActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(Throwable::getMessage);

    }

}
