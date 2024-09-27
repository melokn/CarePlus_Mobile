package com.example.testecareplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TelaEscolha extends AppCompatActivity {
    public void createUser(String funcaoUsuario){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String senha = prefs.getString("senha", null);
        String usuario = prefs.getString("usuario", null);


        String url = "http://10.0.2.2:4060/users";


        JSONObject dadosBody = new JSONObject();
        try{
            dadosBody.put("name", usuario);
            dadosBody.put("email", email);
            dadosBody.put("password", senha);
            dadosBody.put("userFunction", funcaoUsuario);
        }catch(JSONException exc){
            exc.printStackTrace();
        }
        JsonObjectRequest enviarPost = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dadosBody,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            // Supondo que o ID do usuário está em um campo chamado "userId"
                            if (response.has("userId")) {
                                String userId = response.getString("userId");
                                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("userId", userId);
                                editor.apply();

                                // Exibir uma mensagem de sucesso e iniciar a próxima atividade
                                Toast.makeText(TelaEscolha.this, "Cadastrado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TelaEscolha.this, TelaPrincipal.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(TelaEscolha.this, "Erro: resposta inválida", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TelaEscolha.this, "Erro ao processar a resposta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(TelaEscolha.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(TelaEscolha.this);
        queue.add(enviarPost);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_escolha);
        ImageButton familia = findViewById(R.id.ibFamila);  // Corrija o ID para corresponder ao XML
        ImageButton cuidador = findViewById(R.id.ibCuidador);  // Corrija o ID para corresponder ao XML

        // Configurar o Listener do botão Familia
        familia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar uma Intent para iniciar a TelaCadastro
                String funcaoUsuario = "Familiar";
                createUser(funcaoUsuario);
            }
        });

        cuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String funcaoUsuario = "Cuidador";
                createUser(funcaoUsuario);
            }
        });
    }
}