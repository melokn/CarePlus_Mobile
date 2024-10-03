package com.example.testecareplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends AppCompatActivity {

    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private List<String> patientIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        imageAdapter = new ImageAdapter(this, imageUrls, patientIds, new ImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick( String patientId) {

                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("selectedPatientId", patientId);
                editor.apply();


                Intent intent = new Intent(HomeFragment.this, ProntuarioFragment.class);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(imageAdapter);


        ImageButton Morepacientes = findViewById(R.id.btMorePacientes);
        Morepacientes.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this, ProntuarioFragment.class);
            startActivity(intent);
        });


        fetchImageUrlsFromDatabase();
    }

    private void fetchImageUrlsFromDatabase() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        String url = String.format("http://10.0.2.2:4060/users/%s/patients/", userId);


        JsonObjectRequest enviarGet = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,  // Não estamos enviando corpo na requisição
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<String> newImageUrls = new ArrayList<>();
                        List<String> newPatientIds = new ArrayList<>();

                        try {

                            if (response.has("patients")) {

                                JSONArray patientsArray = response.getJSONArray("patients");


                                for (int i = 0; i < patientsArray.length(); i++) {
                                    JSONObject patientJson = patientsArray.getJSONObject(i);
                                    String patientId = patientJson.optString("id", null);  // Obtém o ID do paciente
                                    String urlIcon = patientJson.optString("urlIcon", null);  // Obtém a URL da imagem (se houver)

                                    if (urlIcon != null && !urlIcon.isEmpty()) {
                                        newImageUrls.add(urlIcon);
                                        newPatientIds.add(patientId);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(HomeFragment.this, "Erro ao processar os pacientes", Toast.LENGTH_SHORT).show();
                        }


                        updateRecyclerView(newImageUrls, newPatientIds);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(HomeFragment.this, "Erro ao carregar pacientes", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        Volley.newRequestQueue(this).add(enviarGet);
    }


    private void updateRecyclerView(List<String> imageUrls, List<String> patientIds) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);

        this.patientIds.clear();
        this.patientIds.addAll(patientIds);


        imageAdapter.notifyDataSetChanged();
    }
}
