package cn.gjyniubi.cinema.admin.core.doc.type.controller;

import cn.gjyniubi.cinema.admin.core.doc.type.dto.FilmTypeDto;
import cn.gjyniubi.cinema.admin.core.doc.type.service.FilmTypeService;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.CreateTypeVo;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.QueryTypeVo;
import cn.gjyniubi.cinema.admin.core.doc.type.vo.UpdateTypeVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.dto.SimpleFilmTypeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class FilmTypeController
 */
@CrossOrigin
@Api(tags = "电影类型")
@RequestMapping("/doc/film-type/")
@RestController
public class FilmTypeController {

    @Autowired
    private FilmTypeService typeService;

    @ApiOperation("获取类型列表")
    @GetMapping("/list")
    public TableData<FilmTypeDto> getList(@Validated QueryTypeVo queryTypeVo){
        return typeService.getList(queryTypeVo);
    }

    @ApiOperation("更新类型")
    @PostMapping("/update")
    public void update(@Validated @RequestBody UpdateTypeVo updateTypeVo){
        typeService.updateType(updateTypeVo);
    }

    @ApiOperation("删除类型")
    @PostMapping("/delete")
    public void delete(@RequestParam Integer id){
        typeService.deleteType(id);
    }

    @ApiOperation("创建类型")
    @PostMapping("/create")
    public void create(@Validated @RequestBody CreateTypeVo createTypeVo){
        typeService.createType(createTypeVo);
    }

    @ApiOperation("获取所有类型")
    @GetMapping("/all")
    public List<SimpleFilmTypeDto> getAllTypes(){
        return typeService.getAllTypes();
    }
}
