package com.company.beans;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Integer uid;
    private String username;
    private String passwd;
    private String udesc;

    @Override
    public String toString() {
        return "User [uid=" + uid + ", username=" + username + ", passwd=" + passwd + ", udesc=" + udesc + "]";
    }

}
