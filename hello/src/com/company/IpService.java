package com.company;

import com.company.beans.*;

import java.sql.SQLException;
import java.util.List;


public class IpService {
    public static IpDao ipDao = new IpDao();

    /**
     * 获取当页的goods对象
     *
     * @param database databse
     * @return haha
     * @throws SQLException
     */
    public static PageBean getPageBean(Integer currentPage, String database) throws SQLException {
        PageBean pageBean = new PageBean();
        //设置当前页
        pageBean.setCurrentPage(currentPage);
        //有多少记录
        //从数据库中获取
        int totalCount = ipDao.getcount(database).intValue();
        pageBean.setTotalCount(totalCount);
        //一页展示多少记录
        Integer pageCount = 13;

        //总页数
        double ceil = Math.ceil(1.0 * totalCount / pageCount);
        pageBean.setTotalPage((int) ceil);

        //获取分页数据
        List<NetworkInfo> pageData = ipDao.getPageData((currentPage - 1) * pageCount, pageCount, database);
        pageBean.setNetworkList(pageData);
        network2new(pageData);
        return pageBean;
    }

    /**
     * 对象转换
     */
    public static void network2new(List<NetworkInfo> pageData) {
        for (NetworkInfo networkInfo : pageData) {
            networkInfo.setIpLong(IpOperation.getIpFromLong(Long.parseLong(networkInfo.getIpLong())));
        }
    }

    public static PageIpBean getPageIpBean(Integer currentPage, String database, String filter) throws SQLException {
        PageIpBean pageIpBean = new PageIpBean();
        //设置当前页
        pageIpBean.setCurrentPage(currentPage);
        //有多少记录
        //从数据库中获取
        int totalCount = ipDao.get_ip_count(database, filter).intValue();
        pageIpBean.setTotalCount(totalCount);
        //一页展示多少记录
        Integer pageCount = 13;

        //总页数
        double ceil = Math.ceil(1.0 * totalCount / pageCount);
        pageIpBean.setTotalPage((int) ceil);

        //获取分页数据
        List<IpObject> pageData = ipDao.getPageIpData((currentPage - 1) * pageCount, pageCount, database, filter);
        pageIpBean.setIpList(pageData);
        return pageIpBean;
    }

    /**
     * 获取单个IP数据
     *
     * @return
     * @throws SQLException
     */
    public static IpObject getIpData(String database, String serial_num) throws SQLException {
        return ipDao.getIpData(database, serial_num);
    }

    public static void updateIpData(String database, IpObject ipObject) throws SQLException {
        ipDao.updateIpData(database, ipObject);
    }

    /**
     * 获取网段分布信息
     *
     * @param currentPage
     * @param database
     * @param filter
     * @return
     * @throws SQLException
     */
    public static PageBaseBean getPageBeanIpArea(int currentPage, String database, String filter) throws SQLException {
        //如果filter是ip地址的话，将他改为long
        String[] filters = null;
        int method = 0;//0:无搜索,1:IP搜索
        if (null != filter) {
            filters = filter.split("\\s+");
            for (String string : filters) {
                if (IpOperation.isip(string)) {
//					filter = IpOperation.getIpFromString(filter).toString();
                    System.err.println(string + ":是IP");
                    method = 1;
                } else {
                    System.err.println(filter + ":不是IP");
                    method = 0;
                }
            }

        }

        PageBaseBean pageBaseBean = new PageBaseBean();
        //设置当前页
        pageBaseBean.setCurrentPage(currentPage);
        //有多少记录
        //从数据库中获取
        int totalCount = 0;
        if (database.equals("ip_area_all")) {
            //全局查找
            totalCount = ipDao.get_ip_area_all_count(database, filters, method).intValue();
        } else {
            //平常情况
            totalCount = ipDao.get_ip_area_count(database, filters, method).intValue();
        }
        pageBaseBean.setTotalCount(totalCount);
        //一页展示多少记录
        Integer pageCount = 1300000;

        //总页数
        double ceil = Math.ceil(1.0 * totalCount / pageCount);
        pageBaseBean.setTotalPage((int) ceil);

        //获取分页数据
        List<IpAreaBean> pageData = null;
        if (database.equals("ip_area_all")) {
            pageData = ipDao.getPageDataIpAreaAll((currentPage - 1) * pageCount, pageCount, database, filters, method);
        } else {
            //正常情况下
            pageData = ipDao.getPageDataIpArea((currentPage - 1) * pageCount, pageCount, database, filters, method);
        }
        pageBaseBean.setObjectList(pageData);
        return pageBaseBean;
    }

    /*
     *获得单个网段部分信息
     */
    public static IpAreaBean getIpArea(String database, String id) throws SQLException {
        return ipDao.getIpArea(database, id);
    }

    /**
     * 修改单个IP网段分布信息
     *
     * @param database
     * @param ipAreaBean
     * @throws SQLException
     */
    public static void updateIpArea(String database, IpAreaBean ipAreaBean) throws SQLException {
        //生成IP的long值
        Long ip_start_long = IpOperation.getIpFromString(ipAreaBean.getIp_start());
        Long ip_end_long = IpOperation.getIpFromString(ipAreaBean.getIp_end());
        ipAreaBean.setIp_start_long(ip_start_long.intValue());
        ipAreaBean.setIp_end_long(ip_end_long.intValue());
        //执行数据库修改
        ipDao.updateIpData(database, ipAreaBean);
    }


}
