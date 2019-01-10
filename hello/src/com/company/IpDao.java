package com.company;

import com.company.beans.IpAreaBean;
import com.company.beans.IpObject;
import com.company.beans.NetworkInfo;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IpDao {
    public static QueryRunner qRunner = new QueryRunner(DruidUtils.getDatasource());
    public static Map<String, String> databaseMap = new HashMap<>();// 存储数据库和字符串的对应关系

    public IpDao() {
        databaseMap.put("network_ipmi", "new_ip_table_ipmi");
        databaseMap.put("network_base", "new_ip_table_base");
        databaseMap.put("network_fabric", "new_ip_table_fabric");
        databaseMap.put("ip_area_base", "ip_area_bean_base");
        databaseMap.put("ip_area_fabric", "ip_area_bean_fabric");
        databaseMap.put("ip_area_testandterm", "ip_area_bean_testandterm");
    }

    // 获取单个IP数据
    public static IpObject getIpData(String database, String serial_num) throws SQLException {
        String sql = "select * from " + databaseMap.get(database) + " where serial_num=" + serial_num;
        System.out.println(sql);
        return qRunner.query(sql, new BeanHandler<IpObject>(IpObject.class));
    }

    // 获取网段总数
    public Long getcount(String database) throws SQLException {
        String sql = "select count(*) " + "from " + "(SELECT vlan,vlan_desc,min(ipLong) ipLong,count(*) total FROM "
                + databaseMap.get(database) + " group by vlan,vlan_desc) t1, "
                + "(SELECT vlan,count(ip_desc) unused FROM " + databaseMap.get(database)
                + " where ip_desc like '%预留%' or  ip_desc='未使用' group by vlan) t2 " + "where t1.vlan=t2.vlan";
        Long count = qRunner.query(sql, new ScalarHandler<Long>());
        return count;
    }

    /**
     * 获取分页数据
     *
     * @param pageCount
     * @return
     * @throws SQLException
     */
    public List<NetworkInfo> getPageData(Integer index, Integer pageCount, String database) throws SQLException {
        System.out.println(databaseMap.get(database));
        String sql = "select t1.*,t2.unused " + "from "
                + "(SELECT vlan,vlan_desc,min(ipLong) ipLong,count(*) total FROM " + databaseMap.get(database)
                + " group by vlan,vlan_desc) t1, " + "(SELECT vlan,count(ip_desc) unused FROM "
                + databaseMap.get(database) + " where ip_desc like '%预留%' or  ip_desc='未使用' group by vlan) t2 "
                + "where t1.vlan=t2.vlan limit ?,?";
        return qRunner.query(sql, new BeanListHandler<NetworkInfo>(NetworkInfo.class), index, pageCount);
    }

    /*
     * 获取分页数据
     *
     * @param pageCount
     *
     * @param i
     *
     * @return
     *
     * @throws SQLException
     */
    public List<IpObject> getPageIpData(Integer index, Integer pageCount, String database, String filter)
            throws SQLException {
        System.out.println(databaseMap.get(database));
        String sql = "select * from " + databaseMap.get(database) + " where 1=1";
        if (null != filter && !"null".equals(filter)) {
            sql = sql + " and vlan=" + filter + " limit ?,?";
        } else {
            sql = sql + " limit ?,?";
        }
        return qRunner.query(sql, new BeanListHandler<IpObject>(IpObject.class), index, pageCount);
    }

    // 获取网段总数
    public Long get_ip_count(String database, String filter) throws SQLException {
        String sql = "select count(*) from " + databaseMap.get(database) + " where 1=1";
        // 先只判断vlan，后面有需要再改
        if (null != filter && !"null".equals(filter)) {
            sql = sql + " and vlan=" + filter;
        }
        Long count = qRunner.query(sql, new ScalarHandler<Long>());
        return count;
    }

    // 修改单个IP数据
    public void updateIpData(String database, IpObject ipObject) throws SQLException {
        String sql = "UPDATE " + databaseMap.get(database) + " SET ip_desc=?,comment=? where serial_num=?";
        qRunner.update(sql, ipObject.getIp_desc(), ipObject.getComment(), ipObject.getSerial_num());

    }

    // 查看网段分布信息
    public List<IpAreaBean> getPageDataIpArea(int index, Integer pageCount, String database, String[] filters,
                                              int method) throws SQLException {
        System.out.println("数据库：" + databaseMap.get(database));
        String sql = "select * from " + databaseMap.get(database) + " where 1=1";
        if (null != filters && !"null".equals(filters[0])) {
            // 0：默认 1：IP搜索
            if (method == 0) {
                sql = sql + " and vlan=" + filters + " limit ?,?";
            }
            if (method == 1) {
                List<IpAreaBean> list = new ArrayList<>();
                for (String filter : filters) {
                    // 这里的try catch，害怕多个参数的时候，未查找到数据，导致异常，其中一个有异常
                    try {
                        String filter_long = IpOperation.getIpFromString(filter).toString();
                        // sql=sql+" and (ip_start_long<="+filter+" and
                        // ip_end_long>="+filter+") or "
                        // + "(ip_start_long<="+filter+" and
                        // ip_end_long>="+filter+")";
                        String sql_tmp = sql + " and ip_start_long<=" + filter_long + " and ip_end_long>="
                                + filter_long;
                        IpAreaBean query = qRunner.query(sql_tmp, new BeanHandler<IpAreaBean>(IpAreaBean.class));
                        query.setComment(filter);
                        list.add(query);
                    } catch (Exception e) {
                    }

                }
                if (list.size() == 0) {
                    throw new SQLException("未查到数据");
                }
                return list;
            }
        } else {
            sql = sql + " limit ?,?";
        }
        System.out.println(sql);
        return qRunner.query(sql, new BeanListHandler<IpAreaBean>(IpAreaBean.class), index, pageCount);
    }

    // 获取网段分布信息总数
    public Long get_ip_area_count(String database, String[] filters, int method) throws SQLException {
        String sql = "select count(*) from " + databaseMap.get(database) + " where 1=1";
        // 先只判断vlan，后面有需要再改
        if (null != filters && !"null".equals(filters[0])) {
            // 0：默认 1：IP搜索
            if (method == 0) {
                sql = sql + " and vlan=" + filters[0];
            }
            if (method == 1) {
                for (String filter : filters) {
                    filter = IpOperation.getIpFromString(filter).toString();
                    sql = sql + " and (ip_start_long<=" + filter + " and ip_end_long>=" + filter + ") or "
                            + "(ip_start_long<=" + filter + " and ip_end_long>=" + filter + ")";
                }
                System.out.println(sql);
            }
        }
        Long count = qRunner.query(sql, new ScalarHandler<Long>());
        return count;
    }

    /**
     * 获得单个网段信息
     *
     * @param database
     * @param id
     * @return
     * @throws SQLException
     */
    public IpAreaBean getIpArea(String database, String id) throws SQLException {
        String sql = "select * from " + databaseMap.get(database) + " where id=" + id;
        System.out.println(sql);
        return qRunner.query(sql, new BeanHandler<IpAreaBean>(IpAreaBean.class));
    }

    /**
     * 修改单个IP网段分布信息
     *
     * @param database
     * @param ipAreaBean
     * @throws SQLException
     */
    public void updateIpData(String database, IpAreaBean ipAreaBean) throws SQLException {
        String sql = "UPDATE " + databaseMap.get(database) + " SET area=?,device=?,vlan=?,"
                + "gateway=?,mask=?,ip_start=?,ip_end=?,ip_start_long=?,ip_end_long=?,comment=? where id=?";
        qRunner.update(sql, ipAreaBean.getArea(), ipAreaBean.getDevice(), ipAreaBean.getVlan(), ipAreaBean.getGateway(),
                ipAreaBean.getMask(), ipAreaBean.getIp_start(), ipAreaBean.getIp_end(), ipAreaBean.getIp_start_long(),
                ipAreaBean.getIp_end_long(), ipAreaBean.getComment(), ipAreaBean.getId());

    }

    public Integer get_ip_area_all_count(String database, String[] filters, int method) {
        String sql1 = "select count(*) from " + databaseMap.get("ip_area_base") + " where 1=1";
        String sql2 = "select count(*) from " + databaseMap.get("ip_area_fabric") + " where 1=1";
        String sql3 = "select count(*) from " + databaseMap.get("ip_area_testandterm") + " where 1=1";
        String[] sqls = {sql1, sql2, sql3};
        Long count = 0L;
        for (String sql : sqls) {
            if (null != filters && !"null".equals(filters[0])) {
                // 0：默认 1：IP搜索
                if (method == 0) {
                    sql = sql + " and vlan=" + filters + " limit ?,?";
                }
                if (method == 1) {
                    for (String filter : filters) {
                        // 这里的try catch，害怕多个参数的时候，未查找到数据，导致异常，其中一个有异常
                        try {
                            String filter_long = IpOperation.getIpFromString(filter).toString();
                            // sql=sql+" and (ip_start_long<="+filter+" and
                            // ip_end_long>="+filter+") or "
                            // + "(ip_start_long<="+filter+" and
                            // ip_end_long>="+filter+")";
                            String sql_tmp = sql + " and ip_start_long<=" + filter_long + " and ip_end_long>="
                                    + filter_long;
                            long tmp = qRunner.query(sql_tmp, new ScalarHandler<Long>());
                            count = count + tmp;
                        } catch (Exception e) {
                        }
                    }
                }
            } else {
                sql = sql + " limit ?,?";
            }
            System.out.println(sql);
        }
        return count.intValue();
    }

    public List<IpAreaBean> getPageDataIpAreaAll(int index, Integer pageCount, String database, String[] filters,
                                                 int method) throws SQLException {
        System.out.println("数据库：" + databaseMap.get(database));
        String sql1 = "select * from " + databaseMap.get("ip_area_base") + " where 1=1";
        String sql2 = "select * from " + databaseMap.get("ip_area_fabric") + " where 1=1";
        String sql3 = "select * from " + databaseMap.get("ip_area_testandterm") + " where 1=1";
        String[] sqls = {sql1, sql2, sql3};
        List<IpAreaBean> list = new ArrayList<>();
        for (String sql : sqls) {
            if (null != filters && !"null".equals(filters[0])) {
                // 0：默认 1：IP搜索
                if (method == 0) {
                    sql = sql + " and vlan=" + filters + " limit ?,?";
                }
                if (method == 1) {
                    for (String filter : filters) {
                        // 这里的try catch，害怕多个参数的时候，未查找到数据，导致异常，其中一个有异常
                        try {
                            String filter_long = IpOperation.getIpFromString(filter).toString();
                            // sql=sql+" and (ip_start_long<="+filter+" and
                            // ip_end_long>="+filter+") or "
                            // + "(ip_start_long<="+filter+" and
                            // ip_end_long>="+filter+")";
                            String sql_tmp = sql + " and ip_start_long<=" + filter_long + " and ip_end_long>="
                                    + filter_long;
                            IpAreaBean query = qRunner.query(sql_tmp, new BeanHandler<IpAreaBean>(IpAreaBean.class));
                            query.setComment(filter);
                            list.add(query);
                        } catch (Exception e) {
                        }
                    }
                }
            } else {
                sql = sql + " limit ?,?";
            }
            System.out.println(sql);
        }
        if (list.size() == 0) {
            throw new SQLException("未查到数据");
        }
        if (list.size() > pageCount + index) {
            return list.subList(index, index + pageCount);
        } else {
            System.out.println("list");
            return list.subList(index, list.size());
        }
    }

}
