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

package com.dc3.center.manager.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dc3.center.manager.mapper.DriverMapper;
import com.dc3.center.manager.service.DeviceService;
import com.dc3.center.manager.service.DriverService;
import com.dc3.center.manager.service.ProfileBindService;
import com.dc3.common.bean.Pages;
import com.dc3.common.constant.Common;
import com.dc3.common.dto.DriverDto;
import com.dc3.common.exception.DuplicateException;
import com.dc3.common.exception.NotFoundException;
import com.dc3.common.exception.ServiceException;
import com.dc3.common.model.Device;
import com.dc3.common.model.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DriverService Impl
 *
 * @author pnoker
 */
@Slf4j
@Service
public class DriverServiceImpl implements DriverService {

    @Resource
    private DriverMapper driverMapper;
    @Resource
    private ProfileBindService profileBindService;
    @Resource
    private DeviceService deviceService;

    @Override
    @Caching(
            put = {
                    @CachePut(value = Common.Cache.DRIVER + Common.Cache.ID, key = "#driver.id", condition = "#result!=null"),
                    @CachePut(value = Common.Cache.DRIVER + Common.Cache.SERVICE_NAME, key = "#driver.serviceName", condition = "#result!=null"),
                    @CachePut(value = Common.Cache.DRIVER + Common.Cache.HOST_PORT, key = "#driver.type+'.'+#driver.host+'.'+#driver.port+'.'+#driver.tenantId", condition = "#result!=null")
            },
            evict = {
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.DEVICE_ID, allEntries = true, condition = "#result!=null"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.PROFILE_ID, allEntries = true, condition = "#result!=null"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.DIC, allEntries = true, condition = "#result!=null"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.LIST, allEntries = true, condition = "#result!=null")
            }
    )
    public Driver add(Driver driver) {
        try {
            selectByServiceName(driver.getServiceName());
            throw new DuplicateException("The driver already exists");
        } catch (NotFoundException notFoundException) {
            if (driverMapper.insert(driver) > 0) {
                return driverMapper.selectById(driver.getId());
            }
            throw new ServiceException("The driver add failed");
        }
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.ID, key = "#id", condition = "#result==true"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.DEVICE_ID, allEntries = true, condition = "#result!=true"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.PROFILE_ID, allEntries = true, condition = "#result!=true"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.SERVICE_NAME, allEntries = true, condition = "#result==true"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.HOST_PORT, allEntries = true, condition = "#result==true"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.DIC, allEntries = true, condition = "#result==true"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.LIST, allEntries = true, condition = "#result==true")
            }
    )
    public boolean delete(Long id) {
        selectById(id);
        return driverMapper.deleteById(id) > 0;
    }

    @Override
    @Caching(
            put = {
                    @CachePut(value = Common.Cache.DRIVER + Common.Cache.ID, key = "#driver.id", condition = "#result!=null"),
                    @CachePut(value = Common.Cache.DRIVER + Common.Cache.SERVICE_NAME, key = "#driver.serviceName", condition = "#result!=null"),
                    @CachePut(value = Common.Cache.DRIVER + Common.Cache.HOST_PORT, key = "#driver.type+'.'+#driver.host+'.'+#driver.port+'.'+#driver.tenantId", condition = "#result!=null")
            },
            evict = {
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.DEVICE_ID, allEntries = true, condition = "#result!=null"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.PROFILE_ID, allEntries = true, condition = "#result!=null"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.DIC, allEntries = true, condition = "#result!=null"),
                    @CacheEvict(value = Common.Cache.DRIVER + Common.Cache.LIST, allEntries = true, condition = "#result!=null")
            }
    )
    public Driver update(Driver driver) {
        selectById(driver.getId());
        driver.setUpdateTime(null);
        if (driverMapper.updateById(driver) > 0) {
            Driver select = driverMapper.selectById(driver.getId());
            driver.setServiceName(select.getServiceName()).setHost(select.getHost()).setPort(select.getPort());
            return select;
        }
        throw new ServiceException("The driver update failed");
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.ID, key = "#id", unless = "#result==null")
    public Driver selectById(Long id) {
        Driver driver = driverMapper.selectById(id);
        if (null == driver) {
            throw new NotFoundException("The driver does not exist");
        }
        return driver;
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.DEVICE_ID, key = "#deviceId", unless = "#result==null")
    public Driver selectByDeviceId(Long deviceId) {
        Device device = deviceService.selectById(deviceId);
        return selectById(device.getDriverId());
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.SERVICE_NAME, key = "#serviceName", unless = "#result==null")
    public Driver selectByServiceName(String serviceName) {
        LambdaQueryWrapper<Driver> queryWrapper = Wrappers.<Driver>query().lambda();
        queryWrapper.eq(Driver::getServiceName, serviceName);
        Driver driver = driverMapper.selectOne(queryWrapper);
        if (null == driver) {
            throw new NotFoundException("The driver does not exist");
        }
        return driver;
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.HOST_PORT, key = "#type+'.'+#host+'.'+#port+'.'+#tenantId", unless = "#result==null")
    public Driver selectByHostPort(String type, String host, Integer port, Long tenantId) {
        LambdaQueryWrapper<Driver> queryWrapper = Wrappers.<Driver>query().lambda();
        queryWrapper.eq(Driver::getType, type);
        queryWrapper.eq(Driver::getHost, host);
        queryWrapper.eq(Driver::getPort, port);
        queryWrapper.eq(Driver::getTenantId, tenantId);
        Driver driver = driverMapper.selectOne(queryWrapper);
        if (null == driver) {
            throw new NotFoundException("The driver does not exist");
        }
        return driver;
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.LIST, keyGenerator = "commonKeyGenerator", unless = "#result==null")
    public List<Driver> selectByIds(Set<Long> ids) {
        List<Driver> drivers = driverMapper.selectBatchIds(ids);
        if (null == drivers || drivers.size() < 1) {
            throw new NotFoundException("The driver does not exist");
        }
        return drivers;
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.PROFILE_ID, key = "#profileId", unless = "#result==null")
    public List<Driver> selectByProfileId(Long profileId) {
        Set<Long> deviceIds = profileBindService.selectDeviceIdByProfileId(profileId);
        List<Device> devices = deviceService.selectByIds(deviceIds);
        Set<Long> driverIds = devices.stream().map(Device::getDriverId).collect(Collectors.toSet());
        return selectByIds(driverIds);
    }

    @Override
    @Cacheable(value = Common.Cache.DRIVER + Common.Cache.LIST, keyGenerator = "commonKeyGenerator", unless = "#result==null")
    public Page<Driver> list(DriverDto driverDto) {
        if (null == driverDto.getPage()) {
            driverDto.setPage(new Pages());
        }
        return driverMapper.selectPage(driverDto.getPage().convert(), fuzzyQuery(driverDto));
    }

    @Override
    public LambdaQueryWrapper<Driver> fuzzyQuery(DriverDto driverDto) {
        LambdaQueryWrapper<Driver> queryWrapper = Wrappers.<Driver>query().lambda();
        if (null != driverDto) {
            if (StrUtil.isNotBlank(driverDto.getName())) {
                queryWrapper.like(Driver::getName, driverDto.getName());
            }
            if (StrUtil.isNotBlank(driverDto.getServiceName())) {
                queryWrapper.like(Driver::getServiceName, driverDto.getServiceName());
            }
            if (StrUtil.isNotBlank(driverDto.getHost())) {
                queryWrapper.like(Driver::getHost, driverDto.getHost());
            }
            if (null != driverDto.getPort()) {
                queryWrapper.eq(Driver::getPort, driverDto.getPort());
            }
            if (StrUtil.isBlank(driverDto.getType())) {
                driverDto.setType(Common.Driver.Type.DRIVER);
            }
            queryWrapper.like(Driver::getType, driverDto.getType());
            if (null != driverDto.getEnable()) {
                queryWrapper.eq(Driver::getEnable, driverDto.getEnable());
            }
            if (null != driverDto.getTenantId()) {
                queryWrapper.eq(Driver::getTenantId, driverDto.getTenantId());
            }
        }
        return queryWrapper;
    }

}
