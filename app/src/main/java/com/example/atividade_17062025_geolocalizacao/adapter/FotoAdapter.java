package com.example.atividade_17062025_geolocalizacao.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade_17062025_geolocalizacao.R;

import java.io.File;
import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.FotoViewHolder> {

    private final List<String> photoPaths;
    private final Context context;

    public FotoAdapter(List<String> photoPaths, Context context) {
        this.photoPaths = photoPaths;
        this.context = context;
    }

    @NonNull
    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item da foto (item_foto.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_foto, parent, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder holder, int position) {
        String photoPath = photoPaths.get(position);
        File imgFile = new File(photoPath);

        if (imgFile.exists()) {
            holder.imageView.setImageURI(Uri.fromFile(imgFile));
        }
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }

    public static class FotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public FotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageFoto);
        }
    }
}