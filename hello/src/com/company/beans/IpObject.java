package com.company.beans;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IpObject {
    //序号
    private Integer serial_num;
    //所属VLAN
    private String vlan;
    //vlan描述
    private String vlan_desc;
    //ip地址
    private String ip;
    //ip描述
    private String ip_desc;
    //iptolong
    private long ipLong;
    //备注
    private String comment;

    @Override
    public String toString() {
        return "IpObject [serial_num=" + serial_num + ", vlan=" + vlan + ", vlan_desc=" + vlan_desc + ", ip=" + ip
                + ", ip_desc=" + ip_desc + ", ipLong=" + ipLong + ", comment=" + comment + "]";
    }


}

