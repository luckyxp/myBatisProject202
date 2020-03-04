package com.xp.mybatis.cfg;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Climb.Xu
 * @date 2020/2/26 22:22
 * 用于封装执行的SQL语句和结果类型的全限定类名
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mapper {
    private String queryString;//sql
    private String resultType;//实体类的全限定类名
}
