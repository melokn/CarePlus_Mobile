package com.example.testecareplus;

import android.content.Intent;
import android.content.SharedPreferences;
<<<<<<< HEAD
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

=======
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
<<<<<<< HEAD
import android.widget.Button;
=======
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

<<<<<<< HEAD
=======
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

<<<<<<< HEAD
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;


public class ProntuarioFragment extends AppCompatActivity {

    public void createPatient(String name, int age, int height, String bloodType, String allergies, String observations, String iconUrl) throws JSONException {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        String url = String.format("http://10.0.2.2:4060/users/%s/patients/newPatient", userId);

        JSONObject dadosBody = new JSONObject();

        try{
            dadosBody.put("name", name);
            dadosBody.put("age", age);
            dadosBody.put("urlIcon", height);
            dadosBody.put("observations", observations);
            dadosBody.put("height", height);
            dadosBody.put("bloodType", bloodType);
            dadosBody.put("allergies", allergies);
        }catch(JSONException exc){
            exc.printStackTrace();
        }
        JsonObjectRequest enviarPost = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dadosBody,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        if (response.has("patientId")) {
                             Toast.makeText(ProntuarioFragment.this, "Paciente criado com sucesso!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProntuarioFragment.this, HomeFragment.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ProntuarioFragment.this, "Erro: resposta inválida", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ProntuarioFragment.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(ProntuarioFragment.this);
        queue.add(enviarPost);
    }

    public void deletePatient(String patientId){

        String url = "http://10.0.2.2:4060/patients/delete";

        JSONObject dadosBody = new JSONObject();

        try{
            dadosBody.put("patientId", patientId);
        }catch(JSONException exc){
            exc.printStackTrace();
        }

        JsonObjectRequest enviarDelete = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                dadosBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(ProntuarioFragment.this, "Paciente deletado com sucesso!", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProntuarioFragment.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(ProntuarioFragment.this);
        queue.add(enviarDelete);
    }
    private ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_prontuario);

        ImageButton btSave = findViewById(R.id.btSave);
        ImageButton btTrash = findViewById(R.id.btTrash);
        ImageButton uploadIcon = findViewById(R.id.ibUploadIcon);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
=======
import java.io.File;

public class ProntuarioFragment extends Fragment {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prontuario, container, false);
        ImageButton btSave = view.findViewById(R.id.btSave);
        ImageButton btTrash = view.findViewById(R.id.btTrash);
        ImageButton uploadIcon = view.findViewById(R.id.ibUploadIcon);
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
                        Uri selectedImage = result.getData().getData();
                        handleImage(selectedImage);
                    }
                }
        );
<<<<<<< HEAD
        btTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String patientId = prefs.getString("patientId", null);
                deletePatient(patientId);
            }
        });
        uploadIcon.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        }));
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText campoName = findViewById(R.id.textNome);
                EditText campoIdade = findViewById(R.id.idadeText);
                EditText campoAltura = findViewById(R.id.alturaText);
                EditText campoTipoSanguineo = findViewById(R.id.tipoSanguineoText);
                EditText campoAlergias = findViewById(R.id.alergiaText);
                EditText campoObservacoes = findViewById(R.id.obsText);

                String nome = campoName.getText().toString().trim();
                int idade = Integer.parseInt(campoIdade.getText().toString().trim());
                int altura = Integer.parseInt(campoAltura.getText().toString().trim());
                String tipoSanguineo = campoTipoSanguineo.getText().toString().trim();
                String alergias = campoAlergias.getText().toString().trim();
                String observacoes = campoObservacoes.getText().toString().trim();
                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                String iconUrl = prefs.getString("iconUrl", null);

                try {
                    createPatient(nome, idade, altura, tipoSanguineo, alergias, observacoes, iconUrl);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
=======

        btTrash.setOnClickListener(view1 -> {
            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
            String patientId = prefs.getString("patientId", null);
            deletePatient(patientId);
        });

        uploadIcon.setOnClickListener(v -> {
            openGallery();
        });
        btSave.setOnClickListener(v -> {
            EditText campoName = view.findViewById(R.id.textNome);
            EditText campoIdade = view.findViewById(R.id.idadeText);
            EditText campoAltura = view.findViewById(R.id.alturaText);
            EditText campoTipoSanguineo = view.findViewById(R.id.tipoSanguineoText);
            EditText campoAlergias = view.findViewById(R.id.alergiaText);
            EditText campoObservacoes = view.findViewById(R.id.obsText);

            String nome = campoName.getText().toString().trim();
            int idade = Integer.parseInt(campoIdade.getText().toString().trim());
            int altura = Integer.parseInt(campoAltura.getText().toString().trim());
            String tipoSanguineo = campoTipoSanguineo.getText().toString().trim();
            String alergias = campoAlergias.getText().toString().trim();
            String observacoes = campoObservacoes.getText().toString().trim();
            SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
            String iconUrl = prefs.getString("iconUrl", null);

            try {
                createPatient(nome, idade, altura, tipoSanguineo, alergias, observacoes, iconUrl);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });

        return view;
    }

>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(intent);
    }
<<<<<<< HEAD
    private void handleImage(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
=======

    private void handleImage(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            // Enviar a imagem para o S3
            uploadImageToS3(picturePath);
        }
    }
<<<<<<< HEAD
    private void uploadImageToS3(String picturePath) {
        AmazonS3Client s3Client = new AmazonS3Client(new CognitoCachingCredentialsProvider(
                getApplicationContext(),
=======

    private void uploadImageToS3(String picturePath) {
        AmazonS3Client s3Client = new AmazonS3Client(new CognitoCachingCredentialsProvider(
                getContext(),
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
                "us-east-1:e4489803-0edd-44c7-93f8-cb3314550ab3",
                Regions.US_EAST_1
        ));

        File file = new File(picturePath);
        String bucketName = "buckettopper";
        String key = "imagens/" + file.getName();

<<<<<<< HEAD
        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getApplicationContext()).build();
=======
        TransferUtility transferUtility = TransferUtility.builder().s3Client(s3Client).context(getContext()).build();
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
        TransferObserver uploadObserver = transferUtility.upload(bucketName, key, file);

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (state == TransferState.COMPLETED) {
<<<<<<< HEAD

                    String iconUrl = s3Client.getUrl(bucketName, key).toString();
                    SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
=======
                    String iconUrl = s3Client.getUrl(bucketName, key).toString();
                    SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("iconUrl", iconUrl);
                    editor.apply();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                double percentDone = ((double) bytesCurrent / (double) bytesTotal) * 100;
                Log.d("Upload", "Progresso: " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("Upload", "Erro: " + ex.getMessage());
            }
        });
    }

<<<<<<< HEAD


}
=======
    public void createPatient(String name, int age, int height, String bloodType, String allergies, String observations, String iconUrl) throws JSONException {
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        String userId = prefs.getString("userId", null);

        String url = String.format("http://10.0.2.2:4060/users/%s/patients/newPatient", userId);

        JSONObject dadosBody = new JSONObject();

        try {
            dadosBody.put("name", name);
            dadosBody.put("age", age);
            dadosBody.put("urlIcon", iconUrl);
            dadosBody.put("observations", observations);
            dadosBody.put("height", height);
            dadosBody.put("bloodType", bloodType);
            dadosBody.put("allergies", allergies);
            dadosBody.put("createdBy", userId);
        } catch (JSONException exc) {
            exc.printStackTrace();
        }

        JsonObjectRequest enviarPost = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dadosBody,
                response -> {
                    if (response.has("patientId")) {
                        Toast.makeText(getContext(), "Paciente criado com sucesso!", Toast.LENGTH_SHORT).show();
                        // Substituir por navegação para outro fragment
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
        String url = "http://10.0.2.2:4060/patients/delete";

        JSONObject dadosBody = new JSONObject();
        try {
            dadosBody.put("patientId", patientId);
        } catch (JSONException exc) {
            exc.printStackTrace();
        }

        JsonObjectRequest enviarDelete = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                dadosBody,
                response -> Toast.makeText(getContext(), "Paciente deletado com sucesso!", Toast.LENGTH_SHORT).show(),
                error -> Toast.makeText(getContext(), "Erro ao deletar", Toast.LENGTH_SHORT).show()
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(enviarDelete);
    }
}
>>>>>>> 9ff707657df96969e9577072b114e45c83f3a74c
