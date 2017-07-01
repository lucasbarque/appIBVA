package com.vidasnoaltar.celulas.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.TipoMsg;
import com.vidasnoaltar.celulas.Utils.Utils;
import com.vidasnoaltar.celulas.task.SaveProgramacaoTask;

public class FormProgramacaoActivity extends AppCompatActivity {
    private EditText editTextNome;
    private EditText editTextData;
    private EditText editTextHorario;
    private EditText editTextEndereco;
    private EditText editTexTelefone;
    private EditText editTextValor;
    private Button buttonSalvar;
    private Toolbar mToolbar;
    private DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_programacao);

        mToolbar = (Toolbar) findViewById(R.id.th_add_programacao);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DbHelper(this);
        editTextNome = (EditText) findViewById(R.id.edittext_nome);
        editTextData = (EditText) findViewById(R.id.data_programacao);
        editTextHorario = (EditText) findViewById(R.id.horario_programacao);
        editTextEndereco = (EditText) findViewById(R.id.edittext_endereco);
        editTexTelefone = (EditText) findViewById(R.id.edittext_telefone);
        editTextValor = (EditText) findViewById(R.id.edittext_valor);
        buttonSalvar = (Button) findViewById(R.id.btnSalvar);

        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.mostraDatePickerDialog(FormProgramacaoActivity.this, editTextData);
            }
        });

        editTextHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.mostraTimePickerDialog(FormProgramacaoActivity.this, editTextHorario);
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNome.getText().toString().trim().isEmpty()) {
                    editTextNome.setError("Esta informação é obrigatoria");
                    editTextNome.requestFocus();
                } else if (editTextData.getText().toString().trim().isEmpty()) {
                    editTextData.setError("Esta informação é obrigatoria");
                    editTextData.requestFocus();
                } else if (editTextHorario.getText().toString().trim().isEmpty()) {
                    editTextHorario.setError("Esta informação é obrigatoria");
                    editTextHorario.requestFocus();
                } else if (editTextEndereco.getText().toString().trim().isEmpty()) {
                    editTextEndereco.setError("Esta informação é obrigatoria");
                    editTextEndereco.requestFocus();
                } else if (editTexTelefone.getText().toString().trim().isEmpty()) {
                    editTexTelefone.setError("Esta informação é obrigatoria");
                    editTexTelefone.requestFocus();
                } else if (editTextValor.getText().toString().trim().isEmpty()) {
                    editTextValor.setError("Esta informação é obrigatoria");
                    editTextValor.requestFocus();
                } else if (buttonSalvar.getText().toString().trim().isEmpty()) {
                    buttonSalvar.setError("Esta informação é obrigatoria");
                    buttonSalvar.requestFocus();
                } else {
                    Programacao programacao = new Programacao();
                    programacao.setData(editTextData.getText().toString());

                    SimpleDateFormat dataEntrada = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat datasaida = new SimpleDateFormat("yyyy-MM-dd");
                    String data = null;
                    try {
                        data = datasaida.format(dataEntrada.parse(programacao.getData()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    programacao.setNome(editTextNome.getText().toString());
                    programacao.setData(data);
                    programacao.setHorario(editTextHorario.getText().toString());
                    programacao.setLocal(editTextEndereco.getText().toString());
                    programacao.setTelefone(editTexTelefone.getText().toString());
                    programacao.setValor(editTextValor.getText().toString());
                    programacao.setProgramacoes_celula_id(Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")));
                    programacao.setAtivo(true);

                    new SaveProgramacaoTask(FormProgramacaoActivity.this, programacao).execute();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.showMessageConfirm(FormProgramacaoActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.gc();
                        finish();
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Utils.showMessageConfirm(FormProgramacaoActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.gc();
                finish();
            }
        });
    }
}
