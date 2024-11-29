package com.example.testecareplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
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
import org.json.JSONException;
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
        CalendarView calendarView = view.findViewById(R.id.calendarView);
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
                Fragment pacienteFragment = new PacienteFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pacienteFragment)
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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Quando o usuário selecionar uma data, abre o Google Agenda
                openGoogleCalendar();
            }
        });
        ImageButton moreComment = view.findViewById((R.id.btMoreComment));
        moreComment.setOnClickListener(v -> {
            EditText etComment = view.findViewById(R.id.etComment);
            String comment = etComment.getText().toString().trim();
            if (comment.isEmpty()) {
                Toast.makeText(getContext(), "Adicione texto antes de enviar!", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String userId = prefs.getString("userId", null);
            String usuario = prefs.getString("usuario", null);
            if (userId == null) {
                Toast.makeText(getContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
            } else {
                createComment(userId, usuario, comment);
                etComment.setText("");
            }

        });

        // Busca os dados dos pacientes
        fetchPatientDataFromDatabase();

        return view;
    }

    private void openGoogleCalendar() {
        // Cria a Intent para abrir o Google Agenda
        Intent intent = new Intent(Intent.ACTION_VIEW);

        // Tenta abrir o Google Calendar com uma URI de visualização de eventos
        intent.setData(Uri.parse("content://com.android.calendar/time/" + System.currentTimeMillis()));

        // Fallback: Tentativa de abrir diretamente o Google Calendar se o sistema não conseguir abrir a URI
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // Caso o Google Agenda não esteja instalado, abrir a versão web no navegador
            Uri uri = Uri.parse("https://www.google.com/calendar");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(webIntent);
        }
    }
    private void fetchPatientDataFromDatabase() {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        String url = String.format("https://careplus-696u.onrender.com/users/%s/patients", userId);

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

    private void createComment(String userId, String username, String comment){
        String url = "https://careplus-696u.onrender.com/hint/addHint";

        JSONObject hintData = new JSONObject();
        try {
            hintData.put("author", username);
            hintData.put("content", comment);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao criar a dica!", Toast.LENGTH_SHORT).show();
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, hintData,
                response -> {

                    Toast.makeText(getContext(), "Dica adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                },
                error -> {

                    Log.e("VolleyError", "Erro ao salvar dica: " + error.getMessage());
                    Toast.makeText(getContext(), "Erro ao salvar a dica!", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(getContext()).add(request);

    }
}
