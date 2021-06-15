package cn.gjyniubi.cinema.admin.core.doc.cinema.dto;

import cn.gjyniubi.cinema.common.entry.DocCinema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author gujianyang
 * @Date 2021/5/16
 * @Class CinemaDto
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class CinemaDto extends DocCinema {
    private String createUser;
}
