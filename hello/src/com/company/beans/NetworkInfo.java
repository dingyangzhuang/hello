package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NetworkInfo {
    private String vlan;
    private String vlan_desc;
    private String ipLong;
    private Integer total;
    private Integer unused;

    @Override
    public String toString() {
        return "NetworkInfo [vlan=" + vlan + ", vlan_desc=" + vlan_desc + ", ipLong=" + ipLong + ", total=" + total
                + ", unused=" + unused + "]";
    }

}
