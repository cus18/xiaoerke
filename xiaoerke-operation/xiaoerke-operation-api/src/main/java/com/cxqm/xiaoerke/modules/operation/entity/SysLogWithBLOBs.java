package com.cxqm.xiaoerke.modules.operation.entity;

public class SysLogWithBLOBs extends SysLog {
    private String params;

    private String exception;

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}