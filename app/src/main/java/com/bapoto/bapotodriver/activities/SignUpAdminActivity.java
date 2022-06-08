package com.bapoto.bapotodriver.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bapoto.bapotodriver.databinding.ActivitySignUpAdminBinding;

public class SignUpAdminActivity extends AppCompatActivity {

    private ActivitySignUpAdminBinding binding;
    private static final String PASSWORD = "Bapoto92";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }


    private void setListeners() {
       binding.buttonSignIn.setOnClickListener(view ->{
                if (isValidPassword()) {
                    startActivity(new Intent(getApplicationContext(),AdminActivity.class)); }
               });

    }


    private void showToast() {
        Toast.makeText(this, "Entrez le mot de passe", Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidPassword() {
        if (binding.inputPassword.getText().toString().isEmpty()) {
            showToast();
            return false;
        }else if (!(binding.inputPassword.getText().toString().equals(PASSWORD))) {
            showToast();
            return false;
        }else {
            return true;
        }
    }
}