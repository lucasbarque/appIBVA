package com.vidasnoaltar.celulas.GCM;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.vidasnoaltar.celulas.Activities.AvisoActivity;
import com.vidasnoaltar.celulas.Activities.GEActivity;
import com.vidasnoaltar.celulas.Activities.PrincipalActivity;
import com.vidasnoaltar.celulas.Activities.ProgramacaoActivity;
import com.vidasnoaltar.celulas.Dados.DadosNotification;
import com.vidasnoaltar.celulas.Notification.NotificationUtil;

public class GCMPushReceiver extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String destino = bundle.getString("destino");

        String titulo = bundle.getString("titulo");
        String conteudo = bundle.getString("conteudo");
        String data = bundle.getString("data");
        String imgUrl = bundle.getString("imgUrl");
        String iconUrl = bundle.getString("iconUrl");
        String count = bundle.getString("count");

        DadosNotification dadosNotification = new DadosNotification(titulo, conteudo, data);

        NotificationUtil notificationUtil = new NotificationUtil(getApplicationContext());

        Intent location = retornaIntent(destino, dadosNotification);

        notificationUtil.showBigNotificationMsg(
                titulo,
                conteudo,
                data,
                location,
                count != null ? Integer.parseInt(count) : null,
                iconUrl,
                imgUrl
        );
    }

    private Intent retornaIntent(String location, DadosNotification dados){
        switch (location){
            case "all":
                Intent all = new Intent(getApplicationContext(), PrincipalActivity.class);
                all.putExtra("notification", dados);
                return all;
            case "aviso":
                Intent aviso = new Intent(getApplicationContext(), AvisoActivity.class);
                aviso.putExtra("notification", dados);
                return aviso;
            case "programacao":
                Intent programacao = new Intent(getApplicationContext(), ProgramacaoActivity.class);
                programacao.putExtra("notification", dados);
                return programacao;
            case "ge":
                Intent ge = new Intent(getApplicationContext(), GEActivity.class);
                ge.putExtra("notification", dados);
                return ge;
            case "usuario":
                Intent usuario = new Intent(getApplicationContext(), GEActivity.class);
                usuario.putExtra("notification", dados);
                return usuario;
        }
        return null;
    }
}
