package com.vn.otofun.utils;

import java.util.Map;

/**
 * Created by AnhVT on 7/14/15.
 */
public final class MapHelper {

    public synchronized static int getValue(Map map, String key, int defaultValue) {
        int result = defaultValue;

        if (map.containsKey(key)) {
            try {
                Object value = map.get(key);
                if (value instanceof Integer) {
                    result = (int) value;
                } else if (value instanceof String) {
                    result = Integer.parseInt(((String) value).trim().replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static String getValue(Map map, String key, String defaultValue) {
        String result = defaultValue;

        if (map.containsKey(key)) {
            try {
                Object value = map.get(key);
                if (value instanceof String) {
                    result = (String) value;
                } else if (value instanceof Integer) {
                    result = String.valueOf((int) value);
                } else if (value instanceof Long) {
                    result = String.valueOf((long) value);
                } else if (value instanceof Double) {
                    result = String.valueOf((double) value);
                } else if (value instanceof Boolean) {
                    result = String.valueOf((boolean) value);
                } else {
                    result = value.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static boolean getValue(Map map, String key, boolean defaultBoolean) {
        boolean result = defaultBoolean;

        if (map.containsKey(key)) {
            try {
                Object value = map.get(key);
                if (value instanceof Boolean) {
                    result = (boolean) value;
                } else if (value instanceof Integer) {
                    result = ((int) value != 0);
                } else if (value instanceof Long) {
                    result = ((long) value != 0);
                } else if (value instanceof Double) {
                    result = ((double) value != 0);
                } else if (value instanceof String) {
                    if (isNumeric(((String) value).trim().replaceAll("\\s+", ""))) {
                        result = (getValue(map, key, 0) != 0);
                    } else {
                        result = Boolean.valueOf((String) value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static long getValue(Map map, String key, long defaultValue) {
        long result = defaultValue;

        if (map.containsKey(key)) {
            try {
                Object value = map.get(key);
                if (value instanceof Integer) {
                    result = (int) value;
                } else if (value instanceof Long) {
                    result = (long) value;
                } else if (value instanceof String) {
                    result = Long.parseLong(((String) map.get(key)).trim().replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static double getValue(Map map, String key, double defaultValue) {
        double result = defaultValue;

        if (map.containsKey(key)) {
            try {
                Object value = map.get(key);
                if (value instanceof Integer) {
                    result = (int) value;
                } else if (value instanceof Long) {
                    result = (long) value;
                } else if (value instanceof Double) {
                    result = (double) value;
                } else if (value instanceof String) {
                    result = Double.parseDouble(((String) map.get(key)).trim().replace(",", ".").replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public synchronized static float getValue(Map map, String key, float defaultValue) {
        float result = defaultValue;

        if (map.containsKey(key)) {
            try {
                Object value = map.get(key);
                if (value instanceof Integer) {
                    result = (int) value;
                } else if (value instanceof Long) {
                    result = (long) value;
                } else if (value instanceof Float) {
                    result = (float) value;
                } else if (value instanceof String) {
                    result = Float.parseFloat(((String) map.get(key)).trim().replace(",", ".").replaceAll("\\s+", ""));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private synchronized static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
