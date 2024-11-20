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

        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", null);
        if (userId == null) {
            Toast.makeText(getContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT).show();
        }
        buildDialog(userId);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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


        fetchPatients(userId, selectPatient);


        builder.setTitle("Adicione a sua tarefa!")
                .setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String taskTitle = title.getText().toString();
                        String taskDescription = name.getText().toString();
                        String taskPriority = prioritySpinner.getSelectedItem().toString();
                        String selectedPatientName = (String) selectPatient.getSelectedItem();

                        addCard(taskTitle, taskDescription, taskPriority, selectedPatientName);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


        dialog = builder.create();
    }





    private void addCard(String title, String description, String priority, String patient) {
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


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });


        layout.addView(view);
    }
    private void fetchPatients(String userId, Spinner spinner) {
        String url = String.format("http://10.0.2.2:4060/users/%s/patients", userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    List<String> patientNames = new ArrayList<>();

                    try {
                        // Obter o array de pacientes da chave "patients"
                        JSONArray patientsArray = response.getJSONArray("patients");

                        for (int i = 0; i < patientsArray.length(); i++) {
                            JSONObject patient = patientsArray.getJSONObject(i);
                            String name = patient.getString("name");
                            patientNames.add(name); // Adiciona apenas o nome ao Spinner
                        }

                        // Configurar o Spinner com os nomes dos pacientes
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