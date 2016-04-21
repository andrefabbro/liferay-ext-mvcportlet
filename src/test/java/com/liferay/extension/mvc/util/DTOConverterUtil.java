package com.liferay.extension.mvc.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * Utility for get test DTOs in the test suites
 */
public class DTOConverterUtil {

    /**
     * Build a PersonDTO for testing
     *
     * @return PersonDTO
     */
    public static PersonDTO buildPersonDTO() {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName("Test name");
        personDTO.setAge(25);
        personDTO.setBirthDate(new Date());
        personDTO.setSalary(new BigDecimal(123123123));
        return personDTO;
    }

    /**
     * Build a CompanyDTO for testing
     *
     * @return CompanyDTO
     */
    public static CompanyDTO buildCompanyDTO() {
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setName("Test name");
        companyDTO.setTelephones(new ArrayList<>(Arrays.asList(new String[]{"1231231", "4564564"})));
        companyDTO.setId(123465789L);

        return companyDTO;
    }
    
    /**
     * Build a custom Map for testing purpose
     * 
     * @return Map<Integer, String>
     */
    public static Map<Integer, String> buildCustomMap() {
        Map<Integer, String> rs = new HashMap<Integer, String>();

        rs.put(1, "Brazil");
        rs.put(2, "Panama");
        rs.put(3, "Colombia");
        rs.put(4, "Venezuela");
        rs.put(5, "Argentina");
        rs.put(6, "Chile");
        
        return rs;
    }
    
    /**
     * Build a custom Map in JSON format for testing purpose
     * 
     * @return Map<Integer, String>
     */
    public static String buildCustomMapJSON() {
        return new Gson().toJson(buildCustomMap());
    }

    /**
     * Build a CompanyDTO for testing
     *
     * @return CompanyDTO in String (JSON) representation
     */
    public static String buildCompanyJSON() {
        return new Gson().toJson(buildCompanyDTO());
    }

    /**
     * Build a PersonDTO for testing
     *
     * @return PersonDTO in String (JSON) representation
     */
    public static String buildPersonJSON() {
        return new Gson().toJson(buildPersonDTO());
    }
}
