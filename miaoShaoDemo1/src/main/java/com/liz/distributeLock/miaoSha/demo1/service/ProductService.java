package com.liz.distributeLock.miaoSha.demo1.service;

/**
 * Created by lizhou on 2020/6/6.
 */
public interface ProductService {

    public boolean decrementProductStore(Long productId, Integer productQuantity);
}
