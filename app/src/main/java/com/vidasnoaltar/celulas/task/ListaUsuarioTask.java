package com.vidasnoaltar.celulas.task;


import android.app.ProgressDialog;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.List;

import com.vidasnoaltar.celulas.Activities.UsuarioActivity;
import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.converter.UsuarioConverter;
import com.vidasnoaltar.celulas.ws.WebService;

public class ListaUsuarioTask extends AsyncTask<String, Object, Boolean> {
    private final UsuarioActivity activity;
    private int celulaId;
    private ProgressDialog alert;
    private DbHelper db;
    private ListView lstUsuarios;
    private ImageView imageview_lista_vazia;


    public ListaUsuarioTask(UsuarioActivity activity, int celulaId) {
        this.activity = activity;
        this.celulaId = celulaId;
        db = new DbHelper(activity);
        lstUsuarios = (ListView) activity.findViewById(R.id.usuarioslist);
        imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
    }


    @Override
    protected void onPreExecute() {
        if (db.contagem("SELECT COUNT(*) FROM TB_USUARIOS") <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos sincronizando os membros da sua célula!");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listByCelula("usuarios", celulaId);
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<Usuario> usuarios = new UsuarioConverter().fromJson(jsonArray);
            if (usuarios != null && !usuarios.isEmpty()) {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_USUARIOS;");
                for (int i = 0; i < usuarios.size(); i++) {
                    db.atualizarUsuario(usuarios.get(i));
                }
                db.close();
            } else {
                System.out.println("O objeto acabou ficando vazio!");
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    alert.dismiss();
                } catch (NullPointerException e) {
                    System.out.println("Alert esta nulo");
                }
            }
        });
        try {
            int celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            List<Usuario> usuarioList = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE USUARIOS_CELULA_ID = " + celulaid);
            ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, usuarioList);
            lstUsuarios.setAdapter(adapter);
            if (usuarioList.size() > 0) {
                imageview_lista_vazia.setVisibility(View.GONE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }

        if (!statusOK) {
            Toast.makeText(activity, "Você não esta conectado a internet", Toast.LENGTH_LONG).show();
        }
    }
}
