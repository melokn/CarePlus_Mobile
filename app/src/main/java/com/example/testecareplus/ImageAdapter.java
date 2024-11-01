package com.example.testecareplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<String> imageBase64List;
    private List<String> patientIds;
    private Context context;
    private OnImageClickListener onImageClickListener;

    // Interface para capturar o clique nas imagens
    public interface OnImageClickListener {
        void onImageClick(String patientId);
    }

    // Construtor do Adapter
    public ImageAdapter(Context context, List<String> imageBase64List, List<String> patientIds, OnImageClickListener onImageClickListener) {
        this.context = context;
        this.imageBase64List = imageBase64List;
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
        String imageBase64 = imageBase64List.get(position);  // Base64 da imagem do paciente
        String patientId = patientIds.get(position);  // ID do paciente

        // Decodifica a imagem e define no ImageButton
        Bitmap bitmap = decodeBase64ToBitmap(imageBase64);
        holder.imageButton.setImageBitmap(bitmap);

        // Configurar o clique
        holder.imageButton.setOnClickListener(v -> {
            if (onImageClickListener != null) {
                onImageClickListener.onImageClick(patientId);  // Passa o ID do paciente para o listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageBase64List.size();
    }

    // Método para decodificar base64 para Bitmap
    private Bitmap decodeBase64ToBitmap(String base64) {
        byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButtonPaciente);  // Referência ao ImageButton
        }
    }
}
