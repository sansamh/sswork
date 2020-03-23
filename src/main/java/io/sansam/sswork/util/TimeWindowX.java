package io.sansam.sswork.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;

@Slf4j
public class TimeWindowX {

    private ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();

    private int seconds;

    private int max;

    public TimeWindowX(int seconds, int max) {
        this.seconds = seconds;
        this.max = max;
        // 开启清理线程
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep((seconds - 1) * 1000L);
                } catch (InterruptedException e) {
                    log.error("清理线程出现异常", e);
                }
                clean();
            }
        }).start();
    }

    public static void main(String[] args) {
        new TimeWindowX(5, 3).start();
    }

    /**
     * 清理过期数据的线程
     */
    private void clean() {
        long startPoint = (System.currentTimeMillis() - seconds) * 1000L;
        Long next;
        while ((next = queue.peek()) != null && next < startPoint) {
            next = queue.poll();
            log.info("清理数据 " + next);

        }
    }

    /**
     * 获取令牌的方法
     *
     * @return
     */
    public boolean take() {
        int size = sizeOfValid(System.currentTimeMillis());
        if (size >= max) {
            log.warn("当前窗口线程数超过限制了");
            return false;
        }
        synchronized (queue) {
            long start = System.currentTimeMillis();
            size = sizeOfValid(start);
            if (size >= max) {
                log.warn("当前窗口线程数超过限制了");
                return false;
            }
            boolean offer = queue.offer(start);
            log.info("获取令牌[" + start + "]" + offer);
            return offer;
        }
    }

    /**
     * 查询当前统计窗口存在的线程数据
     *
     * @param now 截止时间
     * @return
     */
    private int sizeOfValid(long now) {
        Iterator<Long> iterator = queue.iterator();
        long startPoint = now - seconds * 1000;
        int count = 0;
        while (iterator.hasNext()) {
            if (iterator.next() > startPoint) {
                count++;
            }
        }
        log.info("时间段[" + startPoint + "]到[" + now + "]有线程数[" + count + "]");

        return count;
    }

    public void start() {
        IntStream.range(0, 3).forEach((i) -> new Thread(() -> {
                    while (true) {
                        try {
                            Thread.sleep(new Random().nextInt(20) * 100);
                        } catch (InterruptedException e) {
                            log.error("获取令牌线程异常", e);
                        }
                        log.info("线程[" + Thread.currentThread().getName() + "]开始获取令牌");
                        take();
                    }
                }).start()
        );
    }
}
