package cn.gjyniubi.cinema.app.domain;

import lombok.Builder;
import lombok.Data;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class DelayMessage
 */
@Builder
@Data
public class DelayMessage <T> implements Delayed {

    private long begin;
    private long delay;
    private T data;
    private TimeUnit delayUnit;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(begin+delayUnit.toMillis((delay))-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS)-o.getDelay(TimeUnit.MILLISECONDS));
    }

}
