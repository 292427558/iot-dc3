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

package com.dc3.api.center.manager.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc3.api.center.manager.hystrix.PointClientHystrix;
import com.dc3.common.bean.R;
import com.dc3.common.constant.Common;
import com.dc3.common.dto.PointDto;
import com.dc3.common.model.Point;
import com.dc3.common.valid.Insert;
import com.dc3.common.valid.Update;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 位号 FeignClient
 *
 * @author pnoker
 */
@FeignClient(path = Common.Service.DC3_MANAGER_POINT_URL_PREFIX, name = Common.Service.DC3_MANAGER_SERVICE_NAME, fallbackFactory = PointClientHystrix.class)
public interface PointClient {

    /**
     * 新增 Point
     *
     * @param point Point
     * @return Point
     */
    @PostMapping("/add")
    R<Point> add(@Validated(Insert.class) @RequestBody Point point, @RequestHeader(value = Common.Service.DC3_AUTH_TENANT_ID, defaultValue = "-1") Long tenantId);

    /**
     * 根据 ID 删除 Point
     *
     * @param id Point Id
     * @return Boolean
     */
    @PostMapping("/delete/{id}")
    R<Boolean> delete(@NotNull @PathVariable(value = "id") Long id);

    /**
     * 修改 Point
     *
     * @param point Point
     * @return Point
     */
    @PostMapping("/update")
    R<Point> update(@Validated(Update.class) @RequestBody Point point, @RequestHeader(value = Common.Service.DC3_AUTH_TENANT_ID, defaultValue = "-1") Long tenantId);

    /**
     * 根据 ID 查询 Point
     *
     * @param id Point Id
     * @return Point
     */
    @GetMapping("/id/{id}")
    R<Point> selectById(@NotNull @PathVariable(value = "id") Long id);

    /**
     * 根据 设备 ID 查询 Point
     *
     * @param deviceId Device Id
     * @return Point Array
     */
    @GetMapping("/device_id/{deviceId}")
    R<List<Point>> selectByDeviceId(@NotNull @PathVariable(value = "deviceId") Long deviceId);

    /**
     * 根据 模板 ID 查询 Point
     *
     * @param profileId Profile Id
     * @return Point Array
     */
    @GetMapping("/profile_id/{profileId}")
    R<List<Point>> selectByProfileId(@NotNull @PathVariable(value = "profileId") Long profileId);

    /**
     * 分页查询 Point
     *
     * @param pointDto Point Dto
     * @return Page<Point>
     */
    @PostMapping("/list")
    R<Page<Point>> list(@RequestBody(required = false) PointDto pointDto, @RequestHeader(value = Common.Service.DC3_AUTH_TENANT_ID, defaultValue = "-1") Long tenantId);

    /**
     * 查询 位号单位
     *
     * @param pointIds Point Id Set
     * @return Map<Long, String>
     */
    @PostMapping("/unit")
    R<Map<Long, String>> unit(@RequestBody(required = false) Set<Long> pointIds);
}
