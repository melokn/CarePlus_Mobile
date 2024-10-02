package com.example.testecareplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        String urlLogin = "http://10.0.2.2:4060/users/login";


        EditText campoEmail = findViewById(R.id.campoEmailLogin);
        EditText campoSenha = findViewById(R.id.campoSenhaLogin);
        Button btLogin = findViewById(R.id.btLogin);
        Button btCadastro = findViewById(R.id.btCadastro);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject dadosBody = new JSONObject();

                String email = campoEmail.getText().toString().trim();
                String senha = campoSenha.getText().toString().trim();


                if (email.isEmpty() || senha.isEmpty()) {
                    // Exibir uma mensagem de erro se algum campo estiver vazio
                    Toast.makeText(LoginFragment.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    dadosBody.put("email", email);
                    dadosBody.put("password", senha);
                }catch(JSONException exc){
                    exc.printStackTrace();
                }

                JsonObjectRequest enviarPost = new JsonObjectRequest(
                        Request.Method.POST,
                        urlLogin,
                        dadosBody,
                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                try {
                                    // Supondo que o ID do usuário está em um campo chamado "userId"
                                    if (response.has("userId")) {
                                        String userId = response.getString("userId");
                                        // Armazene o ID do usuário conforme necessário
                                        // Por exemplo, você pode armazená-lo em SharedPreferences ou usá-lo diretamente
                                        // Exemplo de armazenamento em SharedPreferences:
                                        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = prefs.edit();
                                        editor.putString("userId", userId);
                                        editor.apply();

                                        // Exibir uma mensagem de sucesso e iniciar a próxima atividade
                                        Toast.makeText(LoginFragment.this, "Cadastrado", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginFragment.this, HomeFragment.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginFragment.this, "Erro: resposta inválida", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(LoginFragment.this, "Erro ao processar a resposta", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                Toast.makeText(LoginFragment.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                            }
                        }
                );

                RequestQueue queue = Volley.newRequestQueue(LoginFragment.this);
                queue.add(enviarPost);
            }
        });

        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFragment.this, TelaCadastro.class);
                startActivity(intent);
            }
        });

    }

}