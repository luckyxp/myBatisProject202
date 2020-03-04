package com.xp.util;

import com.xp.mybatis.cfg.Mapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Climb.Xu
 * @date 2020/2/26 23:29
 * 负责执行SQL语句,并且封装结果集
 */
public class Executor {
    public <E> List<E> getAll(Mapper mapper, Connection connection) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //1.取出mapper中的值
            String queryString = mapper.getQueryString();
            String resultType = mapper.getResultType();
            Class<?> pojoClass = Class.forName(resultType);//通过类加载器,加载类(blog)
            ps = connection.prepareStatement(queryString);
            //2.执行sql语句
            rs = ps.executeQuery();
            //3.遍历结果集,封装到List中
            List<E> list = new ArrayList<E>();
            while (rs.next()) {
                //实例化要封装的实体类对象
                E obj = (E) pojoClass.newInstance();
                //取出结果集的元信息: ResultSetMetaData
                ResultSetMetaData metaData = rs.getMetaData();
                //取出总列数
                int columnCount = metaData.getColumnCount();
                //遍历总列数
                for (int i = 0; i < columnCount; i++) {
                    //获取每一列的名称,列名的序号是从一开始的
                    String columnName = metaData.getColumnName(i + 1);
                    //根据得到的列名获取得到的值
                    if (columnName.contains("_")) {
                        columnName = columnName.replaceAll("(_)(.?)", (char) (columnName.charAt(columnName.indexOf("_") + 1) - 32) + "");
                    }
                    Object columnValue = rs.getObject(columnName);
                    //给obj赋值,使用java机制(借助PropertyDescriptor实现属性封装)
                    PropertyDescriptor pd = new PropertyDescriptor(columnName, pojoClass);
                    //获取他的写入方法
                    Method writeMethod = pd.getWriteMethod();
                    //把获取列的值给对象赋值
                    writeMethod.invoke(obj, columnValue);
                }
                //把赋好值的对象加到list中
                list.add((E) obj);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
