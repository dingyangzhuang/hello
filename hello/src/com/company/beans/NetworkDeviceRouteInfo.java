package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NetworkDeviceRouteInfo {
    private String device_name;
    private String network_ip;
    private String mask;
    private int ip_start;
    private int ip_end;
    private String gateway;
    private String type;

    //other(1) 静态路由
    //reject(2)：当作不可达而丢弃消息的路由。在某些协议里作为正确聚合路由的一种方法。
    //local(3)：下一跳是最终目的的路由。
    //remote(4)：下一跳不是最终目的的路由。
    //ospf(13) :ospf
    @Override
    public String toString() {
        return "NetworkDeviceRouteInfo [device_name=" + device_name + ", network_ip=" + network_ip + ", mask=" + mask
                + ", ip_start=" + ip_start + ", ip_end=" + ip_end + ", gateway=" + gateway + ", type=" + type + "]";
    }

}
