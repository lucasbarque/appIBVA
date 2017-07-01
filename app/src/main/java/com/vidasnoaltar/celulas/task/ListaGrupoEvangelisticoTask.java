package com.vidasnoaltar.celulas.task;


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
    private ListView listview_ge;
    private ImageView imageview_lista_vazia;

    public ListaGrupoEvangelisticoTask(GEActivity activity) {
        this.activity = activity;
        db = new DbHelper(activity);
        listview_ge = (ListView) activity.findViewById(R.id.listview_ge);
        imageview_lista_vazia = (ImageView) activity.findViewById(R.id.imageview_lista_vazia);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            WebService request = new WebService();
            String jsonResult = request.listAll("ges");
            JSONArray jsonArray = new JSONArray(jsonResult);
            List<GrupoEvangelistico> gruposevangelisticos = new GrupoEvangelisticoConverter().fromJson(jsonArray);
            if (gruposevangelisticos != null && !gruposevangelisticos.isEmpty()) {
                DbHelper db = new DbHelper(activity);
                db.alterar("DELETE FROM TB_GES;");
                for (int i = 0; i < gruposevangelisticos.size(); i++) {
                    db.atualizarGrupoEvangelistico(gruposevangelisticos.get(i));
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
        try {
            int celulaid = Integer.parseInt(db.consulta("SELECT USUARIOS_CELULA_ID FROM TB_LOGIN", "USUARIOS_CELULA_ID"));
            List<GrupoEvangelistico> listaGrupoEvangelistico = db.listaGrupoEvangelistico("SELECT * FROM TB_GES WHERE GES_CELULA_ID = " + celulaid + ";");
            ArrayAdapter<GrupoEvangelistico> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, listaGrupoEvangelistico);
            listview_ge.setAdapter(adapter);
            if (listaGrupoEvangelistico.size() > 0) {
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
