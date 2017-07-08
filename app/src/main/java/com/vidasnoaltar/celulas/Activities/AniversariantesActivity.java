package com.vidasnoaltar.celulas.Activities;


import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.task.ListaAniversarianteTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



public class AniversariantesActivity extends AppCompatActivity {


    private ListView listview_aniversariantes;
    private Toolbar mToolbar;
    private ImageView imageViewListaVazia;
    private int celulaid;
    final DbHelper db = new DbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniversariantes);

        imageViewListaVazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
        listview_aniversariantes = (ListView) findViewById(R.id.listview_aniversariantes);

        mToolbar = (Toolbar) findViewById(R.id.th_aniversariante);
        mToolbar.setTitle("Aniversariantes");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.gc();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        try {
            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            List<Usuario> listaUsuario = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE USUARIOS_CELULA_ID = " + celulaid + ";");
            for (int i = 0; i < listaUsuario.size(); i++) {
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date data = formatoEntrada.parse(listaUsuario.get(i).getNascimento());
                    Date dataMes = new Date();
                    if (data.getMonth() != dataMes.getMonth()) {
                        listaUsuario.remove(i);
                        i = 0;
                    }
                } catch (ParseException | NullPointerException e) {
                    System.out.println(e.getMessage());
                    listaUsuario.remove(i);
                }
            }
            if (listaUsuario.size() > 0) {
                ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, listaUsuario);

                listview_aniversariantes.setAdapter(adapter);
            } else {
                imageViewListaVazia.setVisibility(View.VISIBLE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela aniversariantes vazia!");
            imageViewListaVazia.setVisibility(View.VISIBLE);
        }
        new ListaAniversarianteTask(AniversariantesActivity.this, celulaid).execute();
        super.onResume();
    }
}
