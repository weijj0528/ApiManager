package cn.crap.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class HttpTest {
    public static JSONObject demoTest(String url, JSONObject biz_param, String debugIsLogin) throws Exception {
        long time = System.currentTimeMillis();
        String substring = url.substring(url.indexOf("?"));
        String[] param = substring.replace("?", "").split("&");
        JSONObject json = new JSONObject();
        json.put("module", param[0]);
        json.put("method", param[1]);
        json.put("version", "1.0.0");
        String sign = "";
        JSONArray array = JSONArray.fromObject(debugIsLogin);
        JSONObject biz_paramLogin = new JSONObject();
        for (Object object : array) {
            biz_paramLogin.putAll(JSONObject.fromObject(object));
        }
        if (biz_paramLogin.optInt("isLogin") == 1) {
            biz_paramLogin.remove("isLogin");
            biz_paramLogin.put("model", "CrapApi_API");
            biz_paramLogin.put("deviceId", "6666666666");
            int loginType = biz_paramLogin.optInt("loginType", 0);
            String loginUrl = loginType == 0 ? "/user/phoneLogin" : "";
            json.put("param", biz_paramLogin);
            JSONObject httpPostJson = HttpClientUtils.httpPostJson(url.replace("/handle/request", loginUrl), json);
            JSONObject result = httpPostJson.optJSONObject("result");
            if (httpPostJson.optInt("status") != 200 || !result.optString("code").equals("1c01")) {
                return httpPostJson;
            }
            url = url.replace(substring, "") + ";jsessionid=" + result.getJSONObject("biz_result").getString("SID");
            sign = SecretUtils.HmacSHA1Encrypt("biz_module=" + param[0] + "&biz_method=" + param[1] + "&time=" + time,
                    result.getJSONObject("biz_result").getString("KEY"));
        }
        json.put("time", time);
        json.put("sign", sign);
        json.put("param", biz_param);
        JSONObject result = HttpClientUtils.httpPostJson(url, json);
        return result;
    }

    public static JSONObject demoTestLogin(String url, JSONObject biz_param) throws Exception {
        long time = System.currentTimeMillis();
        JSONObject json = new JSONObject();
        json.put("module", "");
        json.put("method", "");
        json.put("version", "1.0.0");
        json.put("time", time);
        json.put("sign", "");
        json.put("param", biz_param);
        JSONObject result = HttpClientUtils.httpPostJson(url, json);
        return result;
    }
}
