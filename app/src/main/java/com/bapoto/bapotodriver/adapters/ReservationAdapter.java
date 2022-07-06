package com.bapoto.bapotodriver.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.models.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ReservationAdapter extends FirestoreRecyclerAdapter <Reservation, ReservationAdapter.ReservationHolder>{
   private OnItemClickListener listener;


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

     public class ReservationHolder extends RecyclerView.ViewHolder {
        TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice;



        public ReservationHolder(View itemView) {
            super(itemView);
            tvPickUp = itemView.findViewById(R.id.tvpickUp);
            tvDropOff = itemView.findViewById(R.id.tvDropOff);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvPrice = itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position),position );

                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}





    /*private void alertAcceptRide() {
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

    public void assignRide() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        PreferenceManager preferenceManager = new PreferenceManager(itemView.getContext());
        HashMap<String,Object> update = new HashMap<>();
        update.put(Constants.IS_ACCEPTED,true);
        update.put(Constants.KEY_ACCEPTED_BY,preferenceManager.getString(Constants.KEY_NAME));
        CollectionReference ref = db.collection(Constants.KEY_COLLECTION_RESERVATIONS)
                .getId()
                .toString()
        ref.document(ref.document().getId())
                .update(update)
                .addOnSuccessListener(unused -> {
                    preferenceManager.putBoolean(Constants.IS_ACCEPTED,true);

                });

    }

    private void showAcceptedRideMessage() {
        okBtn.setVisibility(View.INVISIBLE);
        tvMessageAccepted.setVisibility(View.VISIBLE);

    }*/




