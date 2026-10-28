package com.vn.otofun.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;
import com.vn.otofun.BuildConfig;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.provider.Settings;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.Date;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

import android.os.Build;

import com.vn.otofun.OtofunApplication;
import com.vn.otofun.R;

/**
 * Created by AnhVT on 6/2/15.
 */
public final class Commons {

    final static ArrayList<String> normalFunctions = new ArrayList<>();
    final static String dic = "aáàảãạăắằẳẵặâấầẩẫậbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòỏõọôốồỗộơớờởỡợpqrstuvwxyýỳỷỹỵz";

    // adapted from post by Phil Haack and modified to match better
    final static String tagStart =
            "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
    final static String tagEnd =
            "\\</\\w+\\>";
    final static String tagSelfClosing =
            "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
    final static String htmlEntity =
            "&[a-zA-Z][a-zA-Z0-9]+;";
    final static Pattern htmlPattern = Pattern.compile(
            "(" + tagStart + ".*" + tagEnd + ")|(" + tagSelfClosing + ")|(" + htmlEntity + ")",
            Pattern.DOTALL
    );


    public final static String DOMAIN = "http://muabanotoxemaycu.com";
    public final static String API_ROOT_NEW = DOMAIN + "/otofun/api/heaven_gate";

    static HashMap<String, String> mDeviceInfoMap = null;

    public static HashMap getDefaultAPIParams(Context context) {
        if (mDeviceInfoMap == null) {
            VLog.d("mDeviceInfoMap null. Get device info");
            mDeviceInfoMap = new HashMap<>();

            try {
                final String device_id = Utils.getUUID(context);
                if (device_id != null) {
                    mDeviceInfoMap.put("device_id", device_id);
                }

                final String deviceModel = Utils.getDeviceName();//Build.MANUFACTURER;//Utils.getDeviceName();
//                final String deviceName = Build.USER;

//                final String deviceName = Settings.System.getString(getContentResolver(), "device_name");

//                if (deviceName != null) {
//                    mDeviceInfoMap.put("device_name", deviceName);
//                }


                if (deviceModel != null) {
                    mDeviceInfoMap.put("device_type", deviceModel);
                }

                final String appVersion = BuildConfig.VERSION_NAME;
                if (appVersion != null) {
                    mDeviceInfoMap.put("app_version", appVersion);
                }

                final String osVersion = Utils.getOsVersion();
                if (osVersion != null) {
                    mDeviceInfoMap.put("os_version", osVersion);
                }

                final String phoneNum = Utils.getMobileNumber(context);
                if (phoneNum != null) {
                    mDeviceInfoMap.put("phone", phoneNum);
                }
                mDeviceInfoMap.put("os_type", "2");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return mDeviceInfoMap;
    }

    public final static Locale LOCALE_VN = new Locale("vi", "vn");

    public static void sendGAEvent(Activity activity, String category, String action, String label, long value) {
//        Tracker tracker = ((OtofunApplication) activity.getApplication()).getGoogleAnalyticsTracker();
//        tracker.send(new HitBuilders.EventBuilder()
//                .setCategory(category)
//                .setAction(action)
//                .setLabel(label)
//                .setValue(value).build());
    }

    public static void sendTrackerScreen(Context context, String screenName) {
//        Tracker tracker = getTracker(context);
//        if (tracker != null) {
//            tracker.setScreenName(screenName);
//            tracker.send(new HitBuilders.ScreenViewBuilder().build());
//        }
    }

//    private static synchronized Tracker getTracker(Context context) {
//        try {
//            final GoogleAnalytics googleAnalytics = GoogleAnalytics.getInstance(context);
//            return googleAnalytics.newTracker(R.xml.app_tracker);
//        } catch (final Exception e) {
//            e.fillInStackTrace();
//        }
//
//        return null;
//    }


    public synchronized static int compareVietnameseWord(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int len = Math.max(s1.length(), s2.length());
        for (int i = s1.length(); i < len; i++) {
            s1 += " ";
        }
        for (int i = s2.length(); i < len; i++) {
            s2 += " ";
        }
        for (int i = 0; i < s1.length(); i++) {
            if (dic.indexOf(s1.charAt(i)) != -1) {
                if (dic.indexOf(s1.charAt(i)) - dic.indexOf(s2.charAt(i)) != 0) {
                    return dic.indexOf(s1.charAt(i)) - dic.indexOf(s2.charAt(i));
                }
            } else {
                return String.valueOf(s1.charAt(i)).compareTo(String.valueOf(s2.charAt(i)));
            }
        }
        return 0;
    }


    public static String checkValidUserName(String userName) {
        if (userName.length() < 6 || userName.length() > 30) {
            return "Tên truy cập không hợp lệ, phải từ 6 - 30 ký tự";
        }
        return null;
    }

    public static String checkValidPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            return "Số điện thoại không hợp lệ, phải từ 10 - 11 số";
        }

        if (!isNumeric(phoneNumber)) {
            return "Số điện thoại chỉ được gồm các chữ số";
        }

        if (parseTelephoneProvider(phoneNumber) == null) {
            return "Số điện thoại không thuộc nhà mạng nào";
        }

        return null;
    }

    public static String checkValidEmail(String email) {
        if (!isValidEmail(email)) {
            return "Địa chỉ email không hợp lệ";
        }
        return null;
    }

    public static String checkValidPassword(String password) {
        if (password.length() < 6 || password.length() > 20) {
            return "Mật khẩu không hợp lệ, phải từ 6 - 20 ký tự";
        }
        return null;
    }

    public static String checkValidAddress(String address) {
        if (address.length() > 100) {
            return "Địa chỉ quá dài, nhập ít hơn 100 ký tự";
        }
        return null;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {

        if (!isNumeric(phoneNumber)) {
            return false;
        }

        if (phoneNumber.length() < 10 || phoneNumber.length() > 11) {
            return false;
        }

//        if (phoneNumber.startsWith("09") && phoneNumber.length() != 10) {
//            return false;
//        }
//
//        if (phoneNumber.startsWith("01") && phoneNumber.length() != 11) {
//            return false;
//        }

        if (parseTelephoneProvider(phoneNumber) == null) {
            return false;
        }

        return true;
    }

    public static String formatHtmlContent(String content) {
        String header = "<HTML><HEAD><link rel='stylesheet' href='style.css' type='text/css'></HEAD><BODY>";
        String footer = "</BODY></HTML>";
        String tag = "<meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1\\\" />";

        return (header + tag + content + footer);
    }

    public static String parseTelephoneProvider(String phoneNunber) {
        String phone = formatPhoneNumber(phoneNunber);
        String[] viettelPrefixes = new String[]{"096", "097", "098", "0162", "0163", "0164", "0165", "0166", "0167", "0168", "0169", "086"};
        String[] mobifonePrefixes = new String[]{"090", "093", "0120", "0121", "0122", "0126", "0128", "089"};
        String[] vinaphonePrefixes = new String[]{"091", "094", "0123", "0124", "0125", "0127", "0129", "088"};
        String[] vietnamobilePrefixes = new String[]{"092", "0188", "0186"};
        String[] beelinePrefixes = new String[]{"099", "0199"};
        String[] sfonePrefixes = new String[]{"095"};

        for (String prefix : viettelPrefixes) {
            if (phone.startsWith(prefix)) {
                return "VIETTEL";
            }
        }
        for (String prefix : mobifonePrefixes) {
            if (phone.startsWith(prefix)) {
                return "MOBIFONE";
            }
        }
        for (String prefix : vinaphonePrefixes) {
            if (phone.startsWith(prefix)) {
                return "VINAPHONE";
            }
        }
        for (String prefix : vietnamobilePrefixes) {
            if (phone.startsWith(prefix)) {
                return "VIETNAMOBILE";
            }
        }
        for (String prefix : beelinePrefixes) {
            if (phone.startsWith(prefix)) {
                return "BEELINE";
            }
        }
        for (String prefix : sfonePrefixes) {
            if (phone.startsWith(prefix)) {
                return "SFONE";
            }
        }
        return null;
    }

    public static String parseTelephoneProviderShorthand(String phoneNumber) {
        String phone = formatPhoneNumber(phoneNumber);
        String[] viettelPrefixes = new String[]{"096", "097", "098", "0162", "0163", "0164", "0165", "0166", "0167", "0168", "0169", "086"};
        String[] mobifonePrefixes = new String[]{"090", "093", "0120", "0121", "0122", "0126", "0128", "089"};
        String[] vinaphonePrefixes = new String[]{"091", "094", "0123", "0124", "0125", "0127", "0129", "088"};
        String[] vietnamobilePrefixes = new String[]{"092", "0188", "0186"};
        String[] beelinePrefixes = new String[]{"099", "0199"};
        String[] sfonePrefixes = new String[]{"095"};

        for (String prefix : viettelPrefixes) {
            if (phone.startsWith(prefix)) {
                return "VTEL";
            }
        }
        for (String prefix : mobifonePrefixes) {
            if (phone.startsWith(prefix)) {
                return "VMS";
            }
        }
        for (String prefix : vinaphonePrefixes) {
            if (phone.startsWith(prefix)) {
                return "VNP";
            }
        }
        for (String prefix : vietnamobilePrefixes) {
            if (phone.startsWith(prefix)) {
                return "VNM";
            }
        }
        for (String prefix : beelinePrefixes) {
            if (phone.startsWith(prefix)) {
                return "GTEL";
            }
        }
        for (String prefix : sfonePrefixes) {
            if (phone.startsWith(prefix)) {
                return "SFONE";
            }
        }
        return "UNKNOWN";
    }

    public static String parseCreditCardType(String cardNumber) {
        String number = cardNumber.trim().replaceAll("\\s+", "");
        char firstDigit = number.charAt(0);
        if (firstDigit == '4') {
            return "001";
        } else if (firstDigit == '5') {
            return "002";
        }
        return "";
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }



    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void setupViewListenSoftKeyboard(View view, final Activity activity) {
        //Set up touch listener for non-text1 box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupViewListenSoftKeyboard(innerView, activity);
            }
        }
    }

    public synchronized static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public synchronized static void logVeryLongString(String TAG, String message) {
        int maxLogSize = 1000;
        for (int i = 0; i <= message.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > message.length() ? message.length() : end;
            Log.d(TAG, message.substring(start, end));
        }

    }

    public synchronized static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public synchronized static String formatPhoneNumberWithCountryCode(String phone) {
        if (phone.startsWith("0")) {
            return "84" + phone.substring(1);
        } else {
            return phone;
        }
    }

    public synchronized static String formatPhoneNumberToDisplay(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            String result = phone.trim().replaceAll("[^0-9]", "");
            if (result.length() >= 10 && result.length() <= 11) {
                MessageFormat formater = new MessageFormat("{0}.{1}.{2}");
                String[] strArr = {result.substring(0, 4), result.substring(4, 7), result.substring(7)};
                return formater.format(strArr);
            }
        }
        return phone;
    }

    public synchronized static String getIpAddress(Context context) {
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
    }

    public synchronized static String getMACAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        return wInfo.getMacAddress();
    }



    public static synchronized boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(activity, resultCode, 9000).show();
//            } else {
//                Log.i("VIMO", "This device is not supported.");
//            }
            return false;
        }
        return true;
    }


    private static final String ALGORITHM = "AES/ECB/PKCS7Padding";

    public synchronized static String encrypt(String plainString, String keyString) throws Exception {
        Key key = generateKey(keyString);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(plainString.getBytes());
        String encryptedValue = Base64.encodeToString(encValue, Base64.DEFAULT).replaceAll("\\p{C}", "");
        return encryptedValue;
    }

    public synchronized static String decrypt(String encryptedString, String keyString) throws Exception {
        Key key = generateKey(keyString);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedString, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        if (decValue != null)
            return new String(decValue);
        else
            return "";
    }

    public synchronized static String toCurrencyString(long money) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###đ", symbols);
        return decimalFormat.format(money);
    }

    public synchronized static String toCurrencyStringVND(long money) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###đ", symbols);
        return decimalFormat.format(money);
    }
    public synchronized static String toCurrencyStringCNY(double money) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##¥", symbols);
        return decimalFormat.format(money);
    }

    public synchronized static String formatNumberString(long number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);
        return decimalFormat.format(number);
    }

    public synchronized static String formatPhoneNumber(String s) {
        if (s.startsWith("84")) {
            return "0" + s.substring(2);
        } else if (s.startsWith("+84")) {
            return "0" + s.substring(3);
        } else {
            return s;
        }
    }

    public synchronized static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private static Key generateKey(String keyString) throws Exception {
        byte[] bKey = keyString.getBytes("UTF-8");
        bKey = Arrays.copyOf(bKey, 16);
        Key key = new SecretKeySpec(bKey, ALGORITHM);
        return key;
    }

    public static boolean isHtml(String s) {
        boolean ret = false;
        if (s != null) {
            ret = htmlPattern.matcher(s).find();
        }
        return ret;
    }

    public static String formatDate(String timeString, String formatString) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US);
        SimpleDateFormat fmt2 = new SimpleDateFormat("kk:mm, dd/MM/yyyy", Locale.US);
        if (formatString != null) {
            fmt2 = new SimpleDateFormat(formatString, Locale.US);
        }
        try {
            Date date = fmt.parse(timeString);
            return fmt2.format(date);
        }
        catch(ParseException pe) {

            return "Date";
        }
    }

//    public static String formatDate2(String timeString) {
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US);
//        SimpleDateFormat fmt2 = new SimpleDateFormat("kk:mm", Locale.US);
//        try {
//            Date date = fmt.parse(timeString);
//            return fmt2.format(date);
//        }
//        catch(ParseException pe) {
//
//            return "Date";
//        }
//    }

    public static String formatToYesterdayOrToday(String date) {
        try {
            Date dateTime = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US).parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateTime);
            Calendar today = Calendar.getInstance();
            Calendar yesterday = Calendar.getInstance();
            yesterday.add(Calendar.DATE, -1);
            DateFormat timeFormatter = new SimpleDateFormat("kk:mm", Locale.US);

            if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
                return timeFormatter.format(dateTime) + ", hôm nay";
            } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
                return timeFormatter.format(dateTime) + ", hôm qua";
            } else {
                SimpleDateFormat fmt2 = new SimpleDateFormat("kk:mm, dd/MM/yyyy", Locale.US);
                return fmt2.format(dateTime);
            }
        } catch(ParseException pe) {
            return date;
        }
    }
}
