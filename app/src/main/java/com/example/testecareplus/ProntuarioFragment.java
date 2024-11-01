package com.example.testecareplus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProntuarioFragment extends Fragment {

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri selectedImageUri;
    private ImageButton uploadIcon;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prontuario, container, false);
        ImageButton btSave = view.findViewById(R.id.btSave);
        ImageButton btTrash = view.findViewById(R.id.btTrash);
        uploadIcon = view.findViewById(R.id.ibUploadIcon);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        uploadIcon.setImageURI(selectedImageUri);
                    }
                }
        );

        btTrash.setOnClickListener(view1 -> {
            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String patientId = prefs.getString("patientId", null);
            deletePatient(patientId);
        });

        uploadIcon.setOnClickListener(v -> openGallery());

        btSave.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                    savePatient();

            } else {
                Toast.makeText(getContext(), "Por favor, selecione uma imagem", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }

    private String convertImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void savePatient() {
        // Obtém outros dados do paciente dos campos de entrada
        EditText campoName = getView().findViewById(R.id.textNome);
        EditText campoIdade = getView().findViewById(R.id.idadeText);
        EditText campoAltura = getView().findViewById(R.id.alturaText);
        EditText campoTipoSanguineo = getView().findViewById(R.id.tipoSanguineoText);
        EditText campoAlergias = getView().findViewById(R.id.alergiaText);
        EditText campoObservacoes = getView().findViewById(R.id.obsText);

        String nome = campoName.getText().toString().trim();
        int idade = Integer.parseInt(campoIdade.getText().toString().trim());
        int altura = Integer.parseInt(campoAltura.getText().toString().trim());
        String tipoSanguineo = campoTipoSanguineo.getText().toString().trim();
        String alergias = campoAlergias.getText().toString().trim();
        String observacoes = campoObservacoes.getText().toString().trim();
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        String base64Image = convertImageToBase64(selectedImageUri);

        // Criação do JSON para envio ao backend
        JSONObject dadosBody = new JSONObject();
        try {
            dadosBody.put("name", nome);
            dadosBody.put("age", idade);
            dadosBody.put("icon", base64Image);
            dadosBody.put("observations", observacoes);
            dadosBody.put("height", altura);
            dadosBody.put("bloodType", tipoSanguineo);
            dadosBody.put("allergies", alergias);
            dadosBody.put("createdBy", userId);
        } catch (JSONException exc) {
            exc.printStackTrace();
        }

        String url = String.format("http://10.0.2.2:4060/users/%s/patients/newPatient", userId);
        JsonObjectRequest enviarPost = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dadosBody,
                response -> {
                    if (response.has("patientId")) {
                        Toast.makeText(getContext(), "Paciente criado com sucesso!", Toast.LENGTH_SHORT).show();
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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(enviarPost);
    }

    public void deletePatient(String patientId) {
        if (patientId == null) {
            Toast.makeText(getContext(), "ID do paciente não encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = String.format("http://10.0.2.2:4060/patients/%s/deletePatient", patientId);

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
}
