package com.jdlink.domain.Inventory;

import com.jdlink.domain.CheckState;
import com.jdlink.domain.Client;
import com.jdlink.domain.Salesman;
import com.jdlink.domain.Wastes;

import java.util.Date;
import java.util.List;

/*出库单*/
public class OutboundOrder {
    /*领料单对列表
    * 1:N
    * */
    private MaterialRequisitionOrder  materialRequisitionOrder;
    //private List<MaterialRequisitionOrder> materialRequisitionOrderList;

//    public List<MaterialRequisitionOrder> getMaterialRequisitionOrderList() {
//        return materialRequisitionOrderList;
//    }
//
//    public void setMaterialRequisitionOrderList(List<MaterialRequisitionOrder> materialRequisitionOrderList) {
//        this.materialRequisitionOrderList = materialRequisitionOrderList;
//    }

    /*出库单编号*/
   private String outboundOrderId;
    /*出库日期*/
    private Date outboundDate;
    /*制单人*/
    private String creator;
    /*审核人*/
    private String auditor;
    /*出库类型*/
    private OutboundOrder outboundOrder;
    /*转移联单号*/
    private String transferDraftId;
    /*部门*/
   // private String departmentId;
    /*业务员 来自产废单位的业务员*/
    private Salesman salesman;
    /*单据状态*/
    private CheckState checkState;
    /*记录状态*/
    private  RecordState recordState;
    private Wastes wastes;
    private Client client;
   /*出库类别*/
    private BoundType boundType;
    /*部门*/
    private String departmentName;
     /*次生危废对象*/
    WasteInventory wasteInventory;
    /*出库数量*/
    /*仓库*/
    private WareHouse wareHouse;

    public WareHouse getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }

    private float outboundNumber;

    public float getOutboundNumber() {
        return outboundNumber;
    }

    public void setOutboundNumber(float outboundNumber) {
        this.outboundNumber = outboundNumber;
    }

    public WasteInventory getWasteInventory() {
        return wasteInventory;
    }

    public void setWasteInventory(WasteInventory wasteInventory) {
        this.wasteInventory = wasteInventory;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BoundType getBoundType() {
        return boundType;
    }

    public void setBoundType(BoundType boundType) {
        this.boundType = boundType;
    }

    public Client getClient() {

        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Wastes getWastes() {
        return wastes;
    }

    public void setWastes(Wastes wastes) {
        this.wastes = wastes;
    }

    public MaterialRequisitionOrder getMaterialRequisitionOrder() {
        return materialRequisitionOrder;
    }

    public void setMaterialRequisitionOrder(MaterialRequisitionOrder materialRequisitionOrder) {
        this.materialRequisitionOrder = materialRequisitionOrder;
    }



    public String getOutboundOrderId() {
        return outboundOrderId;
    }

    public void setOutboundOrderId(String outboundOrderId) {
        this.outboundOrderId = outboundOrderId;
    }

    public Date getOutboundDate() {
        return outboundDate;
    }

    public void setOutboundDate(Date outboundDate) {
        this.outboundDate = outboundDate;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public OutboundOrder getOutboundOrder() {
        return outboundOrder;
    }

    public void setOutboundOrder(OutboundOrder outboundOrder) {
        this.outboundOrder = outboundOrder;
    }

    public String getTransferDraftId() {
        return transferDraftId;
    }

    public void setTransferDraftId(String transferDraftId) {
        this.transferDraftId = transferDraftId;
    }

//    public String getDepartmentId() {
//        return departmentId;
//    }
//
//    public void setDepartmentId(String departmentId) {
//        this.departmentId = departmentId;
//    }

    public Salesman getSalesman() {
        return salesman;
    }

    public void setSalesman(Salesman salesman) {
        this.salesman = salesman;
    }

    public CheckState getCheckState() {
        return checkState;
    }

    public void setCheckState(CheckState checkState) {
        this.checkState = checkState;
    }

    public RecordState getRecordState() {
        return recordState;
    }

    public void setRecordState(RecordState recordState) {
        this.recordState = recordState;
    }

    @Override
    public String toString() {
        return "OutboundOrder{" +
                "materialRequisitionOrder=" + materialRequisitionOrder +
                ", outboundOrderId='" + outboundOrderId + '\'' +
                ", outboundDate=" + outboundDate +
                ", creator='" + creator + '\'' +
                ", auditor='" + auditor + '\'' +
                ", outboundOrder=" + outboundOrder +
                ", transferDraftId='" + transferDraftId + '\'' +
                ", salesman=" + salesman +
                ", checkState=" + checkState +
                ", recordState=" + recordState +
                ", wastes=" + wastes +
                ", client=" + client +
                ", boundType=" + boundType +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
