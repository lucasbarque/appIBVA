package com.vidasnoaltar.celulas.Activities;

import android.content.DialogInterface;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.TipoMsg;
import com.vidasnoaltar.celulas.Utils.Utils;
import com.vidasnoaltar.celulas.task.SaveUsuarioTask;

public class FormUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText sobrenome;
    private EditText email;
    private EditText data_nascimento;
    private EditText login;
    private EditText senha;
    private EditText confirma_senha;
    private Button salvar;
    private Toolbar mToolbar;
    final DbHelper db = new DbHelper(this);
    private int celulaid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_usuario);
        mToolbar = (Toolbar) findViewById(R.id.th_add_registrar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = (EditText) findViewById(R.id.nome);
        sobrenome = (EditText) findViewById(R.id.sobrenome);
        email = (EditText) findViewById(R.id.email);
        data_nascimento = (EditText) findViewById(R.id.data_nascimento);
        data_nascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.mostraDatePickerDialog(FormUsuarioActivity.this, data_nascimento);
            }
        });

        login = (EditText) findViewById(R.id.login);
        senha = (EditText) findViewById(R.id.senha);
        confirma_senha = (EditText) findViewById(R.id.confirma_senha);
        salvar = (Button) findViewById(R.id.salvar);



        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificaCampos()) {
                    try {
                        celulaid = Integer.parseInt(db.consulta("SELECT * FROM TB_LOGIN", "USUARIOS_CELULA_ID"));

                        Usuario usuario = new Usuario();

                        usuario.setNascimento(data_nascimento.getText().toString());
                        SimpleDateFormat dataEntrada = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat datasaida = new SimpleDateFormat("yyyy-MM-dd");
                        String data = null;
                        try {
                            data = datasaida.format(dataEntrada.parse(usuario.getNascimento()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        usuario.setNome(nome.getText().toString());
                        usuario.setSobrenome(sobrenome.getText().toString());
                        usuario.setUsuarios_celula_id(celulaid);
                        usuario.setLogin(login.getText().toString());
                        usuario.setSenha(senha.getText().toString());
                        usuario.setEmail(email.getText().toString());
                        usuario.setNascimento(data);
                        usuario.setPerfil(0);
                        usuario.setAtivo(true);

                        new SaveUsuarioTask(FormUsuarioActivity.this, usuario).execute();

                    } catch (CursorIndexOutOfBoundsException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (nome.getText().length() <= 0) {
            nome.setError("Por favor, digite seu nome");
            camposPreenchidos = false;
        }

        if (sobrenome.getText().length() <= 0) {
            sobrenome.setError("Por favor, digite seu sobrenome");
            camposPreenchidos = false;
        }

        if (email.getText().length() <= 0) {
            email.setError("Por favor, digite seu email");
            camposPreenchidos = false;
        }

        data_nascimento.setError(null);
        if (data_nascimento.getText().length() <= 0) {
            data_nascimento.setError("Por favor, selecione sua data de nascimento");
            camposPreenchidos = false;
        } else {
            if (data_nascimento.getTag() != null) {
                if (((Calendar) data_nascimento.getTag()).get(Calendar.YEAR) > Calendar.getInstance().get(Calendar.YEAR)) {
                    Toast.makeText(this, "Digite uma data válida por favor.", Toast.LENGTH_LONG).show();
                    data_nascimento.setError("Por favor, digite uma data válida.");
                    camposPreenchidos = false;
                }
            }
        }

        if (login.getText().length() <= 0) {
            login.setError("Por favor, digite um nome para seu login");
            camposPreenchidos = false;
        }

        if (senha.getText().length() <= 0) {
            senha.setError("Por favor, digite uma senha");
            camposPreenchidos = false;
        } else {
            if (!senha.getText().toString().equals(confirma_senha.getText().toString())) {
                confirma_senha.setError("Campo confirmação de senha não confere com a senha digitada");
                camposPreenchidos = false;
            }
        }

        return camposPreenchidos;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.showMessageConfirm(FormUsuarioActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.gc();
                        finish();
                    }
                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Utils.showMessageConfirm(FormUsuarioActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.gc();
                finish();
            }
        });
    }

}
