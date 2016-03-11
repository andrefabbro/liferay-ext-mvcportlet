package com.liferay.extension.mvc.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Test DTO for test JSON methods
 */
public class PersonDTO implements Serializable {

    private String name;
    private Integer age;
    private Date birthDate;
    private BigDecimal salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
}
