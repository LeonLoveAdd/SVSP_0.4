package com.jdlink.mapper;

import com.jdlink.domain.Page;
import com.jdlink.domain.Produce.IngredientsIn;

import java.util.List;

public interface IngredientsMapper {
    ///入库单///
    int countInById(String id);
    IngredientsIn getInById(String id);
    void addIn(IngredientsIn ingredientsIn);
    List<IngredientsIn> listPageIn(Page page);
    int countIn();
    int searchInCount(IngredientsIn ingredientsIn);
    List<IngredientsIn> searchIn(IngredientsIn ingredientsIn);
    void invalidIn(String id);
}