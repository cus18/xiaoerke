package com.cxqm.xiaoerke.common.persistence;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

/**
 * 类似hibernate的Dialect,但只精简出分页部分
 * @author badqiu
 * @author miemiedev
 */
public class MysqlDialect {

    protected MappedStatement mappedStatement;
    protected Page pageBounds;
    protected Object parameterObject;
    protected BoundSql boundSql;

    private String pageSQL;
    private String countSQL;


    public MysqlDialect(MappedStatement mappedStatement, Object parameterObject, Page pageBounds){
        this.mappedStatement = mappedStatement;
        this.parameterObject = parameterObject;
        this.pageBounds = pageBounds;

        init();
    }

    protected void init(){
        boundSql = mappedStatement.getBoundSql(parameterObject);
        StringBuffer bufferSql = new StringBuffer(boundSql.getSql().trim());
        if(bufferSql.lastIndexOf(";") == bufferSql.length()-1){
            bufferSql.deleteCharAt(bufferSql.length()-1);
        }
        String sql = bufferSql.toString();
        pageSQL = sql;
        if(pageBounds.getOrders() != null && !pageBounds.getOrders().isEmpty()){
            pageSQL = getSortString(sql, pageBounds.getOrders());
        }
        if(pageBounds.getOffset() != RowBounds.NO_ROW_OFFSET
                || pageBounds.getLimit() != RowBounds.NO_ROW_LIMIT){
            pageSQL = getLimitString(pageSQL,pageBounds.getOffset(),pageBounds.getLimit());
        }


        countSQL = getCountString(sql);
    }



    public String getPageSQL(){
        return pageSQL;
    }



    public String getCountSQL(){
        return countSQL;
    }


    protected String getLimitString(String sql,int offset, int limit) {
        StringBuffer buffer = new StringBuffer( sql.length()+20 ).append(sql);
        if (offset > 0) {
            buffer.append(" limit ").append(offset).append(",").append(limit);
        } else {
            buffer.append(" limit ").append(limit);
        }
        return buffer.toString();
    }
    /**
     * 将sql转换为总记录数SQL
     * @param sql SQL语句
     * @return 总记录数的sql
     */
    protected String getCountString(String sql){
        return "select count(1) from (" + sql + ") tmp_count";
    }

    /**
     * 将sql转换为带排序的SQL
     * @param sql SQL语句
     * @return 总记录数的sql
     */
    protected String getSortString(String sql, List<Order> orders){
        if(orders == null || orders.isEmpty()){
            return sql;
        }

        StringBuffer buffer = new StringBuffer("select * from (").append(sql).append(") temp_order order by ");
        for(Order order : orders){
            if(order != null){
                buffer.append(order.toString())
                        .append(", ");
            }

        }
        buffer.delete(buffer.length()-2, buffer.length());
        return buffer.toString();
    }

}


