package io.sansam.sswork.service.order;

import io.sansam.sswork.po.DelayedObject;
import io.sansam.sswork.po.order.PayOrder;
import io.sansam.sswork.util.queue.MyDelayQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.Executor;

@Slf4j
@Component
@Lazy(false)
public class DelayedOrderImpl implements IDelayedOrder<PayOrder> {

//    payOrderDelayQueueExecutor是注入的一个专门处理出队的一个线程

//    @PostConstruct是啥呢,是在容器启动后只进行一次初始化动作的一个注解,相当实用

//    启动后呢,我们去数据库扫描一遍,防止有漏网之鱼,因为单机版吗,队列的数据是在内存中的,重启后肯定原先的数据会丢失,所以为保证服务质量,我们可能会录音.....所以为保证重启后数据的恢复,我们需要重新扫描数据库把未支付的数据重新装载到内存的队列中

//    接下来就是用这个线程去一直不停的访问队列的take()方法,当队列无数据就一直阻塞,或者数据没到期继续阻塞着,直到到期出队,然后获取订单的信息,去处理订单的更新操作

    @Autowired
    @Qualifier(value = "payOrderDelayQueueExecutor")
    Executor executor;

    @PostConstruct
    public void init() {
        executor.execute(() -> {
            log.info("启动处理的订单线程:" + Thread.currentThread().getName());
            DelayedObject<PayOrder> delayedObject;
            for (; ; ) {
                try {
                    delayedObject = delayQueue.take();
                    // 处理订单超时逻辑
                    log.info("支付订单超时了, {}", delayedObject);
                } catch (InterruptedException e) {
                    log.error("执行支付超时订单的_延迟队列_异常:", e);
                }
            }
        });
    }

    private MyDelayQueue<DelayedObject<PayOrder>> delayQueue = new MyDelayQueue<>();

    @Override
    public boolean addToOrderDelayQueue(DelayedObject<PayOrder> delayedObject) {
        return this.delayQueue.offer(delayedObject);
    }

    @Override
    public boolean addToDelayQueue(PayOrder data) {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        DelayedObject<PayOrder> delayedObject = new DelayedObject<>(data.getId(), timestamp.getTime());
        return this.delayQueue.offer(delayedObject);
    }

    @Override
    public boolean removeToOrderDelayQueue(PayOrder data) {
        boolean flag = false;
        Long id = data.getId();
        Iterator<DelayedObject<PayOrder>> iterator = this.delayQueue.iterator();
        DelayedObject<PayOrder> next;

        for (this.delayQueue.iterator(); iterator.hasNext(); ) {
            next = iterator.next();
            if (id.equals(next.getDataId())) {
                iterator.remove();
                flag = true;
            }
        }
        return flag;
    }
}
