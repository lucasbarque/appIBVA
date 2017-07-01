package com.vidasnoaltar.celulas.task;


import android.app.ProgressDialog;
import android.database.CursorIndexOutOfBoundsException;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import com.vidasnoaltar.celulas.Activities.UsuarioActivity;
import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

public class DeleteUsuarioTask extends AsyncTask<String, Object, Boolean> {
    private final Usuario usuario;
    private UsuarioActivity activity;
    private ProgressDialog alert;
    private ListView lstUsuarios;
    private ImageView imageview_lista_vazia;

    public DeleteUsuarioTask(Usuario usuario, UsuarioActivity activity) {
        this.usuario = usuario;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                alert = new ProgressDialog(activity);
                alert.setCancelable(false);
                alert.setTitle("Aguarde um momento");
                alert.setMessage("Excluindo membro");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(usuario.getId(), "usuarios");
            JSONObject jsonObject = new JSONObject(jsonResult);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean statusOK) {
        alert.dismiss();
        if (!statusOK) {
            Toast.makeText(activity, "Houve um erro ao remover o membro", Toast.LENGTH_LONG).show();
        } else {
            DbHelper db = new DbHelper(activity);
            try {
                db.alterar("DELETE FROM TB_USUARIOS WHERE ID = " + usuario.getId() + ";");
                List<Usuario> usuarioList = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE USUARIOS_CELULA_ID = " + Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")));
                ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, usuarioList);

                lstUsuarios = (ListView) activity.findViewById(R.id.usuarioslist);
                lstUsuarios.setAdapter(adapter);
                Toast.makeText(activity, "Membro excluído com sucesso", Toast.LENGTH_LONG).show();

            } catch (CursorIndexOutOfBoundsException e) {
                imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
                imageview_lista_vazia.setVisibility(View.VISIBLE);
            }

        }
    }
}
