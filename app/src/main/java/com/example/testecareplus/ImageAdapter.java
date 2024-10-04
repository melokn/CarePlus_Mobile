package com.example.testecareplus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> imageUrls;
    private List<String> patientIds;
    private Context context;
    private OnImageClickListener onImageClickListener;

    // Interface para capturar o clique nas imagens
    public interface OnImageClickListener {
        void onImageClick(String patientId);
    }

    // Construtor do Adapter
    public ImageAdapter(Context context, List<String> imageUrls, List<String> patientIds, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.patientIds = patientIds;
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_paciente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);  // URL da imagem do paciente
        String patientId = patientIds.get(position);  // ID do paciente

        // Carregar a imagem com Glide
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageButton);

        // Configurar o clique
        holder.imageButton.setOnClickListener(v -> {
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(patientId);  // Passa o ID do paciente para o listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButtonPaciente);  // Referência ao ImageButton
        }
    }
}
