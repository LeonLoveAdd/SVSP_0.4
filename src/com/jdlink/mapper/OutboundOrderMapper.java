package com.jdlink.mapper;

import com.jdlink.domain.Inventory.OutboundOrder;
import com.jdlink.domain.Produce.HandleCategory;

import java.util.Date;
import java.util.List;

public interface OutboundOrderMapper {
    List<String> check();

    void updateMaterialRequisitionOrderCheck1(OutboundOrder outboundOrder);

    List<OutboundOrder> loadOutBoundList();

    int total();

    int searchCount(OutboundOrder outboundOrder);

    List<OutboundOrder> getByOutBoundOrderId(String outboundOrderId);

    void updateOutBoundOrder(String outboundOrderId);

    List<OutboundOrder> getOutBoundOrderList();

    List<OutboundOrder> getById(String id);

    List<OutboundOrder> search(OutboundOrder outboundOrder);

    OutboundOrder getOutBoundByMId(String materialRequisitionId);

    void addSecondary(OutboundOrder outboundOrder);

    OutboundOrder getOutBoundByInId(String inboundOrderId);

    void updateSecOutBoundOrder(OutboundOrder outboundOrder);

    void upWastesInventoryNumber(OutboundOrder outboundOrder);
    HandleCategory getHandelCategoryById(String outboundNumber);
    void upHandelCategoryById(String outboundOrderId,String  handelCategory);
    void  updateSecondart(OutboundOrder outboundOrder);
     List<OutboundOrder>  getOutBoundByRange(Date startDate, Date endDate);
    List<OutboundOrder>  getOutBoundByDateRangeAndEquipment(Date startDate, Date endDate,String equipment);
}
