package com.vidasnoaltar.celulas.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;

import com.vidasnoaltar.celulas.Dados.Celula;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Utils.Utils;


public class CelulaEditarActivity  extends AppCompatActivity {

    private Celula celula;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_celula);

        celula = Utils.retornaCelulaSharedPreferences(this);

        ((EditText) findViewById(R.id.edittext_nome_lider)).setText(celula.getLider());
        //((Spinner) findViewById(R.id.data_celula))
        ((EditText) findViewById(R.id.horario_celula)).setText(celula.getHorario());
//        ((EditText) findViewById(R.id.edittext_local)).setText(celula.getLocal_celula());
        //((Spinner) findViewById(R.id.edittext_dia_jejum))
        //((Spinner) findViewById(R.id.edittext_dia_semana))
        ((EditText) findViewById(R.id.edittext_versiculo)).setText(celula.getVersiculo());

        mToolbar = (Toolbar) findViewById(R.id.th_edit_celula);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.gc();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
