package com.nfet.icare.pojo;

import java.util.Date;

/**
 * @ClassName:  Token   
 * @Description: 凭证封装类
 * @author: zerods
 * @date:   2017年5月22日 
 * @version V1.0   
 * @Copyright: 2017 www.xxx.com Inc. All rights reserved.
 */
public class Token {
    private Integer id;
    // 接口访问凭证
    private String accessToken;
    // 凭证有效期
    private Integer expiresIn;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}