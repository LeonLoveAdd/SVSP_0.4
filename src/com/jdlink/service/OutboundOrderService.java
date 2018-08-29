package com.jdlink.service;

import com.jdlink.domain.Inventory.OutboundOrder;

import java.util.List;

public interface OutboundOrderService {
    List<String> check();
    void updateMaterialRequisitionOrderCheck1(OutboundOrder outboundOrder);
    List<OutboundOrder>loadOutBoundList();
    int total();
    List<OutboundOrder> getByOutBoundOrderId(String outboundOrderId);
    void  updateOutBoundOrder(OutboundOrder outboundOrder);
    List<OutboundOrder> getOutBoundOrderList();
}
