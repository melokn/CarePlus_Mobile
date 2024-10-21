package com.example.testecareplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
<<<<<<< HEAD
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
=======
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
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

<<<<<<< HEAD
public class HomeFragment extends AppCompatActivity {
=======
public class HomeFragment extends Fragment {
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c

    private ImageAdapter imageAdapter;
    private List<String> imageUrls = new ArrayList<>();
    private List<String> patientIds = new ArrayList<>();

<<<<<<< HEAD
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
=======

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Configuração do RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        imageAdapter = new ImageAdapter(getContext(), imageUrls, patientIds, new ImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(String patientId) {
                // Salvando o ID do paciente no SharedPreferences
                SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("selectedPatientId", patientId);
                editor.apply();

<<<<<<< HEAD

                Intent intent = new Intent(HomeFragment.this, ProntuarioFragment.class);
                startActivity(intent);
=======
                // Navegando para outro Fragment
                Fragment prontuarioFragment = new ProntuarioFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, prontuarioFragment)
                        .addToBackStack(null)
                        .commit();
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
            }
        });
        recyclerView.setAdapter(imageAdapter);

<<<<<<< HEAD

        ImageButton Morepacientes = findViewById(R.id.btMorePacientes);
        Morepacientes.setOnClickListener(v -> {
            Intent intent = new Intent(HomeFragment.this, ProntuarioFragment.class);
            startActivity(intent);
        });


        fetchImageUrlsFromDatabase();
    }

    private void fetchImageUrlsFromDatabase() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
=======
        // Configuração do botão "Mais Pacientes"
        ImageButton morePacientes = view.findViewById(R.id.btMorePacientes);
        morePacientes.setOnClickListener(v -> {
            // Navegando para outro Fragment
            Fragment prontuarioFragment = new ProntuarioFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, prontuarioFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Busca os URLs das imagens e IDs dos pacientes
        fetchImageUrlsFromDatabase();

        return view;
    }

    private void fetchImageUrlsFromDatabase() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
        String userId = prefs.getString("userId", null);

        String url = String.format("http://10.0.2.2:4060/users/%s/patients/", userId);

<<<<<<< HEAD

=======
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
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
<<<<<<< HEAD

                            if (response.has("patients")) {

                                JSONArray patientsArray = response.getJSONArray("patients");


=======
                            if (response.has("patients")) {
                                JSONArray patientsArray = response.getJSONArray("patients");

>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
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
<<<<<<< HEAD
                            Toast.makeText(HomeFragment.this, "Erro ao processar os pacientes", Toast.LENGTH_SHORT).show();
                        }


=======
                            Toast.makeText(getContext(), "Erro ao processar os pacientes", Toast.LENGTH_SHORT).show();
                        }

>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
                        updateRecyclerView(newImageUrls, newPatientIds);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
<<<<<<< HEAD
                        Toast.makeText(HomeFragment.this, "Erro ao carregar pacientes", Toast.LENGTH_SHORT).show();
=======
                        Toast.makeText(getContext(), "Erro ao carregar pacientes", Toast.LENGTH_SHORT).show();
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
                    }
                }
        );

<<<<<<< HEAD

        Volley.newRequestQueue(this).add(enviarGet);
    }


=======
        Volley.newRequestQueue(getContext()).add(enviarGet);
    }

>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
    private void updateRecyclerView(List<String> imageUrls, List<String> patientIds) {
        this.imageUrls.clear();
        this.imageUrls.addAll(imageUrls);

        this.patientIds.clear();
        this.patientIds.addAll(patientIds);

<<<<<<< HEAD

=======
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
        imageAdapter.notifyDataSetChanged();
    }
}
