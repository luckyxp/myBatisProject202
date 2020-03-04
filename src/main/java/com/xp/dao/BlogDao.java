package com.xp.dao;

import com.xp.mybatis.annotations.Select;
import com.xp.pojo.Blog;

import java.util.List;

public interface BlogDao {
    @Select("select id,title,content,create_time as createTime,type,author_id as authorId from blog;")
    List<Blog> getAll();
}
