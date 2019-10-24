package io.sansam.sswork.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Delay通用对象
 */
@Data
@EqualsAndHashCode
@ToString
public class DelayedObject<T> implements Delayed {

    /**
     * 默认延迟30分钟
     */
    private final static long DELAY = 30 * 60 * 1000L;
    /**
     * 数据id
     */
    private Long dataId;
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 到期时间 秒
     */
    private long expire;
    /**
     * 创建时间
     */
    private Date now;
    /**
     * 泛型data
     */
    private T data;

    public DelayedObject(Long dataId, long startTime, long expirea) {
        this.dataId = dataId;
        this.startTime = startTime;
        this.expire = startTime + expire * 1000;
        this.now = new Date();
    }

    public DelayedObject(Long dataId, long startTime) {
        this.dataId = dataId;
        this.startTime = startTime;
        this.expire = startTime + DELAY;
        this.now = new Date();
    }

    @Override
    public long getDelay(TimeUnit timeUnit) {
        return timeUnit.convert(getExpire() - getStartTime(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed delayed) {
        return BigDecimal.valueOf(this.getDelay(TimeUnit.MILLISECONDS) - delayed.getDelay(TimeUnit.MILLISECONDS)).intValue();
    }
}
