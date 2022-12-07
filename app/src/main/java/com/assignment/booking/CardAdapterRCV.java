package com.assignment.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CardAdapterRCV extends RecyclerView.Adapter<CardAdapterRCV.cardHolder> {

    Context ctx;
    ArrayList<TableModel> tableList;

    public CardAdapterRCV(Context ctx, ArrayList<TableModel> tableList) {
        this.ctx = ctx;
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public cardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflatedView= LayoutInflater.from(parent.getContext()).inflate(R.layout.table_layout_template, parent, false);
        return new cardHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(@NonNull cardHolder holder, int position) {

        TableModel table= tableList.get(position);
        holder.tableNamePlace.setText(table.getName());
        holder.locationPlace.setText(table.getLocation());
        holder.numGuestPlace.setText(String.valueOf(table.getNumOfGuests()));
        Picasso.get().load(table.getImgURL()).into(holder.tablePicPlace);
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public static class cardHolder extends RecyclerView.ViewHolder{

        ImageView tablePicPlace;
        TextView tableNamePlace, numGuestPlace, locationPlace;
        LinearLayout cardInnerLayout;

        public cardHolder(@NonNull View itemView) {
            super(itemView);

            //Binding Table Layout Template to Adapter
            tableNamePlace= itemView.findViewById(R.id.tableNamePlace);
            numGuestPlace= itemView.findViewById(R.id.numGuestPlace);
            locationPlace= itemView.findViewById(R.id.locationPlace);
            tablePicPlace= itemView.findViewById(R.id.tablePicPlace);
            cardInnerLayout= itemView.findViewById(R.id.cardInnerLayout);

        }
    }
}
