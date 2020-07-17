package com.qg.exclusiveplug.util;


import com.google.gson.Gson;
import com.qg.exclusiveplug.dtos.InteractBigData;
import com.qg.exclusiveplug.dtos.RequestData;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service("httpClientUtil")
@Slf4j
public class HttpClientUtil {

    @Autowired
    private static InteractBigData interactBigData;

    /**
     * 与数据挖掘端交互数据
     *
     * @param url         发送的URL
     * @param requestData 传给数据
     * @return
     * @throws IOException
     */
    public static InteractBigData demandedCount(String url, RequestData requestData) throws IOException {
        // 将Json对象转换为字符串
        Gson gson = new Gson();
        String strJson = gson.toJson(requestData, RequestData.class);
        //使用帮助类HttpClients创建CloseableHttpClient对象.
        CloseableHttpClient client = HttpClients.createDefault();
        //HTTP请求类型创建HttpPost实例
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");

        //组织数据
        StringEntity se = new StringEntity(strJson);
        se.setContentType("application/json");

        //对于httpPost请求,把请求体填充进HttpPost实体.
        httpPost.setEntity(se);

        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = client.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                log.info("请求数据挖掘成功");
                strJson = EntityUtils.toString(entity, "UTF-8").trim();
                interactBigData = gson.fromJson(strJson, InteractBigData.class);
                return interactBigData;
            } else {
                log.debug("数据挖掘崩啦");
                log.debug(response.getEntity().toString());
            }
            return null;
        } finally {
            if (response != null) {
                response.close();
            }
            client.close();
        }
    }


}
