package cn.gjyniubi.cinema.app;

import cn.gjyniubi.cinema.app.domain.DelayMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class ScheduleQueueTest
 */
@SpringBootTest
public class ScheduleQueueTest {

    @Autowired
    private DelayQueue<DelayMessage<?>> delayQueue;

    @Test
    public void testSort() throws InterruptedException {
        delayQueue.add(DelayMessage.builder().begin(System.currentTimeMillis()-2000).data(11).delay(0).
                delayUnit(TimeUnit.SECONDS).build());
        delayQueue.add(DelayMessage.builder().begin(System.currentTimeMillis()+10).data(1122).delay(1).
                delayUnit(TimeUnit.SECONDS).build());
        long l = System.currentTimeMillis();
        while (!delayQueue.isEmpty()){
            DelayMessage<?> poll = delayQueue.poll(10, TimeUnit.SECONDS);
            if(poll!=null){
                System.out.println(poll);
            }else {
                System.out.println("空的");
            }
        }
        System.out.println(System.currentTimeMillis() - l);
    }

}
