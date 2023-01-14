package com.bapoto.bapotodriver.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.activities.admin.AdminsActivity;
import com.bapoto.bapotodriver.adapters.DriverAccountAdapter;
import com.bapoto.bapotodriver.adapters.RecentConversationAdapter;
import com.bapoto.bapotodriver.adapters.ReservationAcceptedAdapter;
import com.bapoto.bapotodriver.adapters.RideAccountAdapter;
import com.bapoto.bapotodriver.databinding.ActivityProfileDriverBinding;
import com.bapoto.bapotodriver.listeners.ConversionListener;
import com.bapoto.bapotodriver.models.ChatMessage;
import com.bapoto.bapotodriver.models.Reservation;
import com.bapoto.bapotodriver.models.User;
import com.bapoto.bapotodriver.utilities.Constants;
import com.bapoto.bapotodriver.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.units.qual.C;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ProfileDriverActivity extends BaseActivity implements ConversionListener {

    private ActivityProfileDriverBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private ReservationAcceptedAdapter adapter;
    private DriverAccountAdapter driverAccountAdapter;
    private RideAccountAdapter rideAccountAdapter;
    private RecentConversationAdapter conversationAdapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference reservationRef = db.collection(Constants.KEY_COLLECTION_RESERVATIONS);
    private final CollectionReference userRef = db.collection(Constants.KEY_COLLECTION_USERS);
    private NotificationManagerCompat notificationManager;

    private FirebaseFirestore database;
    private ReservationAcceptedAdapter reservationAcceptedAdapter;
    @SuppressLint("StaticFieldLeak")
    public static Button btnAbortRide, btnFinishRide;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        notificationManager = NotificationManagerCompat.from(this);

        btnAbortRide = findViewById(R.id.abortRideButton);
        btnFinishRide = findViewById(R.id.finishRideButton);
        init();
        loadUserDetails();
        getToken();
        listenConversation();
        setListeners();
        setupRecyclerView();
        setupSndRecyclerView();

    }



    private void init() {
        conversations = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations,this);
        binding.conversationsRecyclerView.setAdapter(conversationAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void setListeners() {
        binding.imageSignOut.setOnClickListener(view -> signOut());
        binding.imageDeleteAccount.setOnClickListener(view -> alertDeleteAccount());
        binding.fabNewChat.setOnClickListener(view ->
                startActivity(new Intent(this, AdminsActivity.class)));
        binding.fabGoToresa.setOnClickListener(view ->
                startActivity(new Intent(this,MainActivity.class)));
        binding.fabGoToAllDone.setOnClickListener(view -> {
            startActivity(new Intent(this,AllRideDriverActivity.class));
        });
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_FCM_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID)
                        );
        documentReference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnFailureListener(e -> showToast("Echec mis à jour du token"));
    }

    private void loadUserDetails(){


        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));


        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void signOut() {
        showToast("Déconnexion...");
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                        .document(preferenceManager.getString(Constants.KEY_USER_ID));
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(this, SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Déconnexion impossible"));
    }

    private void deleteAccount() {
        showToast("Suppression du compte...");
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID));
        documentReference.delete();
        startActivity(new Intent(this,SignUpActivity.class));
    }

    //PopUp for confirm the signout
    public void alertDeleteAccount() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("A BIENTOT");
        alertDialogBuilder.setIcon(R.drawable.ic_sad);

        // set dialog message
        alertDialogBuilder
                .setMessage("Êtes vous sur de vouloir supprimer votre compte?")
                .setCancelable(false)
                .setPositiveButton("Oui !", (dialog, id) -> {
                    // if this button is clicked, close
                    // current activity
                    deleteAccount();
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

    //////////////////////// RECYCLER VIEW - RESERVATIONS ACCEPTEES////////////////////////////
    private void setupRecyclerView() {
        Query query = reservationRef.whereEqualTo(Constants.KEY_ACCEPTED_BY,
                preferenceManager.getString(Constants.KEY_USER_ID))
                                    .whereEqualTo(Constants.KEY_IS_DONE,false);

        FirestoreRecyclerOptions<Reservation> options = new FirestoreRecyclerOptions.Builder<Reservation>()
                .setQuery(query, Reservation.class)
                .build();

        adapter = new ReservationAcceptedAdapter(options);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.rideAcceptedRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnButtonAbortClickListener((documentSnapshot, position)   -> alertAbortRide());

        adapter.setOnButtonFinishClickListener((documentSnapshot, position) -> alertFinishRide() );

    }

    private void setupSndRecyclerView() {
        Query query = userRef.whereEqualTo(Constants.KEY_USER_ID,
                preferenceManager.getString(Constants.KEY_USER_ID));

        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();

        driverAccountAdapter = new DriverAccountAdapter(options);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false);
        RecyclerView recyclerView = findViewById(R.id.accountRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(driverAccountAdapter);
    }
    private void alertFinishRide() {
        adapter.setOnButtonFinishClickListener(((documentSnapshot, position) -> {
            AtomicReference<String> docId = new AtomicReference<>(documentSnapshot.getId());
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set title
            alertDialogBuilder.setTitle("COURSE TERMINEE ?");
            alertDialogBuilder.setIcon(R.drawable.ic_thumb_up);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Vous avez terminé cette course ?")
                    .setCancelable(false)
                    .setPositiveButton("Oui !", (dialog, id) -> {
                        {
                            docId.set(documentSnapshot.getId());
                            try {
                                finishRide(docId);
                                addRideToAccount(docId);
                                animationAndGoBackToMain();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                    })
                    .setNegativeButton("Non", (dialog, id) -> dialog.cancel());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }));
    }

    private void finishRide(AtomicReference<String> docId) throws ParseException {

        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                        .document(String.valueOf(docId));


            documentReference.update(Constants.KEY_IS_DONE,true);
            documentReference.update(Constants.KEY_FINISHED_THE,new Date());
        }

        private void addRideToAccount(AtomicReference<String> docId) {


        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_USERS)
                .document(String.valueOf(docId));

        documentReference.update("numberOfRide", FieldValue.increment(1));

        }

        private void alertAbortRide() {
        adapter.setOnButtonAbortClickListener(((documentSnapshot, position) -> {
        AtomicReference<String> docId = new AtomicReference<>(documentSnapshot.getId());
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            // set title
        alertDialogBuilder.setTitle("ANNULATION RÉSERVATION");
        alertDialogBuilder.setIcon(R.drawable.ic_sad);

        // set dialog message
        alertDialogBuilder
                .setMessage("Etes vous sur de ne plus vouloir faire cette course ?")
                .setCancelable(false)
                .setPositiveButton("Oui !", (dialog, id) -> {
                    {
                        docId.set(documentSnapshot.getId());
                        abortRide(docId);

                    }
                })
                .setNegativeButton("Non", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }));

    }

    private void abortRide(AtomicReference<String> docId) {

        DocumentReference documentReference =
                db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                        .document(String.valueOf(docId));
        documentReference.update(Constants.IS_ACCEPTED,false,
                Constants.KEY_ACCEPTED_BY,null
        );
    }

    private void animationAndGoBackToMain() {
        Intent intent = new Intent(ProfileDriverActivity.this, SuccessRideFinish.class);
        startActivity(intent);
        finish();
    }

 @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        driverAccountAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        driverAccountAdapter.stopListening();
    }

    ////////////////////// CHAT //////////////////////////
    private void listenConversation() {
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID,preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    } else {
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                }else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i ++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
         }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onConversionClicked(User user) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
    }
}