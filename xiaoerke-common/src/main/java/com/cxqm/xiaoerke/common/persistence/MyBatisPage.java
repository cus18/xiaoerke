package com.cxqm.xiaoerke.common.persistence;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Administrator on 2014/8/26.
 */
public class MyBatisPage extends ArrayList {

    private long totalCount;
    private int offset;
    private int limit;

    public MyBatisPage(Collection c, long totalCount, int offset, int limit) {
        super(c);
        this.totalCount = totalCount;
        this.offset = offset;
        this.limit = limit;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
