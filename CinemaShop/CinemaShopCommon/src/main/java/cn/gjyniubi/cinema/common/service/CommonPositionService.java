package cn.gjyniubi.cinema.common.service;

import cn.gjyniubi.cinema.common.annotations.CacheFirst;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.entry.District;
import cn.gjyniubi.cinema.common.mapper.CommonPositionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/16
 * @Class CommonPositionService
 */
@Service
public class CommonPositionService {

    @Autowired
    private CommonPositionMapper commonPositionMapper;

    @CacheFirst(value = "",noArgs = true,prefix = UserContact.PROVINCES_CACHE,cache = true,cacheTime = 1,
            cacheTimeUnit = TimeUnit.HOURS)
    public List<District> getProvinces() {
        return commonPositionMapper.selectProvinces();
    }

    @CacheFirst(value = "provinceId",prefix = UserContact.CITIES_CACHE,cache = true,cacheTime = 1,
            cacheTimeUnit = TimeUnit.HOURS,cacheOnArgsNull = true)
    public List<District> getCities(Integer provinceId) {
        return commonPositionMapper.selectCities(provinceId);
    }

    @CacheFirst(value = "cityId",prefix = UserContact.COUNTRIES_CACHE,cache = true,cacheTime = 1,
            cacheTimeUnit = TimeUnit.HOURS,cacheOnArgsNull = true)
    public List<District> getCountries(Integer cityId) {
        return commonPositionMapper.selectCountries(cityId);
    }
}
