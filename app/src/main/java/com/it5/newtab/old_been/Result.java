package com.it5.newtab.old_been;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

/**
 * 数据操作结果实体类
 * Created by IT5 on 2016/10/27.
 */
@SuppressWarnings("serial")
@XStreamAlias("result")
public class Result implements Serializable{

    @XStreamAlias("errorCode")
    private int errorCode;

    @XStreamAlias("errorMessage")
    private String errorMessage;

    public boolean OK() {
        return errorCode == 1;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
