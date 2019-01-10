package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IpAreaBean {
    private int id;
    private String area;
    private String device;
    private String vlan;
    private String gateway;
    private String mask;
    private String ip_start;
    private String ip_end;
    private int ip_start_long;
    private int ip_end_long;
    private String comment;

    @Override
    public String toString() {
        return "IpAreaBean [area=" + area + ", device=" + device + ", vlan=" + vlan + ", gateway=" + gateway + ", mask="
                + mask + ", ip_start=" + ip_start + ", ip_end=" + ip_end + ", ip_start_long=" + ip_start_long
                + ", ip_end_long=" + ip_end_long + ", comment=" + comment + "]";
    }

}
