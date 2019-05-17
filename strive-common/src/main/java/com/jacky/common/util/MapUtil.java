package com.jacky.common.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
public class MapUtil {

    public static List toList(ResultSet rs) {
        try {

            List list = new ArrayList();
            // 获取键名
            ResultSetMetaData md = rs.getMetaData();
            // 获取行的数量
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                // 声明Map
                Map rowData = new HashMap(columnCount);
                for (int i = 1; i <= columnCount; i++) {
                    // 获取键名及值
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
            return list;
        } catch (Exception ex) {
            LogUtil.error(ex);
            return null;
        }
    }
}
