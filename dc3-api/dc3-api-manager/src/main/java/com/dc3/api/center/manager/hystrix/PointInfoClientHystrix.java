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

package com.dc3.api.center.manager.hystrix;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc3.api.center.manager.feign.PointInfoClient;
import com.dc3.common.bean.R;
import com.dc3.common.dto.PointInfoDto;
import com.dc3.common.model.PointInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PointInfoClientHystrix
 *
 * @author pnoker
 */
@Slf4j
@Component
public class PointInfoClientHystrix implements FallbackFactory<PointInfoClient> {

    @Override
    public PointInfoClient create(Throwable throwable) {
        String message = throwable.getMessage() == null ? "No available server for client: DC3-MANAGER" : throwable.getMessage();
        log.error("Hystrix:{}", message);

        return new PointInfoClient() {

            @Override
            public R<PointInfo> add(PointInfo pointInfo) {
                return R.fail(message);
            }

            @Override
            public R<Boolean> delete(Long id) {
                return R.fail(message);
            }

            @Override
            public R<PointInfo> update(PointInfo pointInfo) {
                return R.fail(message);
            }

            @Override
            public R<PointInfo> selectById(Long id) {
                return R.fail(message);
            }

            @Override
            public R<PointInfo> selectByAttributeIdAndDeviceIdAndPointId(Long attributeId, Long deviceId, Long pointId) {
                return R.fail(message);
            }

            @Override
            public R<List<PointInfo>> selectByDeviceIdAndPointId(Long deviceId, Long pointId) {
                return R.fail(message);
            }

            @Override
            public R<List<PointInfo>> selectByDeviceId(Long deviceId) {
                return R.fail(message);
            }

            @Override
            public R<Page<PointInfo>> list(PointInfoDto pointInfoDto) {
                return R.fail(message);
            }

        };
    }
}