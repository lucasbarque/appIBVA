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

import com.vidasnoaltar.celulas.Activities.AvisoActivity;
import com.vidasnoaltar.celulas.Dados.Aviso;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

public class DeleteAvisoTask extends AsyncTask<String, Object, Boolean> {
    private final Aviso aviso;
    private AvisoActivity activity;
    private ProgressDialog alert;
    private ListView lstAvisos;
    private ImageView imageview_lista_vazia;

    public DeleteAvisoTask(Aviso aviso, AvisoActivity activity) {
        this.aviso = aviso;
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
                alert.setMessage("Excluindo aviso");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(aviso.getId(), "avisos");
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
            Toast.makeText(activity, "Houve um erro ao remover o aviso", Toast.LENGTH_LONG).show();
        } else {
            DbHelper db = new DbHelper(activity);
            try {
                db.alterar("DELETE FROM TB_AVISOS WHERE ID = " + aviso.getId() + ";");
                List<Aviso> avisosLst = db.listaAviso("SELECT * FROM TB_AVISOS WHERE AVISOS_CELULA_ID = " + Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")));
                ArrayAdapter<Aviso> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, avisosLst);

                lstAvisos = (ListView) activity.findViewById(R.id.lstAvisos);
                lstAvisos.setAdapter(adapter);
                Toast.makeText(activity, "Aviso excluido com sucesso", Toast.LENGTH_LONG).show();

            } catch (CursorIndexOutOfBoundsException e) {
                imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
                imageview_lista_vazia.setVisibility(View.VISIBLE);
            }

        }
    }
}
