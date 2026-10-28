package com.vn.otofun.utils;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.internal.LinkedTreeMap;
import com.vn.otofun.R;
import com.vn.otofun.data.Prefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;

public class Utils {

	public static String readInputStream(InputStream is) {
		StringBuilder b = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			String line;

			while ((line = br.readLine()) != null) {
				b.append(line);
				b.append('\n');
			}
			br.close();
			br = null;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return b.toString().trim();
	}

	public static String readFile(File f) {
		StringBuilder b = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			String line;

			while ((line = br.readLine()) != null) {
				b.append(line);
				b.append('\n');
			}
			br.close();
			br = null;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return b.toString();
	}

	public static void writeFile(File f, String data) {
		try {
			if (!f.exists())
				f.createNewFile();

			FileOutputStream fos = new FileOutputStream(f);
			fos.write(data.getBytes("UTF-8"));
			fos.flush();
			fos.close();
			fos = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getOsVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	public static String getDeviceName() {
		String deviceModel = null;
		try {
			String manufacturer = Build.MANUFACTURER;
			String model = Build.MODEL;
			if (model.startsWith(manufacturer)) {
				deviceModel = capitalize(model);
			} else {
				deviceModel = capitalize(manufacturer) + " " + model;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (deviceModel == null) {
			deviceModel = "android";
		}

		if (deviceModel.length() <= 0) {
			deviceModel = "android";
		}

		return deviceModel;
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {

		}
		return null;
	}

	public static int getAppVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {

		}
		return 0;
	}

	private static String DEVICE_UUID = "";

	public static String getUUID(Context context) {
		if (DEVICE_UUID == null || DEVICE_UUID.length() == 0) {
			// Try to retrieve IMEI/MEID or ESN:
//			try {
//				TelephonyManager tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//				DEVICE_UUID = tManager.getDeviceId();
//			} catch (Exception e) {
//			}

			// If the ID does not available (on tablets that do not have phone function)
			// Then try to retrieve hardware S/N instead:
//			if (DEVICE_UUID == null || DEVICE_UUID.length() == 0) {
//				try {
//					DEVICE_UUID = Build.class.getField("SERIAL").get(null).toString();
//				} catch (Exception e) {
//				}
//			}

			// When even hardware S/N not found, then we use the Android unique ID as below:
			// *** Note that: this ID will be changed when user do a hard reset,
			// and in some case, due to manufacture problems, they may use the same ID for all devices in the same series.
			if (DEVICE_UUID == null || DEVICE_UUID.length() == 0) {
				DEVICE_UUID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
			}
		}
		return DEVICE_UUID;
	}

	public static String getAndroidID(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String getMobileNumber(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return tm.getLine1Number();
		} catch (Exception e) {
			return "";
		}
	}

	public static boolean hasOsVersion(int ver) {
		return Build.VERSION.SDK_INT >= ver;
	}

	public static int convertPxToDp(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		float ret = (px) / scale;
		return (int) ret;
	}

	public static int convertDpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		float ret = dp * scale;
		;
		return (int) ret;
	}

	public static int convertSpToPx(Context context, float sp) {
		final float scale = context.getResources().getDisplayMetrics().scaledDensity;
		float ret = sp * scale;
		return (int) ret;
	}

	public static Point getScreenSize(Context context) {
		// Point ret = new Point();
		// WindowManager wm =
		// (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		// Display d = wm.getDefaultDisplay();
		// d.getSize(ret);
		// return ret;
		Point ret = new Point();
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		int w = metrics.widthPixels;
		int h = metrics.heightPixels;
		ret.set(w, h);
		return ret;
	}

	public static int getScreenWidth(Context context) {
		DisplayMetrics metrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(metrics);
		int w = metrics.widthPixels;
		return w;
	}

	private static DecimalFormat formatter = null;
	private static DecimalFormat formatterFloat = null;

	public static String format(int number) {
		if (number == 0) return "";
		if (formatter == null) {
			formatter = new DecimalFormat("#,###,###");
			DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.US);
			formatter.setDecimalFormatSymbols(sym);
		}
		return formatter.format(number);
	}

	public static String format(float value) {
		String ret = value + "";
		if (value == 0f)
			ret = "";
		return ret;
	}

	public static String formatPrice(float price) {
		if (price == 0) return "";
		if (formatterFloat == null) {
			formatterFloat = new DecimalFormat("#,###,###.##");
			DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.US);
			formatterFloat.setDecimalFormatSymbols(sym);
		}
		return formatterFloat.format(price);
	}

	public static String formatVol(float vol) {
		if (vol == 0) return "";
		if (formatterFloat == null) {
			formatterFloat = new DecimalFormat("#,###,###");
			DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.US);
			formatterFloat.setDecimalFormatSymbols(sym);
		}
		return formatterFloat.format(vol);
	}

	public static String correctNumber(String val) {
		return val.replaceAll(",", "");
	}

	public static boolean isNumber(String value) {
		if (value == null)
			return false;

		if (value.equals("AOT")) {
			Log.d("Utils", "isNumber: " + value);
		}

		boolean ret = false;

		try {
			Float.parseFloat(correctNumber(value));
			ret = true;
		} catch (NumberFormatException ne) {

		}

//		ret = !value.matches("[A-Za-z]");

		return ret;
	}

	public static final String removeAccents(String originalString) {
		if (originalString == null || originalString.length() == 0) return originalString;
		originalString = originalString.replace('Đ', 'D').replace('đ', 'd');
		return Normalizer.normalize(originalString, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
	}

	private static Field ColorDrawableStateField;
	private static Field ColorDrawableUseColorField;

	@SuppressLint("NewApi")
	public static int getBackgroundColor(View view) {
		int color = Color.TRANSPARENT;
		Drawable background = view.getBackground();
		if (background instanceof ColorDrawable) {
			if (Utils.hasOsVersion(11))
				color = ((ColorDrawable) background).getColor();
			else {
				try {
					if (ColorDrawableStateField == null)
						ColorDrawableStateField = ColorDrawable.class.getDeclaredField("mState");
					ColorDrawableStateField.setAccessible(true);
					Object fv = ColorDrawableStateField.get(background);

					if (ColorDrawableUseColorField == null)
						ColorDrawableUseColorField = fv.getClass().getDeclaredField("mUseColor");
					ColorDrawableUseColorField.setAccessible(true);
					color = Integer.parseInt(ColorDrawableUseColorField.get(fv).toString());

				} catch (Exception e) {

				}
			}
		}
		return color;
	}

	public static void openPlayStoreAppDetails(Context context, String packageName) {
		Intent localIntent = new Intent(Intent.ACTION_VIEW);
		localIntent.setFlags(
				Intent.FLAG_ACTIVITY_NEW_TASK |
						Intent.FLAG_ACTIVITY_CLEAR_TOP |
						Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
		);
		localIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));

		try {
			localIntent.setPackage("com.android.vending");
			context.startActivity(localIntent);

		} catch (ActivityNotFoundException e) {
			localIntent.setPackage(null);
			context.startActivity(localIntent);
		}
	}

	public static boolean openBrowser(Context context, String url) {
		boolean opened = openBrowser(context, url, "com.android.chrome");
		if (!opened)
			opened = openBrowser(context, url, "com.android.browser");
		if (!opened)
			opened = openBrowser(context, url, null);
		return opened;
	}

	private static boolean openBrowser(Context context, String url, String pkg) {
		try {
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			if (pkg != null) it.setPackage(pkg);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(it);
			return true;

		} catch (ActivityNotFoundException e) {
			return false;
		}
	}







	public static String getWeekdayFromDate(Context context, String timeString) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.US);
		try {
			Date date = fmt.parse(timeString);

			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
			String dayWeek = "";
			switch (dayOfWeek) {
				case 2: {
					dayWeek = context.getResources().getString(R.string.label_monday);
					break;
				}
				case 3: {
					dayWeek = context.getResources().getString(R.string.label_tuesday);
					break;
				}
				case 4: {
					dayWeek = context.getResources().getString(R.string.label_wednesday);
					break;
				}
				case 5: {
					dayWeek = context.getResources().getString(R.string.label_thursday);
					break;
				}
				case 6: {
					dayWeek = context.getResources().getString(R.string.label_friday);
					break;
				}
				case 7: {
					dayWeek = context.getResources().getString(R.string.label_satusday);
					break;
				}
				case 1: {
					dayWeek = context.getResources().getString(R.string.label_sunday);
					break;
				}
			}


			SimpleDateFormat fmtOut = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

			String dateOut = dayWeek + " " + fmtOut.format(date);
			return dateOut;
		}
		catch(ParseException pe) {

			return timeString;
		}

	}


}