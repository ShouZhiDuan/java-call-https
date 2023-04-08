package me.dreamingodd.ca;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;


/**
 * #1
 * HTTPS 双向认证 - direct into cacerts
 * @Author Ye_Wenda
 * @Date 7/11/2017
 */
public class HttpsKeyStoreDemo {
    private final static String PFX_PATH = "C:\\Development\\deployment\\ssl\\ca-demo\\client.p12";
    private final static String PFX_PWD = "demo_pwd";

    public static String sslRequestGet(String url) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        InputStream instream = new FileInputStream(new File(PFX_PATH));
        try {
            keyStore.load(instream, PFX_PWD.toCharArray());
        } finally {
            instream.close();
        }

        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, PFX_PWD.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext
                , new String[] { "TLSv1" }
                , null
                , SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        try {
            HttpGet httpget = new HttpGet(url);
//          httpost.addHeader("Connection", "keep-alive");// 设置一些heander等
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                // 返回结果
                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(sslRequestGet("https://ssl.demo.com/"));
    }

}
