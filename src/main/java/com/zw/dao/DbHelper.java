package com.zw.dao;

import com.zw.mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供数据库连接和基本操作方法的帮助类。
 * 该类封装了数据库连接的建立、SQL语句的执行以及资源的释放等通用功能，
 * 用于简化数据访问层的编码工作。
 * <p>
 * 子类可以通过继承该类来获得数据库操作的能力，同时可以根据需要覆盖或添加特定的方法。
 * <p>
 * 注意：该类不处理事务，如果需要事务控制，应该在服务层进行管理。
 *
 * @author zhongwang
 * @version 1.0
 */
public class DbHelper {

    static final String URL = "jdbc:mysql://127.0.0.1:3306/exercisedatabase?serverTimezone=UTC";
    static final String USERNAME = "root";
    static final String PASSWORD = "root";
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private Connection connection;
    private PreparedStatement prepareStatement;

    /**
     * 获取数据库连接
     */
    private void getConnection() {
        // 加载驱动（反射）
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("获取驱动失败，请确认是否正常导入MySQL驱动");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("获取数据库连接失败，请确认连接地址以及账户或者密码是否正确");
        }
    }

    /**
     * 执行数据库的增、删、改
     *
     * @param sql     数据库执行语句
     * @param objects 增删改的参数
     * @return 影响行数
     */
    public int executeUpdate(String sql, Object... objects) {
        System.out.println("执行的SQL：" + sql);
        this.getConnection();
        try {
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                System.out.println("参数（" + (i + 1) + "）:" + objects[i]);
                prepareStatement.setObject(i + 1, objects[i]);
            }
            return prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("执行数据库的增、删、改失败，请确认执行的SQL语句");
        } finally {
            this.close(null);
        }
        return 0;
    }

    /**
     * 执行数据库查询动作
     *
     * @param sql     数据库语句
     * @param objects 查询的参数
     * @return 数据结果集
     */
    public <T> List<T> executeQuery(String sql, RowMapper<T> row, Object... objects) {
        System.out.println("执行的SQL：" + sql);
        this.getConnection();
        ResultSet rs = null;
        try {
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                System.out.println("参数（" + (i + 1) + "）:" + objects[i]);
                prepareStatement.setObject(i + 1, objects[i]);
            }
            rs = prepareStatement.executeQuery();
            List<T> lists = new ArrayList<>();
            while (rs.next()) {
                T t = row.mapper(rs);
                lists.add(t);
            }
            return lists;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("执行数据库的增、删、改失败，请确认执行的SQL语句");
        } finally {
            this.close(null);
        }
        return null;
    }

    /**
     * 执行数据库查询动作
     *
     * @param sql     数据库语句
     * @param objects 查询的参数
     * @return 单个数据结果
     */
    public <T> T executeQueryOne(String sql, RowMapper<T> row, Object... objects) {
        System.out.println("执行的SQL：" + sql);
        this.getConnection();
        ResultSet rs = null;
        try {
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                System.out.println("参数（" + (i + 1) + "）:" + objects[i]);
                prepareStatement.setObject(i + 1, objects[i]);
            }
            rs = prepareStatement.executeQuery();
            while (rs.next()) {
                T t = row.mapper(rs);
                return t;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("执行数据库的增、删、改失败，请确认执行的SQL语句");
        } finally {
            this.close(null);
        }
        return null;
    }


    /**
     * 执行数据库查询动作
     *
     * @param sql     数据库语句
     * @param objects 查询的参数
     * @return 数据结果集
     */
    public ResultSet executeQuery(String sql, Object... objects) {
        System.out.println("执行的SQL：" + sql);
        this.getConnection();
        try {
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                System.out.println("参数（" + (i + 1) + "）:" + objects[i]);
                prepareStatement.setObject(i + 1, objects[i]);
            }
            return prepareStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("执行数据库的增、删、改失败，请确认执行的SQL语句");
        } finally {
            // this.close(null);
        }
        return null;
    }

    /**
     * 关闭数据库
     *
     * @param rs 结果集
     */
    public void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (prepareStatement != null) {
                prepareStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("数据库关闭成功");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库关闭失败");
        }
    }

    /**
     * 通用查询方法
     *
     * @param sql     sql语句
     * @param objects 查询参数
     * @return 结果集中的所有数据
     */
    public List<Object[]> executeQueryCommon(String sql, Object... objects) {
        // 执行SQL，获取结果集
        ResultSet rs = this.executeQuery(sql, objects);
        try {
            // 创建一个空集合用于存储结果集中的数据
            List<Object[]> objects_data = new ArrayList<>();
            // 循环表中的每一行
            while (rs.next()) {
                // 获取每行中有多少个列
                int columnCount = rs.getMetaData().getColumnCount();
                // 有多少个列，数组就有多少个内容
                Object[] object = new Object[columnCount];
                // 循环将列填充到数组中
                for (int i = 0; i < columnCount; i++) {
                    Object obj = rs.getObject(i + 1);
                    object[i] = obj;
                }
                // 将Object数组添加到集合
                objects_data.add(object);
            }
            // 返回结果集中的所有数据
            return objects_data;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(rs);
        }
        return null;
    }

    public int executeCount(String sql, Object... objects) {
        ResultSet rs = null;
        try {
            getConnection();
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                prepareStatement.setObject(i + 1, objects[i]);
            }
            rs = prepareStatement.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(rs);
        }
        return 0;
    }
}
