package com.xp.mybatis.sqlsession;

/**
 * @author Climb.Xu
 * @date 2020/2/26 21:28
 */
public interface SqlSessionFactory {
    /**
     * 用于打开一个新的SqlSession对象
     *
     * @return
     */
    SqlSession openSession();
}
