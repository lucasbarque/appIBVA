package com.vidasnoaltar.celulas.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.vidasnoaltar.celulas.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationUtil {

    private Context context;
    public static final int NOTIFICATION_ID = 357;

    public static final int BIG_NOTIFICATION_ID = 358;

    public NotificationUtil(Context context) {
        this.context = context;
    }

    public void showSmallNotificationMsg(String titulo, String msg, String timestamp, Integer count, String iconUrl, Intent intent){
        showBigNotificationMsg(titulo, msg, timestamp, intent, count, iconUrl, null);
    }

    public void showBigNotificationMsg(String titulo, String msg, String timestamp, Intent intent, Integer count, String iconUrl,String imgURL) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }

        Bitmap icon = null;
        if(iconUrl == null){
            icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.noti);
        }


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        //Uri somAlarme = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/notification");
        Uri somAlarme = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (TextUtils.isEmpty(imgURL)) {
            showSmallNotification(builder, icon, titulo, msg, timestamp, count, pendingIntent, somAlarme);
        } else {
            if (imgURL.length() > 4 && Patterns.WEB_URL.matcher(imgURL).matches()) {
                Bitmap bitmap = getBitmapFromUrl(imgURL);
                if(bitmap != null){
                    showBigNotification(bitmap, builder, icon, titulo, msg, timestamp, count, pendingIntent, somAlarme );
                } else {
                    showSmallNotification(builder, icon, titulo, msg, timestamp, count, pendingIntent, somAlarme);
                }
            }
        }
    }


    private void showSmallNotification(NotificationCompat.Builder builder,
                                       Bitmap icon,
                                       String titulo,
                                       String msg,
                                       String timestamp,
                                       Integer count,
                                       PendingIntent pendingIntent,
                                       Uri somAlarme) {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        inboxStyle.addLine(msg);
        builder.setSmallIcon(R.mipmap.noti)
                .setTicker(titulo)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(somAlarme)
                .setWhen(getTimeInMilliSec(timestamp))
                .setLargeIcon(icon)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.GREEN, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(inboxStyle);

        if(count != null){
            builder = builder.setNumber(count);
        }
        Notification notification = builder.build();
        notification.vibrate = new long[]{150, 300, 150, 600 };

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);



    }

    private void showBigNotification(Bitmap bitmap,
                                     NotificationCompat.Builder builder,
                                     Bitmap icon,
                                     String titulo,
                                     String msg,
                                     String timestamp,
                                     Integer count,
                                     PendingIntent pendingIntent,
                                     Uri somAlarme) {

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(titulo);
        bigPictureStyle.setSummaryText(Html.fromHtml(msg).toString());
        bigPictureStyle.bigPicture(bitmap);

        builder.setSmallIcon(R.mipmap.noti)
                .setTicker(titulo)
                .setContentTitle(titulo)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(somAlarme)
                .setWhen(getTimeInMilliSec(timestamp))
                .setLargeIcon(icon)
                .setStyle(bigPictureStyle)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setLights(Color.GREEN, 3000, 3000)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .build();

        if(count != null){
            builder = builder.setNumber(count);
        }
        Notification notification = builder.build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(BIG_NOTIFICATION_ID, notification);


    }

    public static long getTimeInMilliSec(String timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date data = format.parse(timestamp);
            return data.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Bitmap getBitmapFromUrl(String stringUrl) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
