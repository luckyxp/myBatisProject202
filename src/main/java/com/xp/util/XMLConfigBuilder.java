package com.xp.util;

import com.xp.mybatis.annotations.Select;
import com.xp.mybatis.cfg.Configuration;
import com.xp.mybatis.cfg.Mapper;
import com.xp.mybatis.io.Resources;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Climb.Xu
 * @date 2020/2/26 21:39
 * 这是一个解析XML配置文件的工具类
 */
public class XMLConfigBuilder {
    /**
     * 解析主配置文件,把里面的内容填充到DefaultSqlSession所需要的地方
     * 使用技术: dom4j+xpath
     *
     * @param config
     * @return
     */
    public static Configuration loadConfiguration(InputStream config) {
        try {
            //定义封装连接信息的配置对象(mybatis的配置对象)
            Configuration cfg = new Configuration();
            //1.获取SAXReader对象
            SAXReader reader = new SAXReader();
            //2.根据字节输入流获取Document对象
            Document document = reader.read(config);
            //3.获取根节点
            Element rootElement = document.getRootElement();
            //4.使用xpath中选择指定节点的方式,获取所有property节点
            List<Element> propertyElements = rootElement.selectNodes("//property");
            //5.遍历节点
            for (Element propertyElement : propertyElements) {
                //判断节点是连接数据库的哪一部分信息
                //获取name属性的值
                String name = propertyElement.attributeValue("name");
                if ("driver".equals(name)) {
                    //表示此节点为驱动
                    //获取property标签value属性
                    String driver = propertyElement.attributeValue("value");
                    cfg.setDriver(driver);
                }
                if ("url".equals(name)) {
                    //表示此节点为连接字符串
                    //获取property标签value属性
                    String url = propertyElement.attributeValue("value");
                    cfg.setUrl(url);
                }
                if ("username".equals(name)) {
                    //表示此节点为用户名
                    //获取property标签value属性
                    String username = propertyElement.attributeValue("value");
                    cfg.setUserName(username);
                }
                if ("password".equals(name)) {
                    //表示此节点为密码
                    //获取property标签value属性
                    String password = propertyElement.attributeValue("value");
                    cfg.setPassWord(password);
                }
            }
            //6.取出mappers中的所有mapper标签,判断他们使用的是resource还是class属性,以用来判断使用的是注解还是配置的方式
            List<Element> mapperElements = rootElement.selectNodes("//mappers/mapper");
            for (Element mapperElement : mapperElements) {
                Attribute attribute = mapperElement.attribute("resource");
                if (attribute != null) {
                    //resource不为空,说明使用的是配置文件
                    System.out.println("使用的是XML");
                    //取出属性的值
                    String mapperPath = attribute.getValue();//此处获取的应该是mapper配置文件的路径 com/xp/dao/BlogDao.xml
                    //把映射配置文件的内容获取出来,封装成一个map
                    Map<String, Mapper> mappers = loadMapperConfiguration(mapperPath);
                    //给configuration中的mappers赋值
                    cfg.setMappers(mappers);
                }else {
                    //resource为空,说明使用的是注解
                    System.out.println("使用的是annotation");
                    //取出class属性的值
                    String daoClassPath = mapperElement.attributeValue("class");
                    //根据daoClassPath获取封装的必要信息
                    Map<String, Mapper> mappers = loadMapperAnnotation(daoClassPath);
                    //给configuration中的mappers赋值
                    cfg.setMappers(mappers);
                }
            }
            return cfg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据所传入的参数,得到dao中所有被select注解标注的方法.
     * 根据方法名和类名,以及方法上注解value属性的值,组成mapper的必要信息
     * @param daoClassPath
     * @return
     */
    private static Map<String,Mapper> loadMapperAnnotation(String daoClassPath) throws ClassNotFoundException {
        //定义返回对象
        Map<String, Mapper> mappers = new HashMap<String, Mapper>();
        //1.得到dao接口的字节码对象
        Class daoClass = Class.forName(daoClassPath);
        //2.得到dao接口中的方法数组
        Method[] daoMethods = daoClass.getMethods();
        //3.遍历
        for (Method daoMethod : daoMethods) {
            //取出每一个方法,看是否有Select注解
            boolean isAnnotated = daoMethod.isAnnotationPresent(Select.class);
            if (isAnnotated) {
                //创建Mapper对象
                Mapper mapper = new Mapper();
                //取出注解的value属性值
                Select selectAnno = daoMethod.getAnnotation(Select.class);
                String queryString = selectAnno.value();
                mapper.setQueryString(queryString);
                //获取当前方法的返回值,还要求必须带有泛型信息
                Type type = daoMethod.getGenericReturnType();//List<Blog>
                //判断是不是参数化的类型
                if (type instanceof ParameterizedType) {
                    //强转
                    ParameterizedType ptype = (ParameterizedType) type;
                    //得到参数化类型中的实际类型参数
                    Type[] types = ptype.getActualTypeArguments();
                    //取出第一个
                    Class domainClass = (Class) types[0];
                    //获取domainClass类名
                    String resultType = domainClass.getName();
                    //给Mapper赋值
                    mapper.setResultType(resultType);
                }
                //组装key的信息
                //获取方法的名称
                String methodName = daoMethod.getName();
                String className = daoMethod.getDeclaringClass().getName();
                String key = className + "." + methodName;
                //给map赋值
                mappers.put(key, mapper);
            }
        }
        return mappers;
    }
    /**
     * 根据传入的参数,解析XML,并且封装到Map中
     *
     * @param mapperPath 映射配置文件的位置
     * @return map中包含了获取的唯一标识(key是由dao的全限定类名和方法组成)
     * 以及执行所需的必要信息(value是一个Mapper对象,里面存放的是执行的SQL语句,和要封装的实体类的全限定类名
     */
    private static Map<String, Mapper> loadMapperConfiguration(String mapperPath) {
        try {
            InputStream in = null;
            //定义返回值Map
            Map<String, Mapper> mappers = new HashMap<String, Mapper>();
            //1.根据路径获取字节流
            in = Resources.getResourceAsStream(mapperPath);
            //2.根据字节输入流获取Document对象
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            //3.获取根节点
            Element rootElement = document.getRootElement();
            //4.获取根节点namespace属性值
            String namespace = rootElement.attributeValue("namespace");
            //5.获取所有select节点
            List<Element> selectElements = rootElement.selectNodes("//select");
            //6.遍历节点
            for (Element selectElement : selectElements) {
                //获取id的值,和namespace,   组成Map中的key的值 com.xp.dao.BlogDao.getAll
                String id = selectElement.attributeValue("id");
                //获取resultType和SQL语句,封装到Mapper对象中,    组成Map中的value值
                String resultType = selectElement.attributeValue("resultType");
                String queryString = selectElement.getText();
                //组合key
                String key = namespace + "." + id;
                //封装value
                Mapper mapper = new Mapper(queryString, resultType);
                //把key,value存到mappers中
                mappers.put(key, mapper);
            }
            return mappers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
