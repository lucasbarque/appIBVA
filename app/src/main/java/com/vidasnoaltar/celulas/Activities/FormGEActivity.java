package com.vidasnoaltar.celulas.Activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vidasnoaltar.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.TipoMsg;
import com.vidasnoaltar.celulas.Utils.Utils;
import com.vidasnoaltar.celulas.task.SaveGrupoEvangelisticoTask;


public class FormGEActivity extends AppCompatActivity {
    private EditText edittextNome;
    private Button buttonSalvar;
    private Toolbar mToolbar;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_ge);

        db = new DbHelper(this);

        mToolbar = (Toolbar) findViewById(R.id.th_add_ge);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edittextNome = (EditText) findViewById(R.id.edittext_nome);

        buttonSalvar = (Button) findViewById(R.id.button_salvar);
        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittextNome.getText().toString().trim().isEmpty()) {
                    edittextNome.setError("Informe o nome do GE");
                    edittextNome.requestFocus();
                } else {
                    GrupoEvangelistico grupoEvangelistico = new GrupoEvangelistico();
                    grupoEvangelistico.setNome(edittextNome.getText().toString());
                    grupoEvangelistico.setAtivo(true);
                    grupoEvangelistico.setData(db.pegaDataAtual());
                    grupoEvangelistico.setGes_celula_id(Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")));

                    new SaveGrupoEvangelisticoTask(FormGEActivity.this, grupoEvangelistico).execute();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.showMessageConfirm(FormGEActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
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
        Utils.showMessageConfirm(FormGEActivity.this, "ATENÇÃO", "Tem certeza que deseja sair? As alterações não salvas serão perdidas", TipoMsg.ALERTA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.gc();
                finish();
            }
        });
    }
}
