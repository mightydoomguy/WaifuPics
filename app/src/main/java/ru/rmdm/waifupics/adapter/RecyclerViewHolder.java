package ru.rmdm.waifupics.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.rmdm.waifupics.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    ImageView image;
    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.imageView);

    }

    static RecyclerViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        return new RecyclerViewHolder(view);
    }
}
