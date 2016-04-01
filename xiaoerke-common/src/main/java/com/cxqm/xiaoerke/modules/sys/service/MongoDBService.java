package com.cxqm.xiaoerke.modules.sys.service;

import java.util.List;

import com.cxqm.xiaoerke.modules.sys.entity.MongoLog;
import com.mongodb.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public abstract class MongoDBService<T> {
	@Autowired
    protected MongoTemplate mongoTemplate;

	/**
	 * 插入数据
	 * @param entity
	 * @return
	 */
	public abstract int insert(T entity) ;
	
	/**
	 * 批量插入数据
	 * @param entities
	 * @return
	 */
	public abstract int insertByBatch(List<T> entities);


	public abstract WriteResult updateMulti(Query query, Update update);

	public abstract WriteResult upsert(Query query, Update update);

	/**
	 * 删除数据
	 * @param id
	 * @return
	 */
	public abstract int delete(String id) ;
	
	/**
	 * query the count by the condition
	 * 
	 * @param query
	 * @return
	 */
	public abstract long queryCount(Query query);

	public abstract List<T> queryList(Query query);

	public abstract List<T> queryListDistinct(Query query,String key);

	public abstract T  findAndRemove(Query query);

	public abstract List<String> queryStringListDistinct(Query query,String key);
	/**
	 * query the count by the condition
	 * 
	 * @param
	 * @return
	 */
	public abstract long mapReduce(String map, String reduce);
	
    /**
     * 保存一个对象
     *
     * @param t
     * @return
     */
    public void save(T t){
        this.mongoTemplate.save(t);
    }    
    
    /**
     * 为属性自动注入bean服务
     *
     * @param mongoTemplate
     */
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
}