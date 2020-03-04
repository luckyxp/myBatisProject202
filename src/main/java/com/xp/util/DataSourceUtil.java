package com.xp.util;

import com.xp.mybatis.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Climb.Xu
 * @date 2020/2/27 0:06
 * 用于创建数据源的工具类
 */
public class DataSourceUtil {
    /**
     * 用来获取数据库连接
     *
     * @param cfg
     * @return
     */
    public static Connection getConnection(Configuration cfg) {
        try {
            Class.forName(cfg.getDriver());
            return DriverManager.getConnection(cfg.getUrl(), cfg.getUserName(), cfg.getPassWord());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
