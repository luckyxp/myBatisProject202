package com.xp.mybatis.sqlsession.defaults;

import com.xp.mybatis.cfg.Configuration;
import com.xp.mybatis.sqlsession.SqlSession;
import com.xp.mybatis.sqlsession.SqlSessionFactory;

import java.sql.Connection;

/**
 * @author Climb.Xu
 * @date 2020/2/26 23:09
 * SqlSessionFactory接口的实现类
 * 一个默认的SqlSessionFactory工厂
 */
public class DefalultSqlSessionFactory implements SqlSessionFactory {
    private Configuration cfg;

    public DefalultSqlSessionFactory(Configuration cfg) {
        this.cfg = cfg;
    }

    /**
     * 用于创建一个新的数据库操作对象SqlSession
     *
     * @return
     */
    public SqlSession openSession() {
        return new DefaultSqlSession(cfg);
    }
}
