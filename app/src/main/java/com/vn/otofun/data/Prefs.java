package com.vn.otofun.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.vn.otofun.utils.Configs;
import com.vn.otofun.utils.MapHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class Prefs {
	SharedPreferences p;
	Context context;
	
	public Prefs(Context context) {
		p = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
		this.context = context.getApplicationContext();
	}

//    public String getExpireDate() {
//        return p.getString("expireDate", "None");
//    }
//    public void setExpireDate(String expireDate) {
//        p.edit().putString("expireDate", expireDate).apply();
//    }
//
//	public int getActiveDays() {
//		return p.getInt("activeDays", 0);
//	}
//	public void setActiveDays(int activeDays) {
//		p.edit().putInt("activeDays", activeDays).apply();
//	}


	public void setLanguage(Locale lang) {
		Editor e = p.edit();
		e.putString("lang-lang", lang.getLanguage());
		e.putString("lang-country", lang.getCountry());
		e.putString("lang-variant", lang.getVariant());
		e.commit();
	} 
	public Locale getLanguage() {
		final Locale def = Locale.getDefault();
		final String language = p.getString("lang-lang", def.getLanguage());
		final String country = p.getString("lang-country", def.getCountry());
		final String variant = p.getString("lang-variant", def.getVariant());
		Locale l = new Locale(language, country, variant);
		return l;
	}

	public boolean isSetLanguage() {
		final Locale def = Locale.getDefault();
		final String language = p.getString("lang-lang", null);
		final String country = p.getString("lang-country", null);
		final String variant = p.getString("lang-variant", null);
		if (language == null || country == null || variant == null) {
			return false;
		}
		return true;
	}

//	public long getRefreshInterval() {
//		return p.getLong("refreshInterval", 10000);
//	}
//	public void setRefreshInterval(long value) {
//		p.edit().putLong("refreshInterval", value).commit();
//	}
	
//	public int getMaximumStocksPerTable() {
//		return p.getInt("maxStocksPerTbl", 16);
//	}
//	public void setMaximumStocksPerTable(int value) {
//		p.edit().putInt("maxStocksPerTbl", value).commit();
//	}
	
//	public ArrayList<String> getWatchingStocks(String indexId) {
//		String st = getWatchingStockString(indexId);
//		String[] sts = st.split(", ");
//		ArrayList<String> ret = new ArrayList<String>();
//		for (String s : sts) {
//			if (s.trim().length() > 0)
//				ret.add(s);
//		}
//		Collections.sort(ret);
//		return ret;
//	}
//	public void addWatchingStock(String stockId, String indexId) {
//		if (!getWatchingStocks(indexId).contains(stockId)) {
//			String st = getWatchingStockString(indexId);
//			st += ", " + stockId;
//			p.edit().putString("defstocks-"+indexId, st).apply();
//		}
//	}

	//===============================================================================
	public void clearAll() {
		p.edit().clear().apply();
	}

	public void setAllConfig(LinkedTreeMap configMap) {
		String json = new Gson().toJson(configMap);
		p.edit().putString ("all_config_app", json).apply();
	}

	public String getConfigByKey(String key) {
		String jsonString = p.getString ("all_config_app", null);
		if (jsonString == null) {
			return null;
		}
		LinkedTreeMap configMap = new Gson().fromJson(jsonString, LinkedTreeMap.class);
		return MapHelper.getValue(configMap, key, null);
	}
	public boolean setConfigByKey(String key, String value) {
		String jsonString = p.getString ("all_config_app", null);
		if (jsonString == null) {
			return false;
		}
		LinkedTreeMap configMap = new Gson().fromJson(jsonString, LinkedTreeMap.class);
		configMap.put(key, value);
		setAllConfig(configMap);
		return true;
	}

	//===============================================================================
	public int getLastIDOfTopic(String topicID) {
		return p.getInt ("LastTopic_" + topicID, 0);
	}
	public boolean setLastIDOfTopic(String topicID, int value) {
		p.edit().putInt ("LastTopic_" + topicID, value).apply();
		return true;
	}

	public String getLastURL() {
		return p.getString ("last_url_forum", Configs.LINK_HOME_OTOFUN);
	}
	public boolean setLastURL(String url) {
		p.edit().putString ("last_url_forum", url).apply();
		return true;
	}

	//===============================================================================

	//===============================================================================

	//===============================================================================


	//===============================================================================

	//===============================================================================

//	public void setStockBoard(int marketID, LinkedTreeMap stocksMap) {
//		LinkedTreeMap oldStocksMap = getStockBoard(marketID);
//        if (oldStocksMap != null) {
//			oldStocksMap.putAll(stocksMap);
//        }
//        String jsonString = new Gson().toJson(oldStocksMap);
//		p.edit().putString ("stock_board_" + marketID, jsonString).apply();
//	}
//

	public void addStarOfThread(int threadID) {
		ArrayList threadList = new ArrayList();
		String jsonString = p.getString ("list_thread_star", null);
		if (jsonString != null) {
			threadList = new Gson().fromJson(jsonString, ArrayList.class);
		}

		if (!threadList.contains(String.valueOf(threadID))) {
			threadList.add(String.valueOf(threadID));
	        jsonString = new Gson().toJson(threadList);
			p.edit().putString ("list_thread_star", jsonString).apply();
		}
	}

	public void removeStarOfThread(int threadID) {
		ArrayList threadList = new ArrayList();
		String jsonString = p.getString ("list_thread_star", null);
		if (jsonString != null) {
			threadList = new Gson().fromJson(jsonString, ArrayList.class);
		}

		if (threadList.contains(String.valueOf(threadID))) {
			threadList.remove(String.valueOf(threadID));
			jsonString = new Gson().toJson(threadList);
			p.edit().putString ("list_thread_star", jsonString).apply();
		}
	}

	public int getStarOfThread(int threadID) {
		String jsonString = p.getString ("list_thread_star", null);
		if (jsonString == null) {
			return 0;
		}

		ArrayList threadList = new Gson().fromJson(jsonString, ArrayList.class);
		if (threadList.contains(String.valueOf(threadID))) {
			return 1;
		}
		return 0;
	}


}
