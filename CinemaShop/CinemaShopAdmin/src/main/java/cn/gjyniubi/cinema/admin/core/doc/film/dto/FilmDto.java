package cn.gjyniubi.cinema.admin.core.doc.film.dto;

import cn.gjyniubi.cinema.common.dto.SimpleFilmTypeDto;
import cn.gjyniubi.cinema.common.entry.DocFilm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/18
 * @Class FilmDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FilmDto extends DocFilm {
    private String createUser;
    private List<SimpleFilmTypeDto> filmTypes;
}
