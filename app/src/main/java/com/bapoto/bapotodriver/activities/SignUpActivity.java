package com.bapoto.bapotodriver.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bapoto.bapotodriver.activities.admin.PasswordAdminActivity;
import com.bapoto.bapotodriver.databinding.ActivitySignUpBinding;

import com.bapoto.bapotodriver.utilities.Constants;
import com.bapoto.bapotodriver.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        setListeners();
    }

    private void setListeners(){
        binding.textSignIn.setOnClickListener(view -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(view -> {
            if (isValidSignedUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.adminbutton.setOnClickListener(view -> {
            Intent intent = new Intent(this, PasswordAdminActivity.class);
            startActivity(intent);
        });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString());
        user.put(Constants.KEY_ACCOUNT,0);
        user.put(Constants.NUMBER_RIDE,0);
        user.put(Constants.KEY_IMAGE,encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                loading(false);
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
                preferenceManager.putBoolean(Constants.KEY_IS_DRIVER,true);
                preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
                preferenceManager.putString(Constants.KEY_ACCOUNT,"");
                preferenceManager.putString(Constants.NUMBER_RIDE,"");
                preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
                Intent intent = new Intent(this, MainActivity.class);
                HashMap<String,Object> putId = new HashMap<>();
                putId.put(Constants.KEY_USER_ID,documentReference.getId());
                database.collection(Constants.KEY_COLLECTION_USERS).document(documentReference.getId())
                        .set(putId, SetOptions.merge());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                })
                .addOnFailureListener(e -> {
                loading(false);
                e.getMessage();
                });
    }

    private String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth/bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth,previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

    );

    private Boolean isValidSignedUpDetails() {
        if (encodedImage == null){
            showToast("Choisissez une image de profil");
            return false;
        }else if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Entrez votre nom");
            return false;
        }else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Entrez votre adresse mail");
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()){
            showToast("Entrez une adresse mail valide");
            return false;
        }else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Entrez une mot de passe");
            return false;
        }else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()){
            showToast("Confirmez le mot de passe");
            return false;
        }else if (!binding.inputPassword.getText().toString()
                .equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Le mot de passe et la confirmation doivent etre similaires");
            return false;
        }else {
            return true;
        }
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.buttonSignUp.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}