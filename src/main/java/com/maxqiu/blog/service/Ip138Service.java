package com.maxqiu.blog.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.maxqiu.blog.pojo.dto.Ip138InfoDTO;
import com.maxqiu.blog.properties.Ip138Properties;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * ip138 服务
 *
 * @author Max_Qiu
 */
@Service
@Slf4j
public class Ip138Service {
    @Resource
    private Ip138Properties ip138Properties;

    /**
     * 查询IP信息
     *
     * @param strIp
     *            IP字符串
     */
    public Ip138InfoDTO query(String strIp) {
        if (!ip138Properties.getEnable()) {
            return null;
        }
        String urlString = "https://api.ipshudi.com/ip/?ip=" + strIp + "&datatype=jsonp";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("token", ip138Properties.getToken());
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                StringBuilder builder = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
                for (String s = br.readLine(); s != null; s = br.readLine()) {
                    builder.append(s);
                }
                br.close();
                // {"ret":"ok","ip":"203.208.60.61","data":["中国","北京","北京","","谷歌云","100000","010"]}
                String s = builder.toString();
                JSONObject jsonObject = JSONObject.parseObject(s);
                if (jsonObject == null) {
                    return null;
                }
                Ip138InfoDTO dto = new Ip138InfoDTO();
                dto.setStr(jsonObject.getString("ip"));
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (StringUtils.hasText(jsonArray.getString(0))) {
                    dto.setCountry(jsonArray.getString(0));
                }
                if (StringUtils.hasText(jsonArray.getString(1))) {
                    dto.setProvince(jsonArray.getString(1));
                }
                if (StringUtils.hasText(jsonArray.getString(2))) {
                    dto.setCity(jsonArray.getString(2));
                }
                if (StringUtils.hasText(jsonArray.getString(3))) {
                    dto.setCounty(jsonArray.getString(3));
                }
                if (StringUtils.hasText(jsonArray.getString(4))) {
                    dto.setOperator(jsonArray.getString(4));
                }
                if (StringUtils.hasText(jsonArray.getString(5))) {
                    dto.setPostalCode(jsonArray.getString(5));
                }
                if (StringUtils.hasText(jsonArray.getString(6))) {
                    dto.setAreaCode(jsonArray.getString(6));
                }
                return dto;
            }
        } catch (IOException e) {
            log.error("IP138数据解析失败", e);
        }
        return null;
    }
}
