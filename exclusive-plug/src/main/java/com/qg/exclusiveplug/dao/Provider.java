package com.qg.exclusiveplug.dao;

import org.springframework.data.repository.query.Param;

/**
 * @author linxu
 * date 2018/11/13
 */
public class Provider {
    /**
     * SQL创建表
     *
     * @param tableName 表名
     * @return 建表语句
     */
    public static String createTableSql(String tableName) {
        return "CREATE TABLE IF NOT EXISTS " + tableName + "("
                + "`id` int(11) NOT NULL AUTO_INCREMENT,"
                + "`name` varchar(150) NOT NULL COMMENT '电器名称',"
                + "`current` double NOT NULL COMMENT '电流',"
                + "`voltage` double NOT NULL COMMENT '电压',"
                + "`power` double NOT NULL COMMENT '功率',"
                + "`date` varchar(50) NOT NULL,"
                + "`index_num` int(11) NOT NULL COMMENT '串口号',"
                + "`power_factor` double NOT NULL COMMENT '功率系数',"
                + "`frequency` double NOT NULL COMMENT '频率',"
                + "`cumulative_power` double NOT NULL COMMENT '累计用电量',"
                + " PRIMARY KEY (`id`),"
                + "  KEY `power` (`index_num`,`date`) USING BTREE"
                + ") ENGINE=MyISAM AUTO_INCREMENT=2525620 DEFAULT CHARSET=utf8mb4";
    }

    /**
     * provider
     *
     * @param uSql sql
     * @return uSql
     */
    public String updateTableField(@Param("uSql") String uSql) {
        return uSql;
    }
}
