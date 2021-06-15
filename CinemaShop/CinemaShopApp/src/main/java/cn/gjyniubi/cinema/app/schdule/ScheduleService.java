package cn.gjyniubi.cinema.app.schdule;

import cn.gjyniubi.cinema.app.core.order.service.OrderService;
import cn.gjyniubi.cinema.app.domain.DelayMessage;
import cn.gjyniubi.cinema.common.entry.Order;
import cn.gjyniubi.cinema.common.enums.OrderStatus;
import cn.gjyniubi.cinema.common.mapper.CommonOrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author gujianyang
 * @Date 2021/5/31
 * @Class ScheduleService
 */
@Slf4j
@Service("cus_schedule")
public class ScheduleService {

    @Autowired
    private DelayQueue<DelayMessage<?>> delayQueue;

    @Autowired
    private CommonOrderMapper commonOrderMapper;

    private Thread t;

    @Autowired
    private OrderService orderService;

    @Bean
    public DelayQueue<DelayMessage<?>> delayQueue(){
        return new DelayQueue<>();
    }

    @PostConstruct
    public void init(){
        t=new Thread(()->{
            while (true){
                DelayMessage<?> message = delayQueue.poll();
                if(message==null) {
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException e) {
                        log.error("延迟队列获取已停止..");
                        return;
                    }
                }
                //对消息进行消费
                handleMessage(message);
            }
        });
        t.start();
        //加载初始数据
        loadData();
    }

    private void handleMessage(DelayMessage<?> message) {
        Object data = message.getData();
        if(data instanceof Order){
            //订单
            orderService.doOrderTimeOut((Order)data);
        }
    }

    private void loadData(){
        final long begin = System.currentTimeMillis();
        log.info("从数据库加载数据到延迟队列..");
        List<Order> orders = commonOrderMapper.selectList(new QueryWrapper<Order>()
                .eq("`status`", OrderStatus.WAIT_PAY.getCode()));
        for (Order order : orders) {
            this.delayQueue.offer(DelayMessage.builder()
                    .delayUnit(TimeUnit.MINUTES)
                    .delay(15)
                    .begin(order.getCreateTime().getTime())
                    .data(order)
                    .build());
        }
        log.info("从数据库加载数据到延迟队列结束,耗时:{}毫秒",System.currentTimeMillis()-begin);
    }

    public void addNewDelayMessage(DelayMessage<?> message) {
        this.delayQueue.offer(message);
    }
}
