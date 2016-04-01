package com.cxqm.xiaoerke.common.persistence;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

/**
 * Created by Administrator on 2014/8/26.
 */
public class MyObjectFactory extends DefaultObjectFactory {
    @Override
    public <T> boolean isCollection(Class<T> type) {
        return super.isCollection(type)|| (Page.class.isAssignableFrom(type));
    }

/*
    @Override
    public <T> T create(Class<T> type) {
        return super.create(type);
    }
*/
}
