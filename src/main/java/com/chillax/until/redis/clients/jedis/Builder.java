package com.chillax.until.redis.clients.jedis;

public abstract class Builder<T> {
    public abstract T build(Object data);
}
