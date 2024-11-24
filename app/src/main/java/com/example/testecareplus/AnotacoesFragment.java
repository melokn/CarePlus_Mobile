package com.example.testecareplus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AnotacoesFragment extends Fragment {
    Button add;
    AlertDialog dialog;
    LinearLayout layout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_anotacoes_, container, false);

        add = view.findViewById(R.id.add);
        layout = view.findViewById(R.id.container);

        TextView titleTextView = view.findViewById(R.id.title);

        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        if (userId == null) {
            Toast.makeText(getContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
        } else {
            fetchNotes(userId, titleTextView);
        }

        buildDialog(userId);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        return view;
    }


    public void buildDialog(String userId) {
        View view = getLayoutInflater().inflate(R.layout.dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view);

        final EditText title = view.findViewById(R.id.etTitle);
        final EditText name = view.findViewById(R.id.etName);
        final Spinner prioritySpinner = view.findViewById(R.id.spinnerPriority);
        final Spinner selectPatient = view.findViewById(R.id.selectPatient);

        // Buscar pacientes para o spinner
        fetchPatients(userId, selectPatient);

        builder.setTitle("Adicione a sua tarefa!")
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String taskTitle = title.getText().toString();
                    String taskDescription = name.getText().toString();
                    String taskPriority = prioritySpinner.getSelectedItem().toString();
                    String selectedPatientName = (String) selectPatient.getSelectedItem();

                    if (taskTitle.isEmpty() || taskDescription.isEmpty() || selectedPatientName == null) {
                        Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    JSONObject noteData = new JSONObject();
                    try {
                        noteData.put("title", taskTitle);
                        noteData.put("description", taskDescription);
                        noteData.put("priority", taskPriority);
                        noteData.put("patientName", selectedPatientName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Erro ao criar a nota!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    saveNoteToDatabase(userId, noteData, taskTitle, taskDescription, taskPriority, selectedPatientName);
                })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        dialog = builder.create();
    }


    private void saveNoteToDatabase(String userId, JSONObject noteData, String title, String description, String priority, String patient) {
        String url = String.format("https://careplus-696u.onrender.com/users/%s/notes/addNote", userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, noteData,
                response -> {

                    Toast.makeText(getContext(), "Tarefa adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                    addCard(title, description, priority, patient, userId);
                },
                error -> {

                    Log.e("VolleyError", "Erro ao salvar nota: " + error.getMessage());
                    Toast.makeText(getContext(), "Erro ao salvar a tarefa!", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void fetchNotes(String userId, TextView titleTextView) {
        String url = String.format("https://careplus-696u.onrender.com/users/%s/notes", userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        layout.removeAllViews();

                        JSONArray notesArray = response.getJSONArray("notes");

                        if (notesArray.length() == 0) {
                            titleTextView.setVisibility(View.VISIBLE);
                        } else {
                            titleTextView.setVisibility(View.GONE);

                            for (int i = 0; i < notesArray.length(); i++) {
                                JSONObject note = notesArray.getJSONObject(i);
                                String title = note.getString("title");
                                String description = note.getString("description");
                                String patient = note.getString("patientName");
                                String priority = note.getString("priority");

                                addCard(title, description, priority, patient, userId);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("JSONError", "Erro ao processar resposta JSON: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("VolleyError", "Erro ao buscar notas: " + error.getMessage());
                    Toast.makeText(getContext(), "Erro ao carregar as notas!", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }






    private void addCard(String title, String description, String priority, String patient, String userId) {
        final View view = getLayoutInflater().inflate(R.layout.card, null);

        TextView titleView = view.findViewById(R.id.title);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView patientView = view.findViewById(R.id.patient);
        View priorityIndicator = view.findViewById(R.id.priorityIndicator);
        Button delete = view.findViewById(R.id.delete);

        titleView.setText(title);
        descriptionView.setText(description);
        patientView.setText(patient);

        switch (priority.toLowerCase()) {
            case "alta":
                priorityIndicator.setBackgroundColor(Color.RED);
                break;
            case "média":
                priorityIndicator.setBackgroundColor(Color.YELLOW);
                break;
            case "baixa":
                priorityIndicator.setBackgroundColor(Color.GREEN);
                break;
            default:
                priorityIndicator.setBackgroundColor(Color.GRAY);
                break;
        }

        delete.setOnClickListener(v -> {
            deleteNoteFromDatabase(title, userId, () -> {
                // Remover o card do layout após sucesso na exclusão
                layout.removeView(view);

                // Verificar se ainda há notas no layout
                if (layout.getChildCount() == 0) {
                    TextView titleTextView = getView().findViewById(R.id.title);
                    titleTextView.setVisibility(View.VISIBLE);
                }

                Toast.makeText(getContext(), "Nota deletada com sucesso!", Toast.LENGTH_SHORT).show();
            });
        });

        layout.addView(view);

        // Garantir que o TextView "sem notas" seja ocultado
        TextView titleTextView = getView().findViewById(R.id.title);
        titleTextView.setVisibility(View.GONE);
    }


    private void deleteNoteFromDatabase(String noteTitle, String userId, Runnable onSuccess) {
        String url = String.format("https://careplus-696u.onrender.com/users/%s/notes/delete", userId);

        // Criar o corpo da requisição
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", noteTitle);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erro ao criar a requisição!", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    // Chamar a callback de sucesso
                    if (onSuccess != null) {
                        onSuccess.run();
                    }
                },
                error -> {
                    Log.e("VolleyError", "Erro ao deletar nota: " + error.getMessage());
                    Toast.makeText(getContext(), "Erro ao deletar a nota!", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void fetchPatients(String userId, Spinner spinner) {
        String url = String.format("https://careplus-696u.onrender.com/users/%s/patients", userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<String> patientNames = new ArrayList<>();

                    try {

                        JSONArray patientsArray = response.getJSONArray("patients");

                        for (int i = 0; i < patientsArray.length(); i++) {
                            JSONObject patient = patientsArray.getJSONObject(i);
                            String name = patient.getString("name");
                            patientNames.add(name);
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, patientNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONError", "Erro ao processar resposta JSON: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e("VolleyError", "Erro ao buscar pacientes: " + error.getMessage());
                });

        Volley.newRequestQueue(getContext()).add(request);
    }



}