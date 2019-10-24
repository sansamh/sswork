package io.sansam.sswork.util.queue;

import java.util.PriorityQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * MyDelayQueue
 * </p>
 *
 * @author houcb
 * @since 2019-10-24 11:50
 */
public class MyDelayQueue<E extends Delayed> extends DelayQueue<E> {

    private final Lock lock = new ReentrantLock();

    private final PriorityQueue<E> priorityQueue = new PriorityQueue<>();

    private Thread lead = null;

    private Condition awaitCondition = lock.newCondition();


    @Override
    public E take() throws InterruptedException {
        final Lock rLock = this.lock;
        rLock.lockInterruptibly();
        try {
            for (; ; ) {
                E peek = priorityQueue.peek();
                if (null == peek) {
                    awaitCondition.await();
                }
                long delay = peek.getDelay(TimeUnit.NANOSECONDS);
                if (delay <= 0) {
                    return priorityQueue.poll();
                }
                peek = null;
                // 说明有其他线程在take 所以排队
                if (lead != null) {
                    awaitCondition.await();
                } else {
                    // 设置当前线程为lead
                    Thread thread = Thread.currentThread();
                    lead = thread;
                    try {
                        // lead等待在这里 等待头部元素生效
                        awaitCondition.awaitNanos(delay);
                    } finally {
                        // 如果lead为当前线程 清除lead
                        if (lead == thread) {
                            lead = null;
                        }
                    }

                }
            }
        } finally {
            // 给个机会唤醒等待的线程
            if (lead == null && priorityQueue.peek() != null) {
                awaitCondition.signal();
            }
            rLock.unlock();
        }

    }


    @Override
    public boolean offer(E e) {
        final Lock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            priorityQueue.offer(e);
            // 如果添加进去的元素为队首元素 则之前等待的线程失去意义了（因为之前的线程等待的是之前队首元素到期） 现在有了新的队首元素
            // 需要新的线程来获取队首元素
            if (priorityQueue.peek() == e) {
                lead = null;
                awaitCondition.signal();
            }
            return true;
        } finally {
            reentrantLock.unlock();
        }

    }

}
