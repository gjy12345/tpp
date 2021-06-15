package cn.gjyniubi.cinema.common.domain;

import lombok.Data;

import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/5/13
 * @Class TableData
 */
@Data
public class TableData <T>{
    private List<T> list;
    private int total;

    public static <T> TableData<T> buildData(List<T> data,int total){
        TableData<T> tableData=new TableData<>();
        tableData.setList(data);
        tableData.setTotal(total);
        return tableData;
    }
}
