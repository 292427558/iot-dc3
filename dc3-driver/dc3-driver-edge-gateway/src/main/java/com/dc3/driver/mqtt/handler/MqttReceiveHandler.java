/*
 * Copyright 2016-2021 Pnoker. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dc3.driver.mqtt.handler;

import com.alibaba.fastjson.JSON;
import com.dc3.common.sdk.bean.mqtt.MessageHeader;
import com.dc3.driver.mqtt.bean.MqttPayload;
import com.dc3.driver.mqtt.service.MqttReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.MessageHandler;

import javax.annotation.Resource;

/**
 * @author pnoker
 */
@Slf4j
@Configuration
public class MqttReceiveHandler {

    @Resource
    private MqttReceiveService receiveService;

    @Bean
    @ServiceActivator(inputChannel = "mqttValueInputChannel")
    public MessageHandler handlerValue() {
        return message -> {
            MessageHeader messageHeader = new MessageHeader(message.getHeaders());
            MqttPayload payload = JSON.parseObject(message.getPayload().toString(), MqttPayload.class);
        };
    }
}
