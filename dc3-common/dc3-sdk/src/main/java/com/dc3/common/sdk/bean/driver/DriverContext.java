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

package com.dc3.common.sdk.bean.driver;

import com.dc3.common.bean.driver.AttributeInfo;
import com.dc3.common.bean.driver.DriverMetadata;
import com.dc3.common.constant.Common;
import com.dc3.common.exception.NotFoundException;
import com.dc3.common.model.Device;
import com.dc3.common.model.Point;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author pnoker
 */
@Data
@Slf4j
@Component
public class DriverContext {

    /**
     * 驱动 元数据，当且仅当驱动注册成功之后由 Manager 返回
     */
    private DriverMetadata driverMetadata = new DriverMetadata();

    /**
     * 驱动 状态，默认为 未注册 状态
     */
    private String driverStatus = Common.Driver.Status.UNREGISTERED;

    public synchronized void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
    }

    /**
     * 根据 设备Id 获取连接设备的驱动配置信息
     *
     * @param deviceId Device Id
     * @return Map<String, AttributeInfo>
     */
    public Map<String, AttributeInfo> getDriverInfoByDeviceId(Long deviceId) {
        return this.driverMetadata.getDriverInfoMap().get(deviceId);
    }

    /**
     * 根据 设备Id 获取连接设备的全部位号配置信息
     *
     * @param deviceId Device Id
     * @return Map<Long, Map < String, AttributeInfo>>
     */
    public Map<Long, Map<String, AttributeInfo>> getPointInfoByDeviceId(Long deviceId) {
        Map<Long, Map<String, AttributeInfo>> tmpMap = this.driverMetadata.getPointInfoMap().get(deviceId);
        if (null == tmpMap || tmpMap.size() < 1) {
            //todo 提示信息需要统一替换
            throw new NotFoundException("Device(" + deviceId + ") does not exist");
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 和 位号Id 获取连接设备的位号配置信息
     *
     * @param deviceId Device Id
     * @param pointId  Point Id
     * @return Map<String, AttributeInfo>
     */
    public Map<String, AttributeInfo> getPointInfoByDeviceIdAndPointId(Long deviceId, Long pointId) {
        Map<String, AttributeInfo> tmpMap = getPointInfoByDeviceId(deviceId).get(pointId);
        if (null == tmpMap || tmpMap.size() < 1) {
            throw new NotFoundException("Point(" + pointId + ") info does not exist");
        }
        return tmpMap;
    }

    /**
     * 根据 设备Id 获取设备
     *
     * @param deviceId Device Id
     * @return Device
     */
    public Device getDeviceByDeviceId(Long deviceId) {
        Device device = this.driverMetadata.getDeviceMap().get(deviceId);
        if (null == device) {
            throw new NotFoundException("Device(" + deviceId + ") does not exist");
        }
        return device;
    }

    /**
     * 根据 设备Id 获取位号
     *
     * @param deviceId Device Id
     * @return Point Array
     */
    public List<Point> getPointByDeviceId(Long deviceId) {
        Device device = getDeviceByDeviceId(deviceId);
        return this.driverMetadata.getProfilePointMap().entrySet().stream()
                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
                .map(entry -> new ArrayList<>(entry.getValue().values()))
                .reduce(new ArrayList<>(), (total, temp) -> {
                    total.addAll(temp);
                    return total;
                });
    }

    /**
     * 根据 设备Id和位号Id 获取位号
     *
     * @param deviceId Device Id
     * @param pointId  Point Id
     * @return Point
     */
    public Point getPointByDeviceIdAndPointId(Long deviceId, Long pointId) {
        Device device = getDeviceByDeviceId(deviceId);
        Optional<Map<Long, Point>> optional = this.driverMetadata.getProfilePointMap().entrySet().stream()
                .filter(entry -> device.getProfileIds().contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .filter(entry -> entry.containsKey(pointId))
                .findFirst();

        if (optional.isPresent()) {
            return optional.get().get(pointId);
        }

        throw new NotFoundException("Point(" + pointId + ") point does not exist");
    }

}
