package com.vidasnoaltar.celulas.Activities;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.vidasnoaltar.celulas.Dados.Celula;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.task.ListaCelulaTask;

public class CelulaActivity extends AppCompatActivity {

    final DbHelper db = new DbHelper(this);
    private TextView nome;
    private TextView lider;
    private TextView dia;
    private TextView horario;
    private TextView local;
    private TextView semana;
    private TextView periodo;
    private TextView versiculo;
    private Toolbar mToolbar;
    private int celulaid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_celula);

        mToolbar = (Toolbar) findViewById(R.id.th_celula);
        mToolbar.setTitle("Célula");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = (TextView) findViewById(R.id.nome);
        lider = (TextView) findViewById(R.id.lider);
        dia = (TextView) findViewById(R.id.dia);
        horario = (TextView) findViewById(R.id.horario);
        local = (TextView) findViewById(R.id.local);
        semana = (TextView) findViewById(R.id.semana);
        versiculo = (TextView) findViewById(R.id.versiculo);
    }

    @Override
    protected void onResume() {
        try {
            celulaid = Integer.parseInt(db.consulta("SELECT * FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            Celula celula = db.listaCelula("SELECT * FROM TB_CELULAS WHERE ID =" + celulaid).get(0);

            nome.setText(celula.getNome());
            lider.setText(celula.getLider());
            dia.setText(celula.converteDiaCelula());
            horario.setText(celula.getHorario());
            local.setText(celula.getLocal());
            semana.setText(celula.converteDiaJejum() + " - " + celula.getPeriodo());
            versiculo.setText("\"" + celula.getVersiculo() + "\"");

        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        new ListaCelulaTask(CelulaActivity.this, celulaid).execute();
        super.onResume();
    }

    private boolean temPermissao() {
        if (Integer.parseInt(db.consulta("SELECT PERFIL FROM TB_USUARIOS WHERE ID = " + db.consulta("SELECT ID FROM TB_LOGIN", "ID"), "PERFIL")) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_celula, menu);

//        if (!temPermissao()){
//            MenuItem item = menu.findItem(R.id.action_editar);
//            item.setVisible(false);
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (temPermissao()) {
//            if (item.getItemId() == R.id.action_editar) {
//                Intent intent = new Intent(this, CelulaEditarActivity.class);
//                startActivity(intent);
//            }
//        }
        switch (item.getItemId()) {
            case android.R.id.home:
                System.gc();
                finish();
                break;
            case R.id.usuarios:
                Intent usuarios = new Intent(this, UsuarioActivity.class);
                startActivity(usuarios);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}