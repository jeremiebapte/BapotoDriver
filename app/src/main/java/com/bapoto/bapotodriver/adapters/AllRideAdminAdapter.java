package com.bapoto.bapotodriver.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bapoto.bapotodriver.R;
import com.bapoto.bapotodriver.models.Reservation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AllRideAdminAdapter extends FirestoreRecyclerAdapter <Reservation, AllRideAdminAdapter.AllRideHolder> {
    private OnItemClickListener listener;

    public AllRideAdminAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull AllRideHolder holder, int position, @NonNull Reservation model) {
        holder.tvPickUp.setText(model.getPickUp());
        holder.tvDropOff.setText(model.getDropOff());
        holder.tvDate.setText((CharSequence) model.getDate());
        holder.tvHour.setText(model.getHour());
        holder.tvPrice.setText(model.getPrice());
        holder.tvDriver.setText(model.getDriver());

        if (model.getDayAccepted() != null) {
            Timestamp dt = model.getDayAccepted();
            int seconds = (int) dt.getSeconds();
            Date date = new Date(seconds * 1000L);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm");
            String resaTime = sdf.format(date);
            holder.tvDateAccepted.setText(resaTime);

        } else {
            holder.tvDateAccepted.setText("");
        }

        if (model.getDayPaid() != null) {
            holder.tvIsPaid.setText(String.format("%s","Payée !!"));
            holder.tvIsPaid.setVisibility(View.VISIBLE);
        } else {
            holder.tvIsPaid.setVisibility(View.INVISIBLE);
        }
    }



    @NonNull
    @Override
    public AllRideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_all_reservation,
                parent,false);
        return new AllRideHolder(view);
    }


    public class AllRideHolder extends RecyclerView.ViewHolder {
        TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice,tvDateAccepted,tvIsPaid,tvDriver;

        public AllRideHolder(@NonNull View itemView) {
            super(itemView);

            tvPickUp = itemView.findViewById(R.id.tvpickUpAll);
            tvDropOff = itemView.findViewById(R.id.tvDropOffAll);
            tvDate = itemView.findViewById(R.id.tvDateAll);
            tvHour = itemView.findViewById(R.id.tvHourAll);
            tvPrice = itemView.findViewById(R.id.tvPriceAll);
            tvDriver = itemView.findViewById(R.id.tvDriverAll);
            tvIsPaid = itemView.findViewById(R.id.isPaid);
            tvDateAccepted = itemView.findViewById(R.id.tvDateAcceptedAll);


           itemView.setOnClickListener(view -> {
               int position = getBindingAdapterPosition();
               if (position != RecyclerView.NO_POSITION && listener!= null) {
                   listener.onItemClick(getSnapshots().getSnapshot(position),position );
               }
           });

        }

    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
