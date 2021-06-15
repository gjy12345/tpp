package cn.gjyniubi.cinema.common.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.ToIntFunction;

/**
 * @Author gujianyang
 * @Date 2021/5/25
 * @Class ListData
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListData<T> extends BaseSerializable{
    protected Integer offset;//如果是简单排序，那么使用流式查询
    protected List<T> data;

    public static <T> ListData<T> buildListData(List<T> data) {
        ListData<T> listData = new ListData<>();
        listData.data= data;
        return listData;
    }

    public static <T> ListData<T> buildListData(List<T> data, ToIntFunction<T> toIntFunction,boolean min){
        ListData<T> listData = new ListData<>();
        OptionalInt optionalInt = min?data.stream().mapToInt(toIntFunction).min()
                :data.stream().mapToInt(toIntFunction).max();
        if(optionalInt.isPresent())
            listData.offset=optionalInt.getAsInt();
        else
            listData.offset=0;
        listData.data=data;
        return listData;
    }

}
