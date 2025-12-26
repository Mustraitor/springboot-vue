package edu.friday;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidStatManagerFacade;

import java.sql.Connection;

public class DruidTest {

    public static void main(String[] args) throws Exception {

        // 1. 创建 Druid 数据源
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:mysql://localhost:3306/test?useSSL=false&serverTimezone=Asia/Shanghai");
        ds.setUsername("root");
        ds.setPassword("123456");
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // 可选：打印 Druid 的初始化状态
        System.out.println("== Druid DataSource 初始化完成 ==");
        System.out.println("InitialSize: " + ds.getInitialSize());
        System.out.println("MaxActive: " + ds.getMaxActive());
        System.out.println("Url: " + ds.getUrl());

        // 2. 获取一个连接，验证 Druid 工作是否正常
        Connection conn = ds.getConnection();
        System.out.println("== 成功获取连接 ==");
        System.out.println("Connection: " + conn);

        // 3. 关闭连接（Druid 会放回连接池）
        conn.close();
        System.out.println("== 连接已关闭（已放回 Druid 连接池） ==");

        // 4. 打印当前 Druid 的监控信息
        System.out.println("== Druid 监控信息 ==");
        System.out.println(
                DruidStatManagerFacade.getInstance().getDataSourceStatDataList()
        );

        ds.close();
    }
}
