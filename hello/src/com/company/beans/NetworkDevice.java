package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NetworkDevice {
    //id
    private int id;
    //拥有的接口ip
    private String device_name;
    //管理IP
    private String mgmt_ip;
    //使用的snmp方式
    private String get_method;//目前使用华为

}
