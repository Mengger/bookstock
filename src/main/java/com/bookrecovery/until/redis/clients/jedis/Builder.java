package com.bookrecovery.until.redis.clients.jedis;

public abstract class Builder<T> {
    public abstract T build(Object data);
}
