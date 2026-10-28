package com.vn.otofun.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;

import java.security.MessageDigest;

/**
 * Created by AnhVT on 2/16/16.
 */
public class SecureUtils {
    public static final int VALID   = 0;
    public static final int INVALID = 1;

    private static final String SIGNATURE = "mg03PSsWOOAFMt17BkSYakPWxoU=";
    private static final String PLAY_STORE_APP_ID = "com.android.vending";

    public static int checkAppSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {
                byte[] signatureBytes = signature.toByteArray();
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT).replaceAll("\\p{Cntrl}", "");
                // compare signatures
                if (SIGNATURE.equals(currentSignature)) {
                    return VALID;
                }
            }
        } catch (Exception e) {
            //assumes an issue in checking signature., but we let the caller decide on what to do.
            e.printStackTrace();
        }

        return INVALID;
    }

    public static boolean verifyInstaller(Context context) {
        final String installer = context.getPackageManager()
                .getInstallerPackageName(context.getPackageName());
        return installer != null && installer.startsWith(PLAY_STORE_APP_ID);
    }

    public static boolean checkDebuggable(Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static boolean checkEmulator() {
        try {
            boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
            boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;
            boolean sdk = getSystemProperty("ro.product.model").equals("sdk");

            if (emu || goldfish || sdk || "generic".equals(Build.BRAND.toLowerCase())) {
                return true;
            }

        } catch (Exception e) {

        }

        return false;
    }

    private static String getSystemProperty(String name) throws Exception {
        Class systemPropertyClazz = Class.forName("android.os.SystemProperties");
        return (String) systemPropertyClazz.getMethod("get", new Class[]{String.class})
                .invoke(systemPropertyClazz, new Object[]{name});
    }
}
