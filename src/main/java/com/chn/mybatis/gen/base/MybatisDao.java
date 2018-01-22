package com.chn.mybatis.gen.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public interface MybatisDao<T  extends BaseEntity<PK, T>, PK extends Serializable> {

	T get(final PK id);

	int insert(final T entity);

	int insertBatch(final List<T> list);

	int update(final Map<String, Object> params);

	int remove(final Map<String, Object> params);

	int deleteById(PK id);

	List<T> find(final Map<String, Object> params);

	long count(final Map<String, Object> params);
	
	long countByEntity(final T entity);
	
	int updateByEntity(final T entity);
	
	int removeByEntity(final T entity);
	
	List<T> findByEntity(final T entity);

}
