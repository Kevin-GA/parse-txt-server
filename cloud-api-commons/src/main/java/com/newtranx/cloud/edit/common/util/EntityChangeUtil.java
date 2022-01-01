package com.newtranx.cloud.edit.common.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * dto dao互转工具
 */

public class EntityChangeUtil {

    public static<DAO,DTO> DAO toDAO(Class<DAO> clazz,DTO dto) throws IllegalAccessException, InstantiationException {
        DAO dao = clazz.newInstance();
        BeanUtils.copyProperties(dto,dao);
        return dao;
    }

    public static<DAO,DTO> List<DAO> toDAO(Class<DAO> clazz,List<DTO> dtos) throws IllegalAccessException, InstantiationException {
        List<DAO> daos = new ArrayList<>();
        dtos.forEach(dto ->{
            try {
                daos.add(toDAO(clazz,dto));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
        return daos;
    }


    public static<DAO,DTO> DTO toDTO(Class<DTO> clazz,DAO dao) throws IllegalAccessException, InstantiationException {
        DTO dto = clazz.newInstance();
        BeanUtils.copyProperties(dao,dto);
        return dto;
    }

    public static<DTO,DAO> List<DTO> toDTO(Class<DTO> clazz,List<DAO>daos) throws IllegalAccessException, InstantiationException {
        List<DTO> dtos = new ArrayList<>();
        daos.forEach(dao ->{
            try {
                dtos.add(toDTO(clazz,dao));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
        return dtos;
    }


}
