package com.liz.distributeLock.miaoSha.demo1.controller;

import com.liz.distributeLock.miaoSha.demo1.Constants;
import com.liz.distributeLock.miaoSha.demo1.service.JedisClientIService;
import com.liz.distributeLock.miaoSha.demo1.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lizhou on 2020/6/6.
 */
@RestController
@RequestMapping(value = "/product")
public class ProductController {
    private static final Logger log = LogManager.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private JedisClientIService jedisClientIService;


    @GetMapping("/getStock/{productId}")
    public String getStock(@PathVariable("productId") Long productId) {
        if(null == productId || productId <= 0L ){
            return "非法商品";
        }
        boolean exist = jedisClientIService.hasKey(Constants.PRODUCT_STOCK_KEY + productId);
        if(!exist){
            return "该商品暂无秒杀活动";
        }
        if (!productService.decrementProductStore(productId, 1)) {
            log.error("库存不足");
            return "库存不足";
        }
        String stock = jedisClientIService.get(Constants.PRODUCT_STOCK_KEY + productId);
        log.info("拿到库存，剩余" + stock);
        return "拿到库存，剩余：" + stock;
    }

    @GetMapping("/setStock/{stock}")
    public String setStock(@PathVariable("stock") Integer stock) {
        try{
            jedisClientIService.set(Constants.PRODUCT_STOCK_KEY + "1",stock+"");

            return "设置库存成功！总数为:"+stock;
        }catch (Exception e){
            log.info("设置库存失败...." + e.getMessage(),e);
            return "设置库存失败....";
        }

    }
}
