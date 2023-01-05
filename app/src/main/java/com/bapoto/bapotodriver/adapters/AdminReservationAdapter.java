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
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AdminReservationAdapter extends FirestoreRecyclerAdapter<Reservation,AdminReservationAdapter.AdminReservationHolder> {
 private OnItemClickListener listener;


    public AdminReservationAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
     }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull AdminReservationHolder holder, int position,
                                    @NonNull Reservation model) {

        holder.tvPickUp.setText(model.getPickUp());
        holder.tvDropOff.setText(model.getDropOff());
        holder.tvDate.setText(model.getDate());
        holder.tvHour.setText(model.getHour());
        holder.tvPrice.setText(model.getPrice());
        holder.tvdriver.setText(model.getDriver());

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



        }

    @NonNull
    @Override
    public AdminReservationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_admin_reservation
        ,parent,false);
        return new AdminReservationHolder(view);
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();

    }



    public class AdminReservationHolder extends RecyclerView.ViewHolder {
        TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice ,tvdriver,tvDateAccepted;

        public AdminReservationHolder(@NonNull View itemView) {
            super(itemView);
            tvPickUp = itemView.findViewById(R.id.tvpickUp);
            tvDropOff = itemView.findViewById(R.id.tvDropOff);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvHour = itemView.findViewById(R.id.tvHour);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvdriver = itemView.findViewById(R.id.tvDriver);
            tvDateAccepted= itemView.findViewById(R.id.tvDateAccepted);


            itemView.setOnClickListener(view -> {
                int position  = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position );
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
