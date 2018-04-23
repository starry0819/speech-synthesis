package com.zhanghuanfa;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.zhanghuanfa.util.MD5Util;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.List;

/**
 * @author zhanghuanfa
 * @date 2018-04-23 11:16
 */
public class SpeechTest {

    private static final String TTS_URL = "http://api.xfyun.cn/v1/service/v1/tts";
    private static final String TTS_APPID = "5ad98178";
    private static final String TTS_API_KEY = "ad0c4ca9fa07d29595a02716a3e46989";

    public static void main(String[] args) throws Exception {

        httpClientTTS();
    }



    public static void httpClientTTS() throws NoSuchAlgorithmException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(TTS_URL);
        // curTime
        long curTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        // paramBase64
        ParamBean paramBean = new ParamBean();
        paramBean.setAuf("audio/L16;rate=16000");
        paramBean.setAue("raw");
        paramBean.setVoice_name("xiaoyan");
        String param = JSON.toJSONString(paramBean);
        String paramBase64 = Base64.getEncoder().encodeToString(param.getBytes());
        // X-CheckSum
        String origin = (TTS_API_KEY + curTime + paramBase64);
        String checkSum = MD5Util.MD5(origin, "utf-8");
        System.out.println(checkSum);
        System.out.println(Long.toString(curTime));
        post.addHeader("X-CurTime", Long.toString(curTime));
        post.addHeader("X-Param", paramBase64);
        post.addHeader("X-Appid", TTS_APPID);
        post.addHeader("X-CheckSum", checkSum);
        post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=utf-8");
        List<NameValuePair> params = Lists.newArrayList();
        params.add(new BasicNameValuePair("text", "Hello，程序员"));
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
        post.setEntity(urlEncodedFormEntity);
        CloseableHttpResponse response = httpClient.execute(post);
        HttpEntity entity = response.getEntity();
        OutputStream fileOutputStream = null;
        try {
            if (entity != null) {
                System.out.println(entity.getContentType());
                Header[] headers = response.getHeaders("Content-Type");
                System.out.println(headers[0].getValue());
                if (headers[0].getValue().equals("audio/mpeg")) {
                    InputStream content = entity.getContent();
                    int length;
                    byte[] out = new byte[4 * 1024];
                    fileOutputStream = new FileOutputStream(new File("E:\\IdeaProjects\\Workspace\\speech-synthesis\\a.wav"));
                    while ((length = content.read(out)) != -1) {
                        System.out.println("开始写入");
                        fileOutputStream.write(out, 0, length);
                    }
                    System.out.println("写入结束");
                }else {
                    System.out.println(EntityUtils.toString(entity));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    public void urlConnect() throws IOException {
        URL url = new URL(TTS_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    }
}
