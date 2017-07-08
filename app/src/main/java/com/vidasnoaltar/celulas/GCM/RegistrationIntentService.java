package com.vidasnoaltar.celulas.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.loopj.android.http.RequestParams;
import com.vidasnoaltar.celulas.Activities.LoginActivity;
import com.vidasnoaltar.celulas.Dados.Usuario;
import com.vidasnoaltar.celulas.R;
import com.vidasnoaltar.celulas.Repository.DbHelper;
import com.vidasnoaltar.celulas.ws.WebService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = RegistrationIntentService.class.getSimpleName();
    private int id_usuario;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        final DbHelper db = new DbHelper(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            if (db.contagem("SELECT COUNT(*) TB_LOGIN") > 0) {
                if (db.consulta("SELECT SENHA FROM TB_USUARIOS WHERE LOGIN = '" + db.consulta("SELECT LOGIN FROM TB_LOGIN;", "LOGIN") + "'", "SENHA")
                        .equals(db.consulta("SELECT SENHA FROM TB_LOGIN;", "SENHA"))) {
                 try{
                     try{
                         id_usuario = Integer.parseInt(db.consulta("SELECT ID_USUARIO FROM TB_LOGIN;", "ID_USUARIO"));
                         Usuario usuario = db.listaUsuario("SELECT * FROM TB_USUARIOS WHERE ID = '" + id_usuario + "'").get(0);
                         usuario.setAtivo(true);
                         usuario.setToken(token);
                         WebService request = new WebService();
                         request.save(usuario, "usuarios");
                     } catch (Exception e){
                         e.getMessage();
                     }

                 } catch (Exception e){
                     System.out.println("Deu merda");
                 }
                }
            } else {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}