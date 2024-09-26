package com.example.testecareplus;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private List<String> imageUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        recyclerView = findViewById(R.id.recyclerView);

        // Configurando o LayoutManager para ser horizontal
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Supondo que você já tenha um método que obtém as URLs do banco de dados
        imageUrls = fetchImageUrlsFromDatabase(); // Substitua pelo seu método
        adapter = new ImageAdapter(this, imageUrls);
        recyclerView.setAdapter(adapter);

        ImageButton Morepacientes = findViewById(R.id.btMorePacientes);

        Morepacientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeFragment.this, ProntuarioFragment.class);

                startActivity(intent);
            }
        });
    }

    private List<String> fetchImageUrlsFromDatabase() {
        // Implementar a lógica para buscar as URLs do banco de dados
        return new ArrayList<>(); // Retornar as URLs obtidas
    }



}