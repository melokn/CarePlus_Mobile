package com.example.testecareplus;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class PacienteFragment extends Fragment {

    private TextView nomeText, idadeText, alturaText, tipoSanguineoText, alergiaText, obsText;
    private ImageButton ibUploadIcon;

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

        // Obter patientId do SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String patientId = prefs.getString("patientId", null);

        if (patientId != null) {
            fetchPatientData(patientId);
        } else {
            Toast.makeText(getContext(), "Erro: ID do paciente não encontrado", Toast.LENGTH_SHORT).show();
        }
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
}
