package cn.gjyniubi.cinema.admin.core.doc.hall.controller;

import cn.gjyniubi.cinema.common.domain.HallSite;
import cn.gjyniubi.cinema.admin.core.doc.hall.service.HallService;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.CreateHallVo;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.HallSiteVo;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.QueryHallVo;
import cn.gjyniubi.cinema.admin.core.doc.hall.vo.UpdateHallVo;
import cn.gjyniubi.cinema.common.domain.TableData;
import cn.gjyniubi.cinema.common.dto.HallDto;
import cn.gjyniubi.cinema.common.dto.SimpleHallDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/19
 * @Class HallController
 */
@Api(tags = "影厅相关接口")
@CrossOrigin
@RequestMapping("/doc/hall")
@RestController
public class HallController {

    @Autowired
    private HallService hallService;

    /**
     * 不返回座位信息
     * @param queryHallVo 查询对象
     * @return 不含座位信息的影厅信息
     */
    @ApiOperation("获取影厅列表")
    @GetMapping("/list")
    public TableData<HallDto> getHallList(@Validated QueryHallVo queryHallVo){
        return hallService.getHallList(queryHallVo);
    }

    @ApiOperation("创建影厅")
    @PostMapping("/create")
    public void create(@Validated @RequestBody CreateHallVo createHallVo){
        hallService.createHall(createHallVo);
    }

    @ApiOperation("更新影厅")
    @PostMapping("/update")
    public void update(@Validated @RequestBody UpdateHallVo updateHallVo){
        hallService.updateHall(updateHallVo);
    }

    @ApiOperation("删除影厅")
    @PostMapping("/delete")
    public void delete(@RequestParam Integer id){
        hallService.deleteHall(id);
    }

    @ApiOperation("更新座位信息")
    @PostMapping("/site/update")
    public void updateSite(@Validated @RequestBody HallSiteVo hallSiteVo){
        hallService.updateSite(hallSiteVo);
    }

    @ApiOperation("获取座位信息")
    @GetMapping("/site/get")
    public HallSite[][] getSite(@RequestParam Integer hallId){
        return hallService.getSite(hallId);
    }

    @ApiOperation("获取影厅信息，用于下拉框")
    @GetMapping("/simpleAll")
    public List<SimpleHallDto> getAllHall(@RequestParam Integer cinemaId){
        return hallService.getAllHallWithSimple(cinemaId);
    }

}
