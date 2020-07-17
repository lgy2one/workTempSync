package com.qg.exclusiveplug.service;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class AccessLimitService {

    //每秒只发出5个令牌
    final RateLimiter rateLimiter = RateLimiter.create(5.0);

    /**
     * 尝试获取令牌
     * @return
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }
}