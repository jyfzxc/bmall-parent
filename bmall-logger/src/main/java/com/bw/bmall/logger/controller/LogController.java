package com.bw.bmall.logger.controller;


import com.alibaba.fastjson.JSONObject;
import com.bw.bmall.common.constant.BmallConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Slf4j
public class LogController {
    @Autowired(required = false)
    private KafkaTemplate<String,String> kafkaTemplate;
    @PostMapping("log")
    @ResponseBody
    public String log(@RequestParam("logString") String logString){
        //我们要在之前日志的格式上再加一个时间字段
        JSONObject jsonObject=JSONObject.parseObject(logString);
        jsonObject.put("ts",System.currentTimeMillis());
        if ("startup".equals(jsonObject.get("type"))){
            kafkaTemplate.send(BmallConstant.KAFKA_STARTUP,jsonObject.toString());
        }else{
            kafkaTemplate.send(BmallConstant.KAFKA_EVENT,jsonObject.toString());
        }
        //log.info(logString);
        return "success";
    }
}
