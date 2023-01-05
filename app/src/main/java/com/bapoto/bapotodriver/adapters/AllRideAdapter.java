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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class AllRideAdapter extends FirestoreRecyclerAdapter <Reservation, AllRideAdapter.AllRideHolder> {
    private OnItemClickListener listener;

    public AllRideAdapter(@NonNull FirestoreRecyclerOptions<Reservation> options) {
        super(options);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onBindViewHolder(@NonNull AllRideHolder holder, int position, @NonNull Reservation model) {
        holder.tvPickUp.setText(model.getPickUp());
        holder.tvDropOff.setText(model.getDropOff());
        holder.tvDate.setText((CharSequence) model.getDate());
        holder.tvHour.setText(model.getHour().toString());
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
    }

    @NonNull
    @Override
    public AllRideHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_all_reservation,
                parent,false);
        return new AllRideHolder(view);
    }

    public class AllRideHolder extends RecyclerView.ViewHolder {
        TextView tvPickUp,tvDropOff,tvDate, tvHour,tvPrice,tvDateAccepted,tvDriver;

        public AllRideHolder(@NonNull View itemView) {
            super(itemView);

            tvPickUp = itemView.findViewById(R.id.tvpickUpAll);
            tvDropOff = itemView.findViewById(R.id.tvDropOffAll);
            tvDate = itemView.findViewById(R.id.tvDateAll);
            tvHour = itemView.findViewById(R.id.tvHourAll);
            tvPrice = itemView.findViewById(R.id.tvPriceAll);
            tvDriver = itemView.findViewById(R.id.tvDriverAll);
            tvDateAccepted = itemView.findViewById(R.id.tvDateAcceptedAll);


           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   int position = getBindingAdapterPosition();
                   if (position != RecyclerView.NO_POSITION && listener!= null) {
                       listener.onItemClick(getSnapshots().getSnapshot(position),position );
                   }
               }
           });

        }
        // Function to convert date to string
        public  String convertDateToString(String date)
        {
            // Converts the string
            // format to date object
            @SuppressLint("SimpleDateFormat")
            DateFormat df = new SimpleDateFormat(date);

            // Get the date using calendar object
            Date today = Calendar.getInstance()
                    .getTime();

            // Convert the date into a
            // string using format() method

            // Return the result
            return (df.format(today));
        }

    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
