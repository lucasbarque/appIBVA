package com.vidasnoaltar.celulas.task;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.converter.UsuarioConverter;
import com.vidasnoaltar.celulas.ws.WebService;

public class LoginTask extends AsyncTask<String, Object, Boolean> {
    private final Activity activity;
    private ProgressDialog alert;
    private ListView listview_aniversariantes;
    private ImageView imageViewListaVazia;
    private DbHelper db;

    public LoginTask(Activity activity) {
        this.activity = activity;
        try {
            db = new DbHelper(activity);
            listview_aniversariantes = (ListView) activity.findViewById(R.id.listview_aniversariantes);
            imageViewListaVazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
                    alert.setMessage("Estamos sincronizando os dados!");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listAll("usuarios");
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
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_USUARIOS;");
                System.out.println("O objeto acabou ficando vazio!");
                return false;
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
            List<Usuario> listaUsuario = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE USUARIOS_CELULA_ID = " + celulaid + ";");

            if (listaUsuario.size() > 0) {
                ArrayAdapter<Usuario> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listaUsuario);
                listview_aniversariantes.setAdapter(adapter);
                imageViewListaVazia.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            try {
                imageViewListaVazia.setVisibility(View.VISIBLE);
            } catch (Exception e1) {
                System.out.println("imageViewListaVazia vazia");
            }
        }
        if (!statusOK) {
            Toast.makeText(activity, "Você não esta conectado a internet", Toast.LENGTH_LONG).show();
        }
    }
}
