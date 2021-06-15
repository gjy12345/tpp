package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.District;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/16
 * @Class CommonPositionMapper
 */
@Mapper
@Repository
public interface CommonPositionMapper {

    @Select("select * from district where level=0")
    List<District> selectProvinces();

    List<District> selectCities(@Param("provinceId") Integer provinceId);

    List<District> selectCountries(@Param("cityId") Integer cityId);

    @Select("select * from district where id=#{id}")
    District selectDistrictById(@Param("id") Integer id);
}
