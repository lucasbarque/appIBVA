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

import com.vidasnoaltar.celulas.Activities.AvisoActivity;
import com.vidasnoaltar.celulas.Dados.Aviso;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.converter.AvisoConverter;
import com.vidasnoaltar.celulas.ws.WebService;

public class ListaAvisoTask extends AsyncTask<String, Object, Boolean> {
    private final AvisoActivity activity;
    private int celulaId;
    private ProgressDialog alert;
    private DbHelper db;
    private ListView lstAvisos;
    private ImageView imageview_lista_vazia;
    private List<Aviso> avisos;

    public ListaAvisoTask(AvisoActivity activity, int celulaId) {
        this.activity = activity;
        this.celulaId = celulaId;
        db = new DbHelper(activity);
        lstAvisos = (ListView) activity.findViewById(R.id.lstAvisos);
        imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
    }

    @Override
    protected void onPreExecute() {

        if (db.contagem("SELECT COUNT(*) FROM TB_AVISOS") <= 0) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alert = new ProgressDialog(activity);
                    alert.setCancelable(false);
                    alert.setTitle("Aguarde um momento");
                    alert.setMessage("Estamos sincronizando os avisos");
                    alert.show();
                }
            });
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listByCelula("avisos", celulaId);
            JSONArray jsonArray = new JSONArray(jsonResult);
            avisos = new AvisoConverter().fromJson(jsonArray);
            if (avisos != null && !avisos.isEmpty()) {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_AVISOS;");
                for (int i = 0; i < avisos.size(); i++) {
                    db.atualizarAviso(avisos.get(i));
                }
                db.close();
            } else {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_AVISOS;");
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
            List<Aviso> avisosLst = db.listaAviso("SELECT * FROM TB_AVISOS WHERE AVISOS_CELULA_ID = " + celulaid);
            ArrayAdapter<Aviso> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, avisosLst);
            lstAvisos.setAdapter(adapter);
            if (avisosLst.size() > 0) {
                imageview_lista_vazia.setVisibility(View.GONE);
            }
        } catch (CursorIndexOutOfBoundsException e) {
            lstAvisos.setAdapter(null); //Limpando Lista
            imageview_lista_vazia.setVisibility(View.VISIBLE);
        }

        if (!statusOK) {
            Toast.makeText(activity, "Você não esta conectado a internet", Toast.LENGTH_LONG).show();
        }
    }
}
