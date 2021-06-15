package cn.gjyniubi.cinema.common.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author gujianyang
 * @Date 2021/5/17
 * @Class SimpleJoinParamQuery
 */
public class SimpleJoinParamQuery {
    @Getter
    private String query;
    @Getter
    private Boolean useLike;
    @Setter
    private CheckCallBack checkCallBack;

    //SEPARATOR
    public SimpleJoinParamQuery(String separator,Object... params) {
        run(separator,null,params);
    }

    //SEPARATOR
    public SimpleJoinParamQuery(String separator,CheckCallBack checkCallBack,Object... params) {
        run(separator,checkCallBack,params);
    }

    private void run(String separator,CheckCallBack checkCallBack,Object... params){
        int level=0;
        boolean add;
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            if(checkCallBack!=null){
                add=checkCallBack.filter(params[i]);
            }else {
                add=params[i]!=null;
            }
            if(add){
                sb.append(params[i]);
                level++;
            }
            if(add&&i!=params.length-1&&separator!=null)
                sb.append(separator);
        }
        this.query=sb.toString();
        this.useLike=level!=params.length;
    }

    public interface CheckCallBack{
        boolean filter(Object args) ;
    }
}
