package com.xp.mybatis.cfg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Climb.Xu
 * @date 2020/2/26 21:48
 * 自定义mybatis的配置类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
    private String driver;
    private String userName;
    private String passWord;
    private String url;
    private Map<String, Mapper> mappers = new HashMap<String, Mapper>();

    public void setMappers(Map<String, Mapper> mappers) {
        this.mappers.putAll(mappers);//此处需要使用追加的方式
    }
}
