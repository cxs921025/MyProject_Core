package com.cxs.core.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.Set;

/**
 * @author ChenXS
 * redis工具类
 */
@Component
public class RedisUtil {

    private final JedisPool jedisPool;

    // session 在redis过期时间是30分钟30*60
    private int expireTime = 1800;

    @Autowired
    public RedisUtil(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    // 从连接池获取redis连接
    private Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }

    // 回收redis连接
    private void recycleJedis(Jedis jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 保存字符串数据
    public void setString(String key, String value) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                jedis.set(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // 保存字符串数据，并设置时间
    public void setString(String key, String value, int expireTime) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                jedis.setex(key, expireTime, value);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // 获取字符串类型的数据
    public String getString(String key) {
        Jedis jedis = getJedis();
        String result = "";
        if (jedis != null) {
            try {
                result = jedis.get(key);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
        return result;
    }

    // 删除字符串数据
    public void deleteByKey(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                jedis.del(key);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // 保存byte类型数据
    public void setObject(byte[] key, byte[] value) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                if (!jedis.exists(key)) {
                    jedis.set(key, value);
                }
                // redis中session过期时间
                jedis.expire(key, expireTime);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // 保存byte类型数据，并设置时间
    public void setObject(byte[] key, byte[] value, int expireTime) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                if (!jedis.exists(key)) {
                    jedis.setex(key, expireTime, value);
                }
                // redis中session过期时间
                jedis.expire(key, expireTime);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // 获取byte类型数据
    public byte[] getObject(byte[] key) {
        Jedis jedis = getJedis();
        byte[] bytes = null;
        if (jedis != null) {
            try {
                bytes = jedis.get(key);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
        return bytes;

    }

    // 更新byte类型的数据，主要更新过期时间
    public void updateObject(byte[] key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                // redis中session过期时间
                jedis.expire(key, expireTime);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // key对应的整数value加1
    public void inc(String key) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                if (!jedis.exists(key)) {
                    jedis.set(key, "1");
                    // 计数器的过期时间默认2天
                    int countExpireTime = 2 * 24 * 3600;
                    jedis.expire(key, countExpireTime);
                } else {
                    // 加1
                    jedis.incr(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
    }

    // 获取所有keys
    public Set<String> getAllKeys(String pattern) {
        Jedis jedis = getJedis();
        if (jedis != null) {
            try {
                return jedis.keys(pattern);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                recycleJedis(jedis);
            }
        }
        return null;
    }

    /**
     * 将Object对象转换为byte数组
     *
     * @param object 待转换的Object对象
     * @return 转换之后的byte数组
     */
    public byte[] objectToByteArray(Object object) {
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ObjectOutputStream objectStream = new ObjectOutputStream(byteArrayStream);
            objectStream.writeObject(object);
            bytes = byteArrayStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将byte数组转换为Object对象
     *
     * @param bytes 待转换的byte数组
     * @return 转换之后的Object对象
     */
    public Object byteArrayToObject(byte[] bytes) {
        ByteArrayInputStream byteArrayStream = new ByteArrayInputStream(bytes);
        Object object = null;
        try {
            ObjectInputStream objectStream = new ObjectInputStream(byteArrayStream);
            object = objectStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return object;
    }
}
