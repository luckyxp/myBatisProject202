package com.xp.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {

    private Integer id;
    private String title;
    private String content;
    private Date createTime;
    private String type;
    private Integer authorId;
}
