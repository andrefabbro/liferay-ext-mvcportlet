package com.liferay.extension.mvc.util;

import java.util.List;

/**
 * Created by jamaya on 03/11/2016.
 */
public class CompanyDTO {

    private Long id;
    private String name;
    private List<String> telephones;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTelephones() {
        return telephones;
    }

    public void setTelephones(List<String> telephones) {
        this.telephones = telephones;
    }
}
