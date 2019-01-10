package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswdBean {
    private String serial_num;
    private String category;
    private String ip;
    private String user;
    private String passwd;
    private String other_user;
    private String other_passwd;
    private String comment;
    private String owner;

    @Override
    public String toString() {
        return "UserPasswdBean [serial_num=" + serial_num + ", category=" + category + ", ip=" + ip + ", user=" + user
                + ", passwd=" + passwd + ", other_user=" + other_user + ", other_passwd=" + other_passwd + ", comment="
                + comment + ", owner=" + owner + "]";
    }

}
