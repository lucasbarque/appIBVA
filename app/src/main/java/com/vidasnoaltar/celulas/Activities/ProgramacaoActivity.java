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

import com.vidasnoaltar.celulas.Dados.Programacao;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.task.DeleteProgramacaoTask;
import com.vidasnoaltar.celulas.task.ListaProgramacaoTask;


public class ProgramacaoActivity extends AppCompatActivity {
    final DbHelper db = new DbHelper(this);
    private ListView listview_programacoes;
    private ImageView imageview_lista_vazia;
    private Toolbar mToolbar;
    private int celulaid = 0;
    private FloatingActionButton add_programacao;
    private List<Programacao> listaProgramacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_programacao);

        listview_programacoes = (ListView) findViewById(R.id.listview_programacoes);

        mToolbar = (Toolbar) findViewById(R.id.th_programacao);
        mToolbar.setTitle("Programações");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        add_programacao = (FloatingActionButton) findViewById(R.id.add_programacao);
        add_programacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProgramacaoActivity.this, FormProgramacaoActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onResume() {
        try {
            if (Integer.parseInt(db.consulta("SELECT PERFIL FROM TB_USUARIOS WHERE ID = " + db.consulta("SELECT ID FROM TB_LOGIN", "ID"), "PERFIL")) == 1) {
                registerForContextMenu(listview_programacoes);
                add_programacao.setVisibility(View.VISIBLE);
            }

            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            listaProgramacao = db.listaProgramacao("SELECT * FROM TB_PROGRAMACOES WHERE PROGRAMACOES_CELULA_ID = " + celulaid);
            ArrayAdapter<Programacao> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, listaProgramacao);

            listview_programacoes.setAdapter(adapter);
            listview_programacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ProgramacaoActivity.this, ProgramacaoSelecionadaActivity.class);
                    intent.putExtra("id_prog", listaProgramacao.get(position).getId());
                    startActivity(intent);
                }
            });

        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela avisos vazia!");
            imageview_lista_vazia = (ImageView) findViewById(R.id.imageview_lista_vazia);
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        new ListaProgramacaoTask(ProgramacaoActivity.this, celulaid).execute();
        super.onResume();
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
                new DeleteProgramacaoTask(listaProgramacao.get(info.position), ProgramacaoActivity.this).execute();
                return false;
            }
        });
    }
}