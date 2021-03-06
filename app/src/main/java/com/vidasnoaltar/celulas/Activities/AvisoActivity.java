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
import com.vidasnoaltar.celulas.Dados.Aviso;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.Utils.TipoMsg;
import com.vidasnoaltar.celulas.Utils.Utils;
import com.vidasnoaltar.celulas.task.DeleteAvisoTask;
import com.vidasnoaltar.celulas.task.ListaAvisoTask;
import java.util.List;


public class AvisoActivity extends AppCompatActivity {

    final DbHelper db = new DbHelper(this);
    private ListView lstAvisos;
    private Toolbar mToolbar;
    private FloatingActionButton addAviso;
    private int celulaid = 0;
    private ImageView imageview_lista_vazia;
    private List<Aviso> avisosLst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);

        mToolbar = (Toolbar) findViewById(R.id.th_aviso);
        mToolbar.setTitle("Avisos");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addAviso = (FloatingActionButton) findViewById(R.id.add_aviso);
        addAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AvisoActivity.this, FormAvisoActivity.class);
                startActivity(intent);
            }
        });

        lstAvisos = (ListView) findViewById(R.id.lstAvisos);
        imageview_lista_vazia = (ImageView) findViewById(R.id.imageview_lista_vazia);

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
                registerForContextMenu(lstAvisos);
                addAviso.setVisibility(View.VISIBLE);
            }

            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            avisosLst = db.listaAviso("SELECT * FROM TB_AVISOS WHERE AVISOS_CELULA_ID = " + celulaid);
            ArrayAdapter<Aviso> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, avisosLst);

            lstAvisos.setAdapter(adapter);
            lstAvisos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    Aviso avisoSelecionado = (Aviso) adapterView.getItemAtPosition(position);
                    switch (adapterView.getId()) {
                        case R.id.lstAvisos:
                            Utils.showMsgAlertOK(AvisoActivity.this, avisoSelecionado.getTitulo(), avisoSelecionado.getConteudo(), TipoMsg.INFO);
                            break;
                    }
                }
            });
        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela avisos vazia!");
            lstAvisos.setAdapter(null); //Limpando Lista
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        new ListaAvisoTask(AvisoActivity.this, celulaid).execute();
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
                new DeleteAvisoTask(avisosLst.get(info.position), AvisoActivity.this).execute();
                return false;
            }
        });
    }


}


