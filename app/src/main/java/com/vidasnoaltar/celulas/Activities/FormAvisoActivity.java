package com.vidasnoaltar.celulas.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vidasnoaltar.celulas.Dados.Aviso;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.TipoMsg;
import com.vidasnoaltar.celulas.Utils.Utils;
import com.vidasnoaltar.celulas.task.SaveAvisoTask;

public class FormAvisoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTitulo;
    private EditText editTextConteudo;
    private Button buttonSalvar;
    private Toolbar mToolbar;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_aviso);

        db = new DbHelper(this);

        insereListener();
        mToolbar = (Toolbar) findViewById(R.id.th_add_aviso);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void insereListener() {
        getButtonSalvar().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_salvar:
                if (verificaCampos()) {
                    Aviso aviso = new Aviso();
                    aviso.setAvisos_celula_id(Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")));
                    aviso.setAtivo(true);
                    aviso.setCreated(db.pegaDataHoraAtual());
                    aviso.setModified(db.pegaDataHoraAtual());
                    aviso.setTitulo(getEditTextTitulo().getText().toString());
                    aviso.setConteudo(getEditTextConteudo().getText().toString());

                    new SaveAvisoTask(this, aviso).execute();
                }
                break;
        }
    }

    private boolean verificaCampos() {
        boolean camposPreenchidos = true;
        if (getEditTextTitulo().getText().length() <= 0) {
            getEditTextTitulo().setError("Por favor, digite o título");
            camposPreenchidos = false;
        }

        if (getEditTextConteudo().getText().length() <= 0) {
            getEditTextConteudo().setError("Por favor, digite o conteúdo");
            camposPreenchidos = false;
        }
        return camposPreenchidos;
    }

    public EditText getEditTextTitulo() {
        if (editTextTitulo == null) {
            editTextTitulo = (EditText) findViewById(R.id.edittext_titulo);
        }
        return editTextTitulo;
    }

    public EditText getEditTextConteudo() {
        if (editTextConteudo == null) {
            editTextConteudo = (EditText) findViewById(R.id.edittext_conteudo);
        }
        return editTextConteudo;
    }

    public Button getButtonSalvar() {
        if (buttonSalvar == null) {
            buttonSalvar = (Button) findViewById(R.id.button_salvar);
        }
        return buttonSalvar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.showMessageConfirm(FormAvisoActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
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
        Utils.showMessageConfirm(FormAvisoActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.gc();
                finish();
            }
        });
    }
}
