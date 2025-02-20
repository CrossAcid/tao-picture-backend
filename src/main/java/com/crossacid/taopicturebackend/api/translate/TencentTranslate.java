package com.crossacid.taopicturebackend.api.translate;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.crossacid.taopicturebackend.config.TencentTranslateConfig;
import com.squareup.okhttp.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Component
public class TencentTranslate {

    private static final OkHttpClient client = new OkHttpClient();

    @Resource
    private TencentTranslateConfig tencentTranslateConfig;

    /**
     * 使用腾讯机器翻译服务讲中文转化为英文
     * @param Zh
     * @return
     */
    public String Zh2Eng(String Zh) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String version = "2018-03-21";
        String action = "TextTranslate";
        String body = "{\"SourceText\":\"" + Zh + "\",\"Source\":\"zh\",\"Target\":\"en\",\"ProjectId\":0}";
        String region = "ap-chengdu";
        String response = doRequest(tencentTranslateConfig.getSecretId(), tencentTranslateConfig.getSecretKey(), version, action, body, region);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        return jsonObject.getJSONObject("Response").getStr("TargetText");
    }

    private static String doRequest(String secretId, String secretKey, String version, String action, String body, String region) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        Request request = buildRequest(secretId, secretKey, version, action, body, region);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static Request buildRequest(
            String secretId, String secretKey,
            String version, String action,
            String body, String region
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        String host = "tmt.tencentcloudapi.com";
        String endpoint = "https://" + host;
        String contentType = "application/json; charset=utf-8";
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String auth = getAuth(secretId, secretKey, host, contentType, timestamp, body);
        return new Request.Builder()
                .header("Host", host)
                .header("X-TC-Timestamp", timestamp)
                .header("X-TC-Version", version)
                .header("X-TC-Action", action)
                .header("X-TC-Region", region)
                .header("X-TC-RequestClient", "SDK_JAVA_BAREBONE")
                .header("Authorization", auth)
                .url(endpoint)
                .post(RequestBody.create(MediaType.parse(contentType), body))
                .build();
    }

    private static String getAuth(
            String secretId, String secretKey, String host, String contentType,
            String timestamp, String body
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:" + contentType + "\nhost:" + host + "\n";
        String signedHeaders = "content-type;host";

        String hashedRequestPayload = sha256Hex(body.getBytes(StandardCharsets.UTF_8));
        String canonicalRequest = "POST"
                + "\n"
                + canonicalUri
                + "\n"
                + canonicalQueryString
                + "\n"
                + canonicalHeaders
                + "\n"
                + signedHeaders
                + "\n"
                + hashedRequestPayload;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String date = sdf.format(new Date(Long.parseLong(timestamp + "000")));
        String service = host.split("\\.")[0];
        String credentialScope = date + "/" + service + "/" + "tc3_request";
        String hashedCanonicalRequest =
                sha256Hex(canonicalRequest.getBytes(StandardCharsets.UTF_8));
        String stringToSign =
                "TC3-HMAC-SHA256\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;

        byte[] secretDate = hmac256(("TC3" + secretKey).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmac256(secretDate, service);
        byte[] secretSigning = hmac256(secretService, "tc3_request");
        String signature =
                printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
        return "TC3-HMAC-SHA256 "
                + "Credential="
                + secretId
                + "/"
                + credentialScope
                + ", "
                + "SignedHeaders="
                + signedHeaders
                + ", "
                + "Signature="
                + signature;
    }

    public static String sha256Hex(byte[] b) throws NoSuchAlgorithmException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(b);
        return printHexBinary(d).toLowerCase();
    }

    private static final char[] hexCode = "0123456789ABCDEF".toCharArray();

    public static String printHexBinary(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public static byte[] hmac256(byte[] key, String msg) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(StandardCharsets.UTF_8));
    }
}
