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

import com.vidasnoaltar.celulas.Activities.GEActivity;
import com.vidasnoaltar.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.converter.GrupoEvangelisticoConverter;
import com.vidasnoaltar.celulas.ws.WebService;

public class ListaGrupoEvangelisticoTask extends AsyncTask<String, Object, Boolean> {
    private final GEActivity activity;
    private DbHelper db;
    private int celulaId;
    private ProgressDialog alert;
    private ListView listview_ge;
    private ImageView imageview_lista_vazia;

    public ListaGrupoEvangelisticoTask(GEActivity activity, int celulaId) {
        this.activity = activity;
        this.celulaId = celulaId;
        db = new DbHelper(activity);
        listview_ge = (ListView) activity.findViewById(R.id.listview_ge);
        imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
    }

    @Override
    protected void onPreExecute() {

        if (db.contagem("SELECT COUNT(*) FROM TB_GES") <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos sincronizando os ges...");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listByCelula("ges", celulaId);
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<GrupoEvangelistico> ges = new GrupoEvangelisticoConverter().fromJson(jsonArray);
            if (ges != null && !ges.isEmpty()) {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_GES;");
                for (int i = 0; i < ges.size(); i++) {
                    db.atualizarGrupoEvangelistico(ges.get(i));
                }
                db.close();
            } else {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_GES;");
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
            List<GrupoEvangelistico> gesLst = db.listaGrupoEvangelistico("SELECT * FROM TB_GES WHERE GES_CELULA_ID = " + celulaid);
            ArrayAdapter<GrupoEvangelistico> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, gesLst);
            listview_ge.setAdapter(adapter);
            if (gesLst.size() > 0) {
                imageview_lista_vazia.setVisibility(View.GONE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            listview_ge.setAdapter(null); //Limpando Lista
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }

        if (!statusOK) {
            Toast.makeText(activity, "Você não está conectado à internet", Toast.LENGTH_LONG).show();
        }
    }
}
