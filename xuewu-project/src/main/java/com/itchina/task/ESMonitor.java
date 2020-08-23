package com.itchina.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: xiadongming
 * @Date: 2020/8/23 17:27
 */
@Component
public class ESMonitor {
    private static final String HEALTH_CHECK_API = "http://127.0.0.1:9200/_cluster/health";
    private static final String GREEN = "green";
    private static final String YELLOW = "yellow";
    private static final String RED = "red";
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(fixedDelay = 3 * 1000)//3秒钟，检查一次
    public void healthCheck() {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(HEALTH_CHECK_API);
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
                System.out.println("====获取不到es的运行情况===");
            } else {
                String body = EntityUtils.toString(response.getEntity(), "UTF-8");
                JsonNode result = objectMapper.readTree(body);
                String status = result.get("status").asText();
                String message = "";
                boolean isNormal = false;
                switch (status) {
                    case GREEN:
                        message = "es为绿色，运行正常";
                        isNormal = true;
                        break;
                    case YELLOW:
                        message = "es为黄色，亚健康状态，请尽快处理";
                        break;
                    case RED:
                        message = "es为红色，必须马上处理";
                        break;
                    default:
                        message = "es状态未知，必须马上处理";
                        break;
                }
                System.out.println(message);
                //以下是发送邮件
                if (!isNormal) {
                    sendAlertMessage(message);
                }
                // 获取集群节点
                int totalNodes = result.get("number_of_nodes").asInt();
                if (totalNodes < 5) {
                    sendAlertMessage("我们的瓦力节点丢了！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendAlertMessage(String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("dongmingxia2016@163.com");
        mailMessage.setTo("dongmingxia2016@163.com");
        mailMessage.setSubject("【警告】ES服务监控");
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}
