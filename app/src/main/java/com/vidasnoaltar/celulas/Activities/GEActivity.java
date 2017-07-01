package com.vidasnoaltar.celulas.Activities;

import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import com.vidasnoaltar.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.task.DeleteGrupoEvangelisticoTask;
import com.vidasnoaltar.celulas.task.ListaGrupoEvangelisticoTask;

public class GEActivity extends AppCompatActivity {
    final DbHelper db = new DbHelper(this);
    private ListView listview_ge;
    private ImageView imageview_lista_vazia;
    private Toolbar mToolbar;
    private int celulaid;
    private FloatingActionButton add_ge;
    private List<GrupoEvangelistico> listaGrupoEvangelistico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge);

        mToolbar = (Toolbar) findViewById(R.id.th_ge);
        mToolbar.setTitle("Grupo Evangelístico");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listview_ge = (ListView) findViewById(R.id.listview_ge);
        add_ge = (FloatingActionButton) findViewById(R.id.add_ge);
        add_ge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GEActivity.this, FormGEActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        MenuItem deletar = menu.add("Excluir");
        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                new DeleteGrupoEvangelisticoTask(listaGrupoEvangelistico.get(info.position), GEActivity.this).execute();
                return false;
            }
        });
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
            if (Integer.parseInt(db.consulta("SELECT PERFIL FROM TB_USUARIOS WHERE ID = " + db.consulta("SELECT ID FROM TB_LOGIN", "ID"), "PERFIL")) == 1) {
                registerForContextMenu(listview_ge);
                add_ge.setVisibility(View.VISIBLE);
            }

            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            listaGrupoEvangelistico = db.listaGrupoEvangelistico("SELECT * FROM TB_GES WHERE GES_CELULA_ID = " + celulaid + ";");
            ArrayAdapter<GrupoEvangelistico> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listaGrupoEvangelistico);

            listview_ge.setAdapter(adapter);

        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela avisos vazia!");
            imageview_lista_vazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        new ListaGrupoEvangelisticoTask(GEActivity.this).execute();
        super.onResume();
    }
}


