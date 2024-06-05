package com.zw.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 用于映射数据库行的数据
 *
 * @param <T>
 * @author xznzz
 */
public interface RowMapper<T> {

    /**
     * 将数据库行映射为对象
     *
     * @param rs 数据库结果集
     * @return 对象
     * @throws SQLException SQL异常
     */
    T mapper(ResultSet rs) throws SQLException;

}
