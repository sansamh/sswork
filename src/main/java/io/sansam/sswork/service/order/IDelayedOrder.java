package io.sansam.sswork.service.order;

import io.sansam.sswork.po.DelayedObject;

/**
 * delay对象操作接口
 * @param <T>
 */
public interface IDelayedOrder<T> {

    /**
     * 添加延迟对象到延时队列
     *
     * @param delayedObject 延迟对象
     * @return boolean
     */
    boolean addToOrderDelayQueue(DelayedObject<T> delayedObject);

    /**
     * 根据对象添加到指定延时队列
     *
     * @param data 数据对象
     * @return boolean
     */
    boolean addToDelayQueue(T data);

    /**
     * 移除指定的延迟对象从延时队列中
     *
     * @param data
     */
    boolean removeToOrderDelayQueue(T data);

}
