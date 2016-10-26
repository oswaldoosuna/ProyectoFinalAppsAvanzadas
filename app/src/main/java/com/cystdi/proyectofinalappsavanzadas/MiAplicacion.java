package com.cystdi.proyectofinalappsavanzadas;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase para obtener la clave SHA1 por código
 */

public class MiAplicacion extends Application{
    private static final String TAG = "APP";

    @Override
    public void onCreate() {
        super.onCreate();
        mostrarHashKey();
    }

    public void mostrarHashKey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.cystdi.proyectofinalappavanzadas",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Codigo KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

}
