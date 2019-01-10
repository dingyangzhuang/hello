package com.company;

import com.company.beans.IpAreaBean;
import com.company.beans.PageBaseBean;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        // write your code here
        /**
         * 1.读取文件，获取IP列表
         * 2.登录数据库，查看对应IP的所在的设备
         * 3.使用ssh登录设备进行ping操作
         */
        try {
            //FileInputStream file = new FileInputStream("ip.list");
            InputStreamReader in = new InputStreamReader(Main.class.getResourceAsStream("ip.list"));
            BufferedReader br = new BufferedReader(in);
            String line;
            StringBuffer filter = new StringBuffer();
            while (null != (line = br.readLine())) {
                filter.append(line + " ");

            }
            PageBaseBean ip_area_all = IpService.getPageBeanIpArea(1, "ip_area_all", filter.toString());
            List list = ip_area_all.getObjectList();
            for (Object o : list) {
                String gateway = ((IpAreaBean) o).getGateway();//网络设备IP
                String searchIp = ((IpAreaBean) o).getComment();//需要ping测试的IP
                System.out.println(gateway + ":" + searchIp);
                /*登录设备进行ping测试*/
                String cmd[] = {"ping -c 1 " + searchIp};
                boolean expect = new Shell(gateway, 22, "dingyangzhuang", "yang1230zhua", cmd).expect();
                if (!expect) {
                    boolean expect1 = new Shell(gateway, 22, "dingyangzhuang", "Yang1230zhua", cmd).expect();
                    if (!expect1) {
                        boolean expect2 = new Shell(gateway, 22, "dingyangzhuang", "Yang@1230zhua", cmd).expect();
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
