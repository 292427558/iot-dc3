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

package com.dc3.driver.mqtt.bean;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author pnoker
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class MqttPayload {
    private DataType dataType = DataType.DEFAULT;
    private String data;

    public MqttPayload(DataType dataType, Object target) {
        this.dataType = dataType;
        this.data = JSON.toJSONString(target);
    }

    @NoArgsConstructor
    public enum DataType {
        OPC_UA, OPC_DA, MODBUS, PLC, SERIAL, SOCKET, HEARTBEAT, DEFAULT
    }
}
