package com.freeme.discovery.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import com.freeme.discovery.base.Header;

import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;



public class NetworkUtils {
    public static final boolean DEBUG_VIDEO = true;
    public static final String TAG = NetworkUtils.class.getSimpleName();

    public static final String SERVER_URL = "http://service-freemeoscamera.yy845.com:2610";

    public static final String TEST_SERVER_URL = "http://101.95.97.178:2610";
    public static final String TEXT_SERVER_NEWS_URL = "http://192.168.0.52:27111";

    public static final String TEST_APPS_SERVER_URL="http://192.168.0.52:2615";

    public static final String FOREIGN_SERVER_URL="http://spb.dd351.com:2610";

    // for video card
    public final static String ONLINE_RECOMMENDATION = "http://partner.vip.qiyi.com/openapi/home_page?ua=UA&version=4.9.3&os=6.0&id=IMEI&key=KEY&mac_md5=MAC&openudid=6d6cca&platform=PLATFORM";
    public final static String ONLINE_VIDEO_KEYWORDS = "http://partner.vip.qiyi.com/openapi/hotkey?ua=UA&version=4.9.3&os=6.0&id=IMEI&key=KEY&mac_md5=MAC&openudid=6d6cca&platform=PLATFORM";

    public final static String KEY = "1041620266965ce4d0a351a621db3e3a";
    public final static String PLATFORM = "GPhone_trd_tyd";
    public final static int SECURITY_KEY_1 = 1771174171;
    public final static String SECURITY_KEY_2 = "gvDdRdsNRRi";

    public static final String ENCODE_DECODE_KEY = "x_s0_s22";
    public static final String WIDGET_VERSION = "0";
    public static final String PROJECT_NAME = "koobee";
    public static final String CUSTOMER_NAME = "100";

    public static String sVideoUrl = null;
    public static String sVideoKeywordsUrl = null;

    public static String getOnlineHotVideoInfo(Context context) {
        /*String imei = Utils.getImei(context);
        String mac = Utils.getWifiMacAddr(context);
        if (TextUtils.isEmpty(sVideoUrl)) {
            sVideoUrl = buildOnlineRecommendationUrl(Utils.DEFAULT_UA_ID, imei, mac);
        }*/
        return getOnlineVideoInfo(context, sVideoUrl);
    }

    public static String getOnlineHotVideoKeyWordsInfo(Context context) {
      /*  String imei = Utils.getImei(context);
        String mac = Utils.getWifiMacAddr(context);
        if (TextUtils.isEmpty(sVideoKeywordsUrl)) {
            sVideoKeywordsUrl = buildOnlineKeyWordsUrl(Utils.DEFAULT_UA_ID, imei, mac);
        }*/
        return getOnlineVideoInfo(context, sVideoKeywordsUrl);
    }

    public static String getOnlineVideoInfo(Context context, String urlStr) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("t", ((System.currentTimeMillis() / 1000) ^ SECURITY_KEY_1) + "");

            connection.setRequestProperty("sign",
                    MD5.Md5(System.currentTimeMillis() / 1000 + SECURITY_KEY_2 + KEY + "4.9.3"));

            InputStream is = connection.getInputStream();
            String result = InputStreamTOString(is, "UTF-8");

            if (DEBUG_VIDEO) {
                Log.v(TAG, "url:" + url.toString());
                Log.v(TAG, "t:" + ((System.currentTimeMillis() / 1000) ^ 1771174171));
                Log.v(TAG,
                        "sign:"
                                + MD5.Md5(System.currentTimeMillis() / 1000 + "gvDdRdsNRRi"
                                + "1041620266965ce4d0a351a621db3e3a" + "4.9.3"));
                String[] strings = result.split(",");
                for (int i = 0; i < strings.length; i++) {
                    Log.v(TAG, strings[i]);
                }
            }

            return result;
        } catch (IOException e) {
            Log.v(TAG, "e:" + e.toString());
        } catch (Exception e) {
            Log.v(TAG, "e:" + e.toString());
        }
        return null;
    }

    public static String InputStreamTOString(InputStream in, String encoding) throws Exception {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[128];
        int count = -1;
        while ((count = in.read(data, 0, 128)) != -1) {
            outStream.write(data, 0, count);
        }

        data = null;
        return new String(outStream.toByteArray(), encoding);
    }

    public static String buildOnlineRecommendationUrl(String ua, String imei, String mac) {
        mac = mac.replaceAll(":", "");
        mac = mac.toUpperCase();
        mac = MD5.Md5(mac);
        String url = ONLINE_RECOMMENDATION.replaceFirst("UA", ua).replaceFirst("IMEI", imei).replaceFirst("MAC", mac)
                .replaceFirst("KEY", KEY).replaceFirst("PLATFORM", PLATFORM);
        return url;
    }

    public static String buildOnlineKeyWordsUrl(String ua, String imei, String mac) {
        mac = mac.replaceAll(":", "");
        mac = mac.toUpperCase();
        mac = MD5.Md5(mac);
        String url = ONLINE_VIDEO_KEYWORDS.replaceFirst("UA", ua).replaceFirst("IMEI", imei).replaceFirst("MAC", mac)
                .replaceFirst("KEY", KEY).replaceFirst("PLATFORM", PLATFORM);
        return url;
    }

    public static String getStringFromServer(int messageCode) {
        String result = null;
        /*try {
            JSONObject paraInfo = new JSONObject();

            paraInfo.put("mf", DownloadUtils.getProjectName());
            paraInfo.put("ver", WIDGET_VERSION);
            paraInfo.put("lcd", DownloadUtils.getDisplaySize());
            paraInfo.put("customer", DownloadUtils.getCustomerName());

            JSONObject jsObject = new JSONObject();
            jsObject.put("head", NetworkUtils.buildHeadData(messageCode));
            jsObject.put("body", paraInfo.toString());

            String contents = jsObject.toString();

            result = NetworkUtils.accessNetworkByPost(FOREIGN_SERVER_URL, contents);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return result;
    }

    public static String getHotWordStringFromServer(int messageCode) {
        String result = null;
        /*try {
            JSONObject paraInfo = new JSONObject();

            paraInfo.put("mf", DownloadUtils.getProjectName());
            paraInfo.put("ver", WIDGET_VERSION);
//            paraInfo.put("lcd", DownloadUtils.getDisplaySize());
            paraInfo.put("customer", DownloadUtils.getCustomerName());
            paraInfo.put("language", 2);

            JSONObject jsObject = new JSONObject();
            jsObject.put("head", NetworkUtils.buildHeadData(messageCode));
            jsObject.put("body", paraInfo.toString());

            String contents = jsObject.toString();


            result = NetworkUtils.accessNetworkByPost(FOREIGN_SERVER_URL, contents);


        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return result;
    }

    public static String getAppsStringFromServer(int messageCode){
        String result = null;
        try {
            JSONObject paraInfo = new JSONObject();
            JSONObject jsObject = new JSONObject();
            jsObject.put("head", NetworkUtils.buildHeadData(messageCode));
            jsObject.put("body", paraInfo.toString());

            String contents = jsObject.toString();

            result = NetworkUtils.accessNetworkByPost(FOREIGN_SERVER_URL, contents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int getadWinNoticeUrl(Context context,String url){
        /*HttpResponse httpResponse = null;
        HttpGet httpGet = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        httpClient.getParams().setParameter(
                CoreConnectionPNames.SO_TIMEOUT, 5000);
        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                return 200;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return 0;
    }



    public static String accessNetworkByPost(String urlString, String contents) throws IOException {
        String line = null;
        DataOutputStream out = null;
        URL postUrl;

        BufferedInputStream bis = null;
        ByteArrayBuffer baf = null;
        boolean isPress = false;
        HttpURLConnection connection = null;

        try {
            byte[] encrypted = DESUtil.encrypt(contents.getBytes("utf-8"), ENCODE_DECODE_KEY.getBytes());

            postUrl = new URL(urlString);
            connection = (HttpURLConnection) postUrl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(20000);
            connection.setRequestMethod("POST");
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("contentType", "utf-8");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length", "" + encrypted.length);

            out = new DataOutputStream(connection.getOutputStream());
            out.write(encrypted);
            out.flush();
            out.close();

            bis = new BufferedInputStream(connection.getInputStream());
            baf = new ByteArrayBuffer(1024);

            isPress = Boolean.valueOf(connection.getHeaderField("isPress"));

            int current = 0;

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }

            if (baf.length() > 0) {
                byte unCompressByte[];
                byte[] decrypted;
                if (isPress) {
                    decrypted = DESUtil.decrypt(baf.toByteArray(), ENCODE_DECODE_KEY.getBytes());
                    unCompressByte = ZipUtil.uncompress(decrypted);
                    line = new String(unCompressByte);
                } else {
                    decrypted = DESUtil.decrypt(baf.toByteArray(), ENCODE_DECODE_KEY.getBytes());
                    line = new String(decrypted);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
            if (bis != null)
                bis.close();
            if (baf != null)
                baf.clear();
        }
        return line != null ? line.trim() : null;
    }

    public static String buildHeadData(int msgCode) {
        String result = "";

        UUID uuid = UUID.randomUUID();
        Header header = new Header();
        header.setBasicVer((byte) 1);
        header.setLength(84);
        header.setType((byte) 1);
        header.setReserved((short) 0);
        header.setFirstTransaction(uuid.getMostSignificantBits());
        header.setSecondTransaction(uuid.getLeastSignificantBits());
        header.setMessageCode(msgCode);
        result = header.toString();

        return result;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public final static int getNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectMgr.getActiveNetworkInfo();

        if (info != null) {
            if (info.isAvailable()) {
                return info.getType();
            } else {
                return -1;
            }
        }
        return -1;
    }

    public static boolean isWifiEnabled(Context context) {
        /*if (context != null) {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
            return mWifiManager.isWifiEnabled() && ipAddress != 0;
        }*/
        return false;
    }

    public static class MD5 {
        public static String Md5(String str) {
            if (str != null && !str.equals("")) {
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
                    byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < md5Byte.length; i++) {
                        sb.append(HEX[(md5Byte[i] & 0xff) / 16]);
                        sb.append(HEX[(md5Byte[i] & 0xff) % 16]);
                    }
                    str = sb.toString();
                } catch (NoSuchAlgorithmException e) {
                } catch (Exception e) {
                }
            }
            return str;
        }

    }
    public static String getCountryLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh")) {
            return "CN";
        } else if (language.endsWith("hi")) {
            return "IN";
        } else if (language.endsWith("ur")) {
            return "PK";
        } else if (language.endsWith("tl")) {
            return "PH";
        } else if (language.endsWith("th")) {
            return "TH";
        } else if (language.endsWith("ms")) {
            return "MY";
        } else if (language.endsWith("bn")) {
            return "BD";
        } else if (language.endsWith("ru")) {
            return "RU";
        } else if (language.endsWith("tr")) {
            return "TR";
        } else if (language.endsWith("es")) {
            return "ES";
        } else if (language.endsWith("en")) {
            return "US";
        } else if (language.endsWith("ar")) {
            return "SA";
        } else if (language.endsWith("bg")) {
            return "BG";
        } else if (language.endsWith("cs")) {
            return "CZ";
        } else if (language.endsWith("de")) {
            return "DE";
        } else if (language.endsWith("fr")) {
            return "FR";
        } else if (language.endsWith("hr")) {
            return "HR";
        } else if (language.endsWith("hu")) {
            return "HU";
        } else if (language.endsWith("it")) {
            return "IT";
        } else if (language.endsWith("kk")) {
            return "KZ";
        } else if (language.endsWith("pl")) {
            return "PL";
        } else if (language.endsWith("pt")) {
            return "PT";
        } else if (language.endsWith("ro")) {
            return "RO";
        } else if (language.endsWith("sk")) {
            return "SK";
        } else if (language.endsWith("uk")) {
            return "UA";
        } else if (language.endsWith("vi")) {
            return "VN";
        }
        return "US";
    }

    public static String getNewsStringFromServer(Context context, int messageCodeNews, int page) {
        String result = null;

        /*try {
            JSONObject paraInfo = new JSONObject();
            paraInfo.put("channel", DownloadUtils.getProjectName());
            paraInfo.put("customer", DownloadUtils.getCustomerName());
            Log.i("news", "getCountryLanguage(context) = "+getCountryLanguage(context));
            paraInfo.put("countryCode", getCountryLanguage(context));
            paraInfo.put("page", page);
            paraInfo.put("size", 12);
            JSONObject jsObject = new JSONObject();
            jsObject.put("head", NetworkUtils.buildHeadData(messageCodeNews));
            jsObject.put("body", paraInfo.toString());

            String contents = jsObject.toString();
            Log.i("serverString", "contents = "+contents);
            result = NetworkUtils.accessNetworkByPost(FOREIGN_SERVER_URL, contents);
            Log.i("serverString", "result = "+result);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return result;
    }
}
