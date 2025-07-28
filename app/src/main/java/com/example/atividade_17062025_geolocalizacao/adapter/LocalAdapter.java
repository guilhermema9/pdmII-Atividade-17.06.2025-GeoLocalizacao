package com.example.atividade_17062025_geolocalizacao.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.atividade_17062025_geolocalizacao.R;
import com.example.atividade_17062025_geolocalizacao.model.Local;

import java.io.File;
import java.util.List;

public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalViewHolder> {

    private final List<Local> locais;
    private final Context context;
    private OnItemClickListener listener;

    // Interface para o evento de clique em um item da lista
    public interface OnItemClickListener {
        void onItemClick(Local local);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public LocalAdapter(List<Local> locais, Context context) {
        this.locais = locais;
        this.context = context;
    }

    @NonNull
    @Override
    public LocalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item da lista (item_local.xml)
        View view = LayoutInflater.from(context).inflate(R.layout.item_local, parent, false);
        return new LocalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalViewHolder holder, int position) {
        Local local = locais.get(position);

        // Define o nome e a descrição do local nos TextViews
        holder.textNome.setText(local.getNome());
        holder.textDescricao.setText(local.getDescricao());

        // Carrega a primeira foto do local como thumbnail
        if (local.getPhotoPaths() != null && !local.getPhotoPaths().isEmpty()) {
            String firstPhotoPath = local.getPhotoPaths().get(0);
            File imgFile = new File(firstPhotoPath);

            if (imgFile.exists()) {
                // Se o arquivo de imagem existe, carrega-o no ImageView
                holder.imageThumb.setImageURI(Uri.fromFile(imgFile));
            } else {
                // Caso contrário, usa uma imagem padrão
                holder.imageThumb.setImageResource(R.drawable.photo);
            }
        } else {
            // Se não houver fotos, usa a imagem padrão
            holder.imageThumb.setImageResource(R.drawable.photo);
        }

        // Configura o evento de clique para o item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(local);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locais.size();
    }

    /**
     * ViewHolder que armazena as referências para as views de cada item da lista.
     */
    public static class LocalViewHolder extends RecyclerView.ViewHolder {
        ImageView imageThumb;
        TextView textNome;
        TextView textDescricao;

        public LocalViewHolder(@NonNull View itemView) {
            super(itemView);
            imageThumb = itemView.findViewById(R.id.imageThumb);
            textNome = itemView.findViewById(R.id.textNome);
            textDescricao = itemView.findViewById(R.id.textDescricao);
        }
    }
}