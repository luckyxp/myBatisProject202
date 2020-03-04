package com.xp.mybatis.sqlsession;

/**
 * @author Climb.Xu
 * @date 2020/2/26 21:31
 * 自定义mybatis中,和数据库交互的核心类
 * 他可以创建dao接口的代理对象
 */
public interface SqlSession {
    /**
     * 根据参数创建一个代理对象
     *
     * @param daoInterfaceClass dao接口的字节码
     * @param <T>
     * @return
     */
    <T> T getMapper(Class<T> daoInterfaceClass);

    /**
     * 释放资源
     */
    void close();
}
