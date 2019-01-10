package com.company.beans;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class PageIpBean {
    //当前页
    private Integer currentPage;
    //总页数
    private Integer totalPage;
    //总记录
    private Integer totalCount;
    //当前页商品
    private List<IpObject> ipList = new ArrayList<>();
}
