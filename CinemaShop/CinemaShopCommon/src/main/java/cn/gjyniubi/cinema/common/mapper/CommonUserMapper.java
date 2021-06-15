package cn.gjyniubi.cinema.common.mapper;

import cn.gjyniubi.cinema.common.entry.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author gujianyang
 * @Date 2021/5/12
 * @Class CommonUserMapper
 */
@Repository
@Mapper
public interface CommonUserMapper extends BaseMapper<SysUser> {
}
