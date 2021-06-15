package cn.gjyniubi.cinema.common.dto;

import cn.gjyniubi.cinema.common.entry.DocHall;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class SimpleHallDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HallDto extends DocHall {
    protected String createUser;
    protected Integer nonNull;
}
