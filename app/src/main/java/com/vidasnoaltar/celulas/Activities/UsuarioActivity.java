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

import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.task.DeleteUsuarioTask;
import com.vidasnoaltar.celulas.task.ListaUsuarioTask;

public class UsuarioActivity extends AppCompatActivity {

    final DbHelper db = new DbHelper(this);
    private ListView lstUsuarios;
    private Toolbar mToolbar;
    private FloatingActionButton addUsuario;
    private int celulaid = 0;
    private ImageView imageview_lista_vazia;
    private List<Usuario> usuariosLst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);

        mToolbar = (Toolbar) findViewById(R.id.th_usuario);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addUsuario = (FloatingActionButton) findViewById(R.id.add_usuario);
        addUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsuarioActivity.this, FormUsuarioActivity.class);
                startActivity(intent);
            }
        });

        lstUsuarios = (ListView) findViewById(R.id.usuarioslist);
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
                registerForContextMenu(lstUsuarios);
                addUsuario.setVisibility(View.VISIBLE);
            }

            celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            usuariosLst = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE USUARIOS_CELULA_ID = " + celulaid);
            ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, usuariosLst);

            lstUsuarios.setAdapter(adapter);

        } catch (CursorIndexOutOfBoundsException e) {
            System.out.println("Tabela usuarios vazia!");
            lstUsuarios.setAdapter(null); //Limpando Lista
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }
        new ListaUsuarioTask(UsuarioActivity.this, celulaid).execute();
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
                new DeleteUsuarioTask(usuariosLst.get(info.position), UsuarioActivity.this).execute();
                return false;
            }
        });
    }

}


