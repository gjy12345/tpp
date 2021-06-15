package cn.gjyniubi.cinema.admin.core.doc.type.dto;

import cn.gjyniubi.cinema.common.entry.DocFilmType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class FilmTypeDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FilmTypeDto extends DocFilmType {
    private Integer count;
    private String createUser;
}
