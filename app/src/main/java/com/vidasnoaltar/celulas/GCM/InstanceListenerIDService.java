package com.vidasnoaltar.celulas.GCM;
import android.content.Intent;
import android.util.Log;
import com.google.android.gms.iid.InstanceIDListenerService;

public class InstanceListenerIDService extends InstanceIDListenerService{
    private static final String TAG = InstanceListenerIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh(){
        Log.d(TAG, "Novo token de usuário!");
        Intent i = new Intent(this, RegistrationIntentService.class);
        startService(i);
    }
}
