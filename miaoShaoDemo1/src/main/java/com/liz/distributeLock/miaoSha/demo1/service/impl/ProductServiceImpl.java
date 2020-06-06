package com.liz.distributeLock.miaoSha.demo1.service.impl;

import com.liz.distributeLock.miaoSha.demo1.Constants;
import com.liz.distributeLock.miaoSha.demo1.controller.ProductController;
import com.liz.distributeLock.miaoSha.demo1.service.JedisClientIService;
import com.liz.distributeLock.miaoSha.demo1.service.ProductService;
import com.sun.rowset.internal.InsertRow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by lizhou on 2020/6/6.
 * ————————————————
 * 版权声明：本文为CSDN博主「水能载舟，亦能覆舟」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/qq_37892957/article/details/89337943
 */
@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LogManager.getLogger(ProductServiceImpl.class);

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private JedisClientIService jedisClientIService;

    @Override
    public boolean decrementProductStore(Long productId, Integer productQuantity) {
        String key = "dec_stock_lock_" + productId;
        RLock lock = redissonClient.getLock(key);
        try {
            //加锁 操作很类似Java的ReentrantLock机制1`
            lock.lock();
            String str = jedisClientIService.get(Constants.PRODUCT_STOCK_KEY + productId );
            Integer stock = StringUtils.isEmpty(str) ? 0 : Integer.valueOf(str);
            //如果库存为空或不够
            if (stock == 0 || stock < productQuantity) {
                return false;
            }
            //简单减库存操作 没有重新写其他接口了
            jedisClientIService.set(Constants.PRODUCT_STOCK_KEY + productId ,(stock - productQuantity) + "");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        } finally {
            //解锁
            lock.unlock();
        }
        return true;


    }
}
