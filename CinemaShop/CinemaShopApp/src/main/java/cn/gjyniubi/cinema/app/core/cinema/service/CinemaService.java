package cn.gjyniubi.cinema.app.core.cinema.service;

import cn.gjyniubi.cinema.app.core.cinema.dto.CinemaDto;
import cn.gjyniubi.cinema.app.core.cinema.dto.DayScheduleDto;
import cn.gjyniubi.cinema.app.core.cinema.dto.FilmScheduleDto;
import cn.gjyniubi.cinema.app.core.cinema.dto.SimpleCinemaDto;
import cn.gjyniubi.cinema.app.core.cinema.mapper.CinemaMapper;
import cn.gjyniubi.cinema.app.core.cinema.mapper.FilmScheduleMapper;
import cn.gjyniubi.cinema.app.core.cinema.vo.CinemaListQuery;
import cn.gjyniubi.cinema.app.core.cinema.vo.ScheduleCinemaListQuery;
import cn.gjyniubi.cinema.app.core.film.dto.FilmInfoDto;
import cn.gjyniubi.cinema.common.contact.UserContact;
import cn.gjyniubi.cinema.common.domain.ListData;
import cn.gjyniubi.cinema.common.domain.SimpleJoinParamQuery;
import cn.gjyniubi.cinema.common.entry.*;
import cn.gjyniubi.cinema.common.exception.NoSuchDataException;
import cn.gjyniubi.cinema.common.mapper.CommonDocHallMapper;
import cn.gjyniubi.cinema.common.mapper.CommonFilmDocMapper;
import cn.gjyniubi.cinema.common.mapper.CommonFilmTypesMapper;
import cn.gjyniubi.cinema.common.mapper.CommonPositionMapper;
import cn.gjyniubi.cinema.common.util.BeanAssignment;
import cn.gjyniubi.cinema.common.util.DateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author gujianyang
 * @Date 2021/5/26
 * @Class CinemaService
 */
@Service
public class CinemaService {

    @Autowired
    private CinemaMapper cinemaMapper;

    @Autowired
    private CommonPositionMapper commonPositionMapper;

    @Autowired
    private FilmScheduleMapper filmScheduleMapper;

    @Autowired
    private CommonDocHallMapper docHallMapper;

    @Autowired
    private CommonFilmDocMapper commonFilmDocMapper;

    @Autowired
    private CommonFilmTypesMapper commonFilmTypesMapper;

    public ListData<SimpleCinemaDto> getCinemaList(CinemaListQuery cinemaListQuery) {
        return ListData.buildListData(
                cinemaMapper.selectSimpleCinemaList(cinemaListQuery, getPosition(cinemaListQuery.getArea())),
                SimpleCinemaDto::getId,false
        );
    }

    private SimpleJoinParamQuery getPosition(Integer id) {
        if (id == null)
            return null;
        District district;
        int count = 0;
        String[] arr = new String[3];
        do {
            district = commonPositionMapper.selectDistrictById(id);
            if (district != null) {
                arr[count++] = district.getName();
                id = district.getPid();
            }
        } while (district != null && district.getLevel() > 0);
        return new SimpleJoinParamQuery(UserContact.POSITION_SEPARATOR,
                Objects::nonNull,
                arr[2], arr[1], arr[0]);
    }


    public ListData<SimpleCinemaDto> getHasScheduleCinemaList(ScheduleCinemaListQuery query) {
        DocFilm film = commonFilmDocMapper.selectOne(new QueryWrapper<DocFilm>().eq("uid", query.getFilmUid()));
        if (film == null)
            throw new NoSuchDataException("没有此电影");
        query.setFilmId(film.getId());
        return ListData.buildListData(
                cinemaMapper.selectSimpleScheduleCinemaList(query, getPosition(query.getArea()))
        );
    }

    public List<FilmScheduleDto> getScheduleFilms(String cinemaUid) {
        DocCinema cinema = cinemaMapper.selectOne(new QueryWrapper<DocCinema>().eq("uid", cinemaUid));
        if (cinema == null)
            throw new NoSuchDataException("没有此影院");
        List<FilmSchedule> filmSchedules = filmScheduleMapper.selectCinemaSchedules(cinema.getId(),
                DateTimeUtil.localDateTimeToDate(LocalDateTime.now().plusDays(7)));
        Map<Integer, DocHall> hallMap = new ConcurrentHashMap<>();
        // 电影id
        Map<Integer, List<DayScheduleDto>> scheduleMap = new ConcurrentHashMap<>();
        //将所有排片根据电影分类
        filmSchedules.parallelStream()
                .forEach(filmSchedule -> {
                    DayScheduleDto dayScheduleDto = new DayScheduleDto();
                    dayScheduleDto.setBegin(filmSchedule.getBeginTime());
                    dayScheduleDto.setEnd(filmSchedule.getEndTime());
                    dayScheduleDto.setDay(filmSchedule.getBeginTime());
                    DocHall hall = hallMap.computeIfAbsent(filmSchedule.getHallId(),
                            k -> docHallMapper.selectById(filmSchedule.getHallId()));
                    dayScheduleDto.setHallUid(hall.getUid());
                    dayScheduleDto.setHallName(hall.getName());
                    dayScheduleDto.setMinPrice(filmSchedule.getMinPrice());
                    dayScheduleDto.setUid(filmSchedule.getUid());
                    scheduleMap.computeIfAbsent(filmSchedule.getFilmId(),
                            k -> Collections.synchronizedList(new ArrayList<>())).add(dayScheduleDto);
                });
        List<FilmScheduleDto> result = Collections.synchronizedList(new ArrayList<>());
        //将电影根据日期分类
        scheduleMap.keySet().parallelStream().forEach(filmId -> {
            List<DayScheduleDto> daySchedules = scheduleMap.get(filmId);
            FilmScheduleDto filmScheduleDto = new FilmScheduleDto();
            DocFilm docFilm = commonFilmDocMapper.selectById(filmId);
            FilmInfoDto dto = BeanAssignment.copySameFields(docFilm, new FilmInfoDto());
            dto.setTypes(commonFilmTypesMapper.selectFilmSimpleTypes(docFilm.getId()));
            filmScheduleDto.setFilmInfoDto(dto);
            List<FilmScheduleDto.DaySchedule> groupByDayList = new ArrayList<>();
            daySchedules.stream()
                    .sorted(Comparator.comparing(DayScheduleDto::getBegin))
                    .forEach(dayScheduleDto -> {
                        FilmScheduleDto.DaySchedule day;
                        if (groupByDayList.size() == 0 ||
                                !DateTimeUtil.isSameDay(dayScheduleDto.getDay(),
                                        groupByDayList.get(groupByDayList.size() - 1).getDay())) {
                            day = new FilmScheduleDto.DaySchedule();
                            day.setDay(dayScheduleDto.getDay());
                            day.setScheduleList(new ArrayList<>());
                            groupByDayList.add(day);
                        } else {
                            day = groupByDayList.get(groupByDayList.size() - 1);
                        }
                        day.getScheduleList().add(dayScheduleDto);
                    });
            filmScheduleDto.setDaySchedules(groupByDayList);
            result.add(filmScheduleDto);
        });
        return result;
    }

    public CinemaDto getCinemaInfo(String uid) {
        DocCinema cinema = cinemaMapper.selectOne(new QueryWrapper<DocCinema>().eq("uid", uid));
        if (cinema == null)
            throw new NoSuchDataException("没有此影院");
        CinemaDto cinemaDto = BeanAssignment.copySameFields(cinema, new CinemaDto());
        cinemaDto.setFullPosition(cinema.getPosition()+" "+cinema.getAddress());
        return cinemaDto;
    }
}
