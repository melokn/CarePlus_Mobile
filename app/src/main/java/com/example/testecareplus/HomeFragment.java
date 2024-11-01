package com.example.testecareplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

public class HomeFragment extends Fragment {

    private ImageAdapter imageAdapter;
    private List<String> imageBase64List = new ArrayList<>();
    private List<String> patientIds = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar o layout do fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Configuração do RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        imageAdapter = new ImageAdapter(getContext(), imageBase64List, patientIds, new ImageAdapter.OnImageClickListener() {
            @Override
            public void onImageClick(String patientId) {
                // Salvando o ID do paciente no SharedPreferences
                SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("patientId", patientId);
                editor.apply();

                // Navegando para outro Fragment
                Fragment prontuarioFragment = new ProntuarioFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, prontuarioFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setAdapter(imageAdapter);

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

        // Busca os dados dos pacientes
        fetchPatientDataFromDatabase();

        return view;
    }

    private void fetchPatientDataFromDatabase() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        String url = String.format("http://10.0.2.2:4060/users/%s/patients", userId);

        JsonObjectRequest enviarGet = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,  // Não estamos enviando corpo na requisição
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<String> newImageBase64List = new ArrayList<>();
                        List<String> newPatientIds = new ArrayList<>();

                        try {
                            if (response.has("patients")) {
                                JSONArray patientsArray = response.getJSONArray("patients");

                                for (int i = 0; i < patientsArray.length(); i++) {
                                    JSONObject patientJson = patientsArray.getJSONObject(i);
                                    String patientId = patientJson.optString("id", null);  // Obtém o ID do paciente
                                    String iconBase64 = patientJson.optString("icon", null);  // Obtém o base64 da imagem

                                    if (iconBase64 != null && !iconBase64.isEmpty()) {
                                        newImageBase64List.add(iconBase64);
                                        newPatientIds.add(patientId);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Erro ao processar os pacientes", Toast.LENGTH_SHORT).show();
                        }

                        updateRecyclerView(newImageBase64List, newPatientIds);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Erro ao carregar pacientes", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(enviarGet);
    }

    private void updateRecyclerView(List<String> imageBase64List, List<String> patientIds) {
        this.imageBase64List.clear();
        this.imageBase64List.addAll(imageBase64List);

        this.patientIds.clear();
        this.patientIds.addAll(patientIds);

        imageAdapter.notifyDataSetChanged();
    }
}
