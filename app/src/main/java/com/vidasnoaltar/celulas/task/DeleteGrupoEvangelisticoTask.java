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

import com.vidasnoaltar.celulas.Activities.GEActivity;
import com.vidasnoaltar.celulas.Dados.GrupoEvangelistico;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

public class DeleteGrupoEvangelisticoTask extends AsyncTask<String, Object, Boolean> {
    private final GrupoEvangelistico grupoevangelistico;
    private GEActivity activity;
    private ProgressDialog alert;
    private ListView listview_ge;
    private ImageView imageview_lista_vazia;

    public DeleteGrupoEvangelisticoTask(GrupoEvangelistico grupoevangelistico, GEActivity activity) {
        this.grupoevangelistico = grupoevangelistico;
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
                alert.setMessage("Excluindo Grupo Evangelistico");
                alert.show();
            }
        });
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.delete(grupoevangelistico.getId(), "ges");
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
            Toast.makeText(activity, "Houve um erro ao remover o grupoevangelistico", Toast.LENGTH_LONG).show();
        } else {
            DbHelper db = new DbHelper(activity);
            try {
                db.alterar("DELETE FROM TB_GES WHERE ID = " + grupoevangelistico.getId());
                List<GrupoEvangelistico> listaGrupoEvangelistico = db.listaGrupoEvangelistico("SELECT * FROM TB_GES WHERE GES_CELULA_ID = " + Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID")) + ";");
                ArrayAdapter<GrupoEvangelistico> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listaGrupoEvangelistico);

                listview_ge = (ListView) activity.findViewById(R.id.listview_ge);
                listview_ge.setAdapter(adapter);
                Toast.makeText(activity, "Grupo Evangelistico excluida com sucesso", Toast.LENGTH_LONG).show();
            } catch (CursorIndexOutOfBoundsException e) {
                imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
                imageview_lista_vazia.setVisibility(View.VISIBLE);
            }
        }
    }
}
