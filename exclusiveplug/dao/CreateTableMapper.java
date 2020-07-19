package com.qg.exclusiveplug.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.data.repository.query.Param;

/**
 * @author linxu
 * @date 2018/11/13
 */
@Mapper
public interface CreateTableMapper {
    /**
     * create table
     *
     * @param uSql
     */
    @UpdateProvider(type = Provider.class, method = "updateTableField")
    void updateTableField(@Param("uSql") String uSql);
}
