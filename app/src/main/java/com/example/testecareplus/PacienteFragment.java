package com.example.testecareplus;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PacienteFragment extends Fragment {

    private EditText nomeText, idadeText, alturaText, tipoSanguineoText, alergiaText, obsText;
    private ImageButton ibUploadIcon, ibTrash, ibEdit;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar TextViews
        nomeText = view.findViewById(R.id.textNome);
        idadeText = view.findViewById(R.id.idadeText);
        alturaText = view.findViewById(R.id.alturaText);
        tipoSanguineoText = view.findViewById(R.id.tipoSanguineoText);
        alergiaText = view.findViewById(R.id.alergiaText);
        obsText = view.findViewById(R.id.obsText);
        ibUploadIcon = view.findViewById(R.id.ibUploadIcon);
        ibTrash = view.findViewById(R.id.btTrash);
        ibEdit = view.findViewById(R.id.btEdit);

        // Obter patientId do SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String patientId = prefs.getString("patientId", null);

        if (patientId != null) {
            fetchPatientData(patientId);
        } else {
            Toast.makeText(getContext(), "Erro: ID do paciente não encontrado", Toast.LENGTH_SHORT).show();
        }

        ibTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePatient(patientId);
            }
        });
        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void fetchPatientData(String patientId) {
        String url = "http://10.0.2.2:4060/patients/" + patientId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject patient = response.getJSONObject("patient");

                            // Atualizar TextViews com os dados recebidos
                            nomeText.setText(patient.getString("name"));
                            idadeText.setText(String.valueOf(patient.getInt("age")));
                            alturaText.setText(String.valueOf(patient.getInt("height")));
                            tipoSanguineoText.setText(patient.getString("bloodType"));
                            alergiaText.setText(patient.getString("allergies"));
                            obsText.setText(patient.getString("observations"));

                            // Decodificar a imagem base64 e exibir
                            String iconBase64 = patient.getString("icon");
                            if (iconBase64 != null && !iconBase64.isEmpty()) {
                                byte[] decodedString = Base64.decode(iconBase64, Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                ibUploadIcon.setImageBitmap(decodedByte);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Erro ao processar dados do paciente", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(getContext(), "Erro ao carregar dados do paciente", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }
    public void deletePatient(String patientId) {
        if (patientId == null) {
            Toast.makeText(getContext(), "ID do paciente não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = String.format("http://10.0.2.2:4060/patients/%s/updatePatient", patientId);

        JsonObjectRequest deleteRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                response -> {
                    if (response.has("message")) {
                        Toast.makeText(getContext(), "Paciente deletado com sucesso!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Erro: resposta inválida", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Erro ao deletar", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(deleteRequest);
    }
    public void editPatient(String patientId) {
        if (patientId == null) {
            Toast.makeText(getContext(), "ID do paciente não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = String.format("http://10.0.2.2:4060/patients/%s/deletePatient", patientId);

        JSONObject dadosBody = new JSONObject();
        try {
            dadosBody.put("name", nomeText);
            dadosBody.put("age", idadeText);
            dadosBody.put("icon", ibUploadIcon);
            dadosBody.put("observations", obsText);
            dadosBody.put("height", alturaText);
            dadosBody.put("bloodType", tipoSanguineoText);
            dadosBody.put("allergies", alergiaText);
        } catch (JSONException exc) {
            exc.printStackTrace();
        }
        JsonObjectRequest enviarPut = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                dadosBody,
                response -> {
                    if (response.has("patientId")) {
                        Toast.makeText(getContext(), "Paciente atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getContext(), "Erro: resposta inválida", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Erro ao enviar", Toast.LENGTH_SHORT).show();
                }
        );

        // Envia a requisição para a fila de requisições
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(enviarPut);
    }
}
