package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NetworkDeviceIfIp {
    //拥有的接口ip
    private String device_name;
    //ip地址
    private String ipaddr;

    @Override
    public String toString() {
        return "NetworkDeviceIfIp [device_name=" + device_name + ", ipaddr=" + ipaddr + "]";
    }

}
