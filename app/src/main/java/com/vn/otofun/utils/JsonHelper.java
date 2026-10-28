package com.vn.otofun.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by AnhVT on 7/14/15.
 */
public final class JsonHelper {

    public synchronized static int parse(JSONObject json, String key, int defaultValue) {
        int result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof Integer) {
                    result = json.getInt(key);
                } else if (value instanceof String) {
                    result = Integer.parseInt(json.getString(key).trim().replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static String parse(JSONObject json, String key, String defaultValue) {
        String result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof String) {
                    result = json.getString(key);
                } else if (value instanceof Integer) {
                    result = String.valueOf(json.getInt(key));
                } else if (value instanceof Long) {
                    result = String.valueOf(json.getLong(key));
                } else if (value instanceof Double) {
                    result = String.valueOf(json.getDouble(key));
                } else if (value instanceof Boolean) {
                    result = String.valueOf(json.getBoolean(key));
                } else {
                    result = value.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static boolean parse(JSONObject json, String key, boolean defaultValue) {
        boolean result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof Boolean) {
                    result = json.getBoolean(key);
                } else if (value instanceof Integer) {
                    result = (json.getInt(key) != 0);
                } else if (value instanceof Long) {
                    result = (json.getLong(key) != 0l);
                } else if (value instanceof Double) {
                    result = (json.getDouble(key) != 0.0d);
                } else if (value instanceof String) {
                    if (isNumeric(json.getString(key).trim().replaceAll("\\s+", ""))) {
                        result = (parse(json, key, 0l) != 0l);
                    } else {
                        result = Boolean.valueOf(json.getString(key));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static long parse(JSONObject json, String key, long defaultValue) {
        long result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof Integer) {
                    result = json.getInt(key);
                } else if (value instanceof Long) {
                    result = json.getLong(key);
                } else if (value instanceof String) {
                    result = Long.parseLong(json.getString(key).trim().replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static double parse(JSONObject json, String key, double defaultValue) {
        double result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof  Integer) {
                    result = json.getInt(key);
                } else if (value instanceof Long) {
                    result = json.getLong(key);
                } else if (value instanceof Double) {
                    result = json.getDouble(key);
                } else if (value instanceof String) {
                    result = Double.parseDouble(json.getString(key).trim().replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static JSONObject parse2JO(JSONObject json, String key, JSONObject defaultValue) {
        JSONObject result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof JSONObject) {
                    result = json.getJSONObject(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static JSONArray parse2JA(JSONObject json, String key, JSONArray defaultValue) {
        JSONArray result = defaultValue;

        if (!json.isNull(key)) {
            try {
                Object value = json.get(key);
                if (value instanceof JSONArray) {
                    result = json.getJSONArray(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static ArrayList<HashMap> parse2A(JSONObject json, String key) {
        try {
            Object value = json.get(key);
            if (value instanceof JSONArray) {
                return parse2A(json.getJSONArray(key));
            } else if (value instanceof JSONObject) {
                return parse2A(json.getJSONObject(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private synchronized static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public synchronized static ArrayList<HashMap> parse2A(JSONArray arrays) throws JSONException {
        ArrayList<HashMap> maps = new ArrayList<HashMap>();
        for (int i = 0; i < arrays.length(); i++) {
            JSONObject json = arrays.getJSONObject(i);
            maps.add(parse2H(json));
        }
        return maps;
    }

    public synchronized static ArrayList<HashMap> parse2A(JSONObject object) throws JSONException {
        ArrayList<HashMap> maps = new ArrayList<>();
        Iterator keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            JSONObject json = (JSONObject) object.get(key);
            maps.add(parse2H(json));
        }
        return maps;
    }

    public synchronized static HashMap parse2H(JSONObject json) throws JSONException {
        HashMap map = new HashMap();
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, json.get(key));
        }
        return map;
    }
}
