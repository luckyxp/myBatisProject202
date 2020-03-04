package com.xp.mybatis.sqlsession;

import com.xp.mybatis.cfg.Configuration;
import com.xp.mybatis.sqlsession.defaults.DefalultSqlSessionFactory;
import com.xp.util.XMLConfigBuilder;

import java.io.InputStream;

/**
 * @author Climb.Xu
 * @date 2020/2/26 21:26
 * 用于创建一个SqlSessionFactory对象
 */
public class SqlSessionFactoryBuilder {
    /**
     * 根据参数的字节输入流来构建一个SqlSessionFactory工厂
     *
     * @param config
     * @return
     */
    public SqlSessionFactory build(InputStream config) {
        Configuration cfg = XMLConfigBuilder.loadConfiguration(config);
        return new DefalultSqlSessionFactory(cfg);
    }
}
