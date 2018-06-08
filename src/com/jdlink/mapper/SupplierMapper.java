package com.jdlink.mapper;

import com.jdlink.domain.Supplier;

import java.util.List;

/**
 * Created by matt on 2018/4/23.
 */
public interface SupplierMapper {

    void add(Supplier supplier);

    void delete(String supplierId);

    Supplier getBySupplierId(String supplier);

    Supplier getByName(String companyName);

    List<Supplier> getByKeyword(String keyword);

    void update(Supplier supplier);

    List<Supplier> list();

    void setCheckStateToSubmit(String supplierId);

    void setCheckStateExamining(String supplierId);

    void setCheckStateFinished(String supplierId);

    int count();

}
