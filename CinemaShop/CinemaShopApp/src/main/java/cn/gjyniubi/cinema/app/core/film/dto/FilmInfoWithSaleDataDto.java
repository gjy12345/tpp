package cn.gjyniubi.cinema.app.core.film.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/6/3
 * @Class FilmInfoWithSaleDataDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FilmInfoWithSaleDataDto extends FilmInfoDto{
    private Long today;
    private Long allCount;
    private Long allSum;
}
