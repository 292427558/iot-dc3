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

package com.dc3.center.manager.service;

import com.dc3.common.base.Service;
import com.dc3.common.dto.DriverInfoDto;
import com.dc3.common.model.DriverInfo;

import java.util.List;

/**
 * DriverInfo Interface
 *
 * @author pnoker
 */
public interface DriverInfoService extends Service<DriverInfo, DriverInfoDto> {

    /**
     * 根据驱动属性配置 ID 和 设备 ID 查询
     *
     * @param driverAttributeId Driver Attribute Id
     * @param deviceId          Device Id
     * @return DriverInfo
     */
    DriverInfo selectByAttributeIdAndDeviceId(Long driverAttributeId, Long deviceId);

    /**
     * 根据驱动属性配置 ID 查询
     *
     * @param driverAttributeId Driver Attribute Id
     * @return DriverInfo Array
     */
    List<DriverInfo> selectByAttributeId(Long driverAttributeId);

    /**
     * 根据设备 ID 查询
     *
     * @param deviceId Device Id
     * @return DriverInfo Array
     */
    List<DriverInfo> selectByDeviceId(Long deviceId);
}
