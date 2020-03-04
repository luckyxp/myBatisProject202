package com.xp.mybatis.sqlsession.defaults;

import com.xp.mybatis.cfg.Configuration;
import com.xp.mybatis.sqlsession.SqlSession;
import com.xp.mybatis.sqlsession.proxy.MapperProxy;
import com.xp.util.DataSourceUtil;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Climb.Xu
 * @date 2020/2/26 23:14
 * SqlSession接口的实现类
 * 一个默认的SqlSession
 */
public class DefaultSqlSession implements SqlSession {
    private Configuration cfg;

    private Connection conn;

    public DefaultSqlSession(Configuration cfg) {
        this.cfg = cfg;
        this.conn = DataSourceUtil.getConnection(cfg);
    }

    /**
     * 用于创建代理对象
     *
     * @param daoInterfaceClass dao接口的字节码
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> daoInterfaceClass) {
        return (T) Proxy.newProxyInstance(
                daoInterfaceClass.getClassLoader(),
                new Class[]{daoInterfaceClass},
                new MapperProxy(cfg.getMappers(), conn));
    }

    /**
     * 用来关闭资源
     */
    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
