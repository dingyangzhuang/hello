package com.company;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author 唐学俊
 * @version 2017年12月6日上午10:56:48
 */

public class DruidConnection {
    private static Properties properties = null;
    private static DataSource dataSource = null;
    private volatile static DruidConnection instatce = null;

    static {
        try {
            properties = new Properties();
            // 1.加载properties文件
//            String path = DruidConnection.class.getClassLoader().getResource("resource/druid.properties").getPath();
//            path=java.net.URLDecoder.decode(path, "UTF-8");
//            System.out.println(path);
//            FileInputStream in = new FileInputStream(new File(path));
            InputStream in = DruidConnection.class.getResourceAsStream("druid.properties");
            System.out.println("读取配置文件成功！");
            // 2.加载输入流
            properties.load(in);
            System.out.println("加载配置文件成功！");
            System.out.println(properties.getProperty("driverClassName"));

            // 3.获取数据源
            dataSource = getDatasource();
            System.out.println("获取数据源成功！");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Connection connection = null;


    //私有构造函数,防止实例化对象
    private DruidConnection() {

    }

    /**
     * 用简单单例模式确保只返回一个链接对象
     *
     * @return
     */
    public static DruidConnection getInstace() {
        if (instatce == null) {
            synchronized (DruidConnection.class) {
                if (instatce == null) {
                    instatce = new DruidConnection();
                }
            }
        }
        return instatce;
    }

    // 加载数据源
    private static DataSource getDatasource() {
        DataSource source = null;
        try {
            source = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    // 返回一个数据源
    public DataSource getDataSource() {
        return dataSource;
    }

    // 返回一个链接
    public Connection getConnection() {
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
