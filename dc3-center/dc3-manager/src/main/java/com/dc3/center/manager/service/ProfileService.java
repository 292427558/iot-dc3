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
import com.dc3.common.dto.ProfileDto;
import com.dc3.common.model.Profile;

import java.util.List;
import java.util.Set;

/**
 * Profile Interface
 *
 * @author pnoker
 */
public interface ProfileService extends Service<Profile, ProfileDto> {

    /**
     * 根据 模板Name 查询模版
     *
     * @param name Profile Name
     * @param type Profile Type
     * @return Profile
     */
    Profile selectByNameAndType(String name, Short type, Long tenantId);

    /**
     * 根据 模版Id集 查询模版
     *
     * @param ids Profile Id Set
     * @return Profile Array
     */
    List<Profile> selectByIds(Set<Long> ids);

    /**
     * 根据 设备Id 查询模版
     *
     * @param deviceId Device Id
     * @return Profile Array
     */
    List<Profile> selectByDeviceId(Long deviceId);

}
