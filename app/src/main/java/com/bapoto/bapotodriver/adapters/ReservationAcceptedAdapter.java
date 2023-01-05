package com.bapoto.bapotodriver.adapters;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.activities.ProfileDriverActivity;
import com.bapoto.bapotodriver.models.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ReservationAcceptedAdapter extends FirestoreRecyclerAdapter<Reservation,ReservationAcceptedAdapter.ReservationAcceptedHolder> {
    private OnButtonAbortClickListener listener;
    private OnButtonFinishClickListener listener1;
    private GetInputDate listener2;

    public ReservationAcceptedAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReservationAcceptedHolder holder, int position, @NonNull Reservation model) {
        holder.tvDate.setText(model.getDate());
        holder.tvPickUp.setText(model.getPickUp());
        holder.tvDropOff.setText(model.getDropOff());
        holder.tvHour.setText(model.getHour());
        holder.tvPrice.setText(model.getPrice());
        //holder.tvDateAccepted.setText(model.getDayAccepted());
    }

    @NonNull
    @Override
    public ReservationAcceptedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resa_accepted,
                parent, false);
        return new ReservationAcceptedHolder(view);
    }


    public  class ReservationAcceptedHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvPickUp, tvDropOff, tvHour, tvPrice, tvDateAccepted;
        Button btAbortRide, btFinishRide;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();


        public ReservationAcceptedHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateRideAccepted);
            tvPickUp = itemView.findViewById(R.id.tvpickUp);
            tvDropOff = itemView.findViewById(R.id.tvDropOff);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            //tvDateAccepted = itemView.findViewById(R.id.tvAcceptedDate);
            btAbortRide = itemView.findViewById(R.id.abortRideButton);
            btFinishRide = itemView.findViewById(R.id.finishRideButton);


            btAbortRide.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onButtonAbortClick(getSnapshots().getSnapshot(position), position);
                }

            });
            btFinishRide.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener1 != null) {
                    listener1.onButtonFinishClick(getSnapshots().getSnapshot(position),position);
                }
            });
        tvDate.getText().toString();

        }


    }

    public interface OnButtonAbortClickListener {
        void onButtonAbortClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnButtonAbortClickListener(OnButtonAbortClickListener listener) {
        this.listener = listener;
    }

    public interface OnButtonFinishClickListener {
        void onButtonFinishClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnButtonFinishClickListener(OnButtonFinishClickListener listener1) {
        this.listener1 = listener1;
    }

    public interface GetInputDate{
        void getInputDate(String inputDate, int position);
    }

    public void setInputDate(GetInputDate listener2){
        this.listener2 = listener2;
    }

}
