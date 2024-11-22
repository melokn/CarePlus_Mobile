package com.example.testecareplus;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArtigosFragment extends Fragment {

    String currentUsername;
    LinearLayout layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artigos, container, false);
        layout = view.findViewById(R.id.container);
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        currentUsername = prefs.getString("usuario", null);

        fetchHints();
        return view;
    }

    private void fetchHints() {
        String url = "http://192.168.15.10:4060/hints";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        layout.removeAllViews();

                        JSONArray hintsArray = response.getJSONArray("hints");

                        if (hintsArray.length() != 0) {
                            for (int i = 0; i < hintsArray.length(); i++) {
                                JSONObject hint = hintsArray.getJSONObject(i);
                                String hintId = hint.getString("id");
                                String author = hint.getString("author");
                                String content = hint.getString("content");

                                addCard(hintId, author, content);
                            }
                        } else {

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
    private void addCard(String hintId, String author, String content) {
        final View view = getLayoutInflater().inflate(R.layout.hint_card, null);

        TextView authorView = view.findViewById(R.id.author);
        TextView contentView = view.findViewById(R.id.content);
        ImageButton delete = view.findViewById(R.id.btTrashHint);

        // Verificar se o autor é o mesmo que o usuário atual
        if (!author.equals(currentUsername)) {
            delete.setVisibility(View.GONE); // Ocultar o botão de deletar
        } else {
            delete.setVisibility(View.VISIBLE); // Mostrar o botão para o autor
            delete.setOnClickListener(v -> {
                deleteHintFromDatabase(hintId, () -> {
                    layout.removeView(view);
                    Toast.makeText(getContext(), "Dica deletada com sucesso!", Toast.LENGTH_SHORT).show();
                });
            });
        }

        authorView.setText(author);
        contentView.setText(content);

        layout.addView(view);
    }
    private void deleteHintFromDatabase(String hintId, Runnable onSuccess) {
        String url = "http://192.168.15.10:4060/hint/delete";

        // Criar o corpo da requisição
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("hintId", hintId);
            Log.d("HintDebug", "hintId enviado: " + hintId);

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
                    Log.e("VolleyError", "Erro ao deletar dica: " + error.getMessage());
                    Toast.makeText(getContext(), "Erro ao deletar a dica!", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(getContext()).add(request);
    }
}