package com.cxqm.xiaoerke.common.persistence;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.BeanWrapper;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

import java.util.List;

/**
 * Created by Administrator on 2014/8/26.
 */
public class MyObjectWrapperFactory extends DefaultObjectWrapperFactory {

    @Override
    public boolean hasWrapperFor(Object object) {
        return object.getClass()==Page.class;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, final Object object) {
        if(object.getClass()==Page.class)
            return new BeanWrapper(metaObject,object){
                @Override
                public <E> void addAll(List<E> list) {
                    if(list instanceof MyBatisPage) {
                        Page page = (Page) object;
                        page.addAll(list);
                        MyBatisPage myBatisPage = (MyBatisPage) list;
                        page.setCount(myBatisPage.getTotalCount());
                        page.setPageSize(myBatisPage.getLimit());
                        page.setPageNo(myBatisPage.getOffset() / myBatisPage.getLimit() + 1);
                    }
                }
            };
        else return super.getWrapperFor(metaObject, object);
    }
}
