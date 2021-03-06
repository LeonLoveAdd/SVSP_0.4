package com.jdlink.controller;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.jdlink.domain.*;
import com.jdlink.domain.Dictionary.FormTypeItem;
import com.jdlink.domain.Dictionary.HandleCategoryItem;
import com.jdlink.domain.Dictionary.PackageTypeItem;
import com.jdlink.domain.Produce.*;
import com.jdlink.service.CompatibilityService;
import com.jdlink.service.ThresholdService;
import com.jdlink.service.dictionary.DictionaryService;
import com.jdlink.util.DBUtil;
import com.jdlink.util.DateUtil;
import com.jdlink.util.ImportUtil;
import com.jdlink.util.RandomUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.jdlink.domain.CheckState.ToExamine;
import static com.jdlink.domain.FormType.HalfSolid;
import static com.jdlink.domain.FormType.Liquid;
import static com.jdlink.domain.FormType.Solid;
import static com.jdlink.domain.PackageType.*;

@Controller
public class CompatibilityController {
    @Autowired
    CompatibilityService compatibilityService;
    @Autowired
    ThresholdService thresholdService;
    @Autowired
    DictionaryService dictionaryService;

@RequestMapping("importCompatibilityExcel")
@ResponseBody
public String importCompatibilityExcel(MultipartFile excelFile){
    JSONObject res = new JSONObject();
    String fileName = excelFile.getOriginalFilename();
   List<Object[][]> data= ImportUtil.getInstance().getExcelFileData(excelFile);
    System.out.println(data.size()+"长度");
    try{
        for(int i=0;i<data.size();i++){                 //一页遍历
            //配伍对象
            Calendar cal = Calendar.getInstance();
            //获取年
            String year=String.valueOf(cal.get(Calendar.YEAR));
            //获取月
            String mouth= getMouth(String.valueOf(cal.get(Calendar.MONTH)+1));
            //序列号
            String number = "001";

            //先查看数据库的配伍编号
            List<String> compatibilityIList= compatibilityService.check();
            if(compatibilityIList.size()==0){
                number="001";
            }
            if(compatibilityIList.size()>0) {
                String s= compatibilityIList.get(0);//原字符串
                String s2=s.substring(s.length()-3,s.length());//最后一个3字符
                number=getString3(String.valueOf( Integer.parseInt(s2)+1));
            }
            //配伍编号
            String compatibilityId=year+mouth+number;

            //创建配伍对象
            Compatibility compatibility=new Compatibility();

            compatibility.setCompatibilityId(compatibilityId);//设置配伍的主键

            //每日配比量合计

            float totalDailyAmount=0;

            //周需求合计

            float  weeklyDemandTotalAggregate=0;

            //热值总量

            float calorificSum=0;

            //灰分总量

            float  ashSum=0;

            //水分总量

            float  waterSum=0;

            //氯总量

            float   clSum=0;

            //硫总量

            float  sSum=0;

            //磷总量

            float pSum=0;

            //氟总量

            float fSum=0;

            //酸碱度总量

            float phSum=0;

            int index=0;
            for(int k=1;k<data.get(i).length;k++){
                if (data.get(i)[k][5].toString() != "null") {
                    index++;
                    System.out.println(data.get(i)[k][5].toString()+"1232");
                    weeklyDemandTotalAggregate += Float.parseFloat(data.get(i)[k][5].toString());//周需求总量
                }
            }
            //System.out.println("周需求总量"+weeklyDemandTotalAggregate);//==》正确


            for (int j=1;j<data.get(i).length;j++){
                 if(data.get(i)[j][0].toString()!="null") {


                     CompatibilityItem compatibilityItem = new CompatibilityItem();

                     //配伍主表的序号绑定
                     compatibilityItem.setCompatibilityId(compatibilityId);

                     //第二列是处理类别

                     if (data.get(i)[j][1].toString() != "null") {
                         HandleCategoryItem handleCategoryItem=new HandleCategoryItem();
                         handleCategoryItem.setDataDictionaryItemId(dictionaryService.getdatadictionaryitemIdByName(data.get(i)[j][1].toString(),6));
                         compatibilityItem.setHandleCategoryItem(handleCategoryItem);
//                         HandleCategory handleCategory = (HandleCategory.getHandleCategory(data.get(i)[j][1].toString()));
//                         compatibilityItem.setHandleCategory(handleCategory);//射入
                     }

                     if (data.get(i)[j][1] == "null") {
                         compatibilityItem.setHandleCategoryItem(null);//射入
                     }

                     //第三列是物质形态

                     if (data.get(i)[j][2].toString() != "null") {
                         FormTypeItem formTypeItem=new FormTypeItem();
                         if(data.get(i)[j][2].toString().indexOf("固")!=-1&&data.get(i)[j][2].toString().indexOf("半")==-1){
                             formTypeItem.setDataDictionaryItemId(3);
                         }
                        else if(data.get(i)[j][2].toString().indexOf("液")!=-1){
                             formTypeItem.setDataDictionaryItemId(2);
                         }
                         else if(data.get(i)[j][2].toString().indexOf("半")!=-1){
                             formTypeItem.setDataDictionaryItemId(4);
                         }
                         compatibilityItem.setFormTypeItem(formTypeItem);
                     }
                     if (data.get(i)[j][2] == "null") {
                         compatibilityItem.setFormTypeItem(null);
                     }




                     //第四列是比例==>当天周需求总量/周需求总量总数
//                     if(data.get(i)[j][5].toString()!="null"){
//                         compatibilityItem.setProportion(Float.parseFloat(data.get(i)[j][5].toString())/weeklyDemandTotalAggregate);
////                         System.out.println("比例"+Float.parseFloat(data.get(i)[j][5].toString())/weeklyDemandTotalAggregate);
//                         totalDailyAmount+=Float.parseFloat(data.get(i)[j][5].toString())/weeklyDemandTotalAggregate;
//                     }
//                     else {
//                         compatibilityItem.setProportion(0);
//                     }


                     //第五列是每日配置量==》周需求总量/7
                     if (data.get(i)[j][5].toString() != "null") {
                         compatibilityItem.setDailyRatio(Float.parseFloat(data.get(i)[j][5].toString())/7);
                     }
                     if (data.get(i)[j][5].toString()  == "null")
                         compatibilityItem.setDailyRatio(0);


                     //第六列是周需求总量
                     if (data.get(i)[j][5].toString() != "null") {
//                         System.out.println("周需求总量"+Float.parseFloat(data.get(i)[j][5].toString()));
                         float proportion=Float.parseFloat(data.get(i)[j][5].toString())/weeklyDemandTotalAggregate;
                         compatibilityItem.setProportion(proportion);
                         System.out.println("比例"+proportion);
                         totalDailyAmount+=Float.parseFloat(data.get(i)[j][5].toString())/weeklyDemandTotalAggregate;
                         compatibilityItem.setWeeklyDemandTotal(Float.parseFloat(data.get(i)[j][5].toString()));
                     }
                     if (data.get(i)[j][5].toString() == "null")
                         compatibilityItem.setWeeklyDemandTotal(0);


                     //第七列是热值
                     if (data.get(i)[j][6].toString() != "null") {
                         compatibilityItem.setCalorific(Float.parseFloat(data.get(i)[j][6].toString()));
                         calorificSum += Float.parseFloat(data.get(i)[j][6].toString());
                     }
                     if (data.get(i)[j][6].toString() == "null")
                         compatibilityItem.setCalorific(0);

                     //第八列是灰分
                     if (data.get(i)[j][7] != "null") {
                         compatibilityItem.setAsh(Float.parseFloat(data.get(i)[j][7].toString()));
                         ashSum += Float.parseFloat(data.get(i)[j][7].toString());
                     }
                     if (data.get(i)[j][7] == "null")
                         compatibilityItem.setAsh(0);

                     //第九列是水分

                     if (data.get(i)[j][8] != "null") {
                         compatibilityItem.setWater(Float.parseFloat(data.get(i)[j][8].toString()));
                         waterSum += Float.parseFloat(data.get(i)[j][8].toString());
                     }
                     if (data.get(i)[j][8] == "null")
                         compatibilityItem.setWater(0);

                     //第十列是氯

                     if (data.get(i)[j][9].toString() != "null") {
                         compatibilityItem.setCl(Float.parseFloat(data.get(i)[j][9].toString()));
                         clSum += Float.parseFloat(data.get(i)[j][9].toString());
                     }
                     if (data.get(i)[j][9].toString() == "null")
                         compatibilityItem.setCl(0);

                     //硫 11
                     if (data.get(i)[j][10].toString() != "null") {
                         compatibilityItem.setS(Float.parseFloat(data.get(i)[j][10].toString()));
                         sSum += Float.parseFloat(data.get(i)[j][10].toString());
                     }
                     if (data.get(i)[j][10].toString() == "null")
                         compatibilityItem.setS(0);

                     //磷 12
                     if (data.get(i)[j][11].toString() != "null") {
                         compatibilityItem.setP(Float.parseFloat(data.get(i)[j][11].toString()));
                         pSum += Float.parseFloat(data.get(i)[j][11].toString());
                     }
                     if (data.get(i)[j][11].toString() == "null")
                         compatibilityItem.setP(0);

                     //弗 13
                     if (data.get(i)[j][12].toString() != "null") {
                         compatibilityItem.setF(Float.parseFloat(data.get(i)[j][12].toString()));
                         fSum += Float.parseFloat(data.get(i)[j][12].toString());
                     }
                     if (data.get(i)[j][12].toString() == "null")
                         compatibilityItem.setF(0);

                     //PH 14
                     if (data.get(i)[j][13].toString() != "null") {
                         compatibilityItem.setPh(Float.parseFloat(data.get(i)[j][13].toString()));
                         phSum += Float.parseFloat(data.get(i)[j][13].toString());
                     }
                     if (data.get(i)[j][13].toString() == "null") {
                         compatibilityItem.setPh(0);
                     }

                     compatibilityService.addCompatibilityItem(compatibilityItem);//添加字表
                 }
             }
            //设置每日配比量合计
            compatibility.setTotalDailyAmount(totalDailyAmount);

            //设置周需求总量
            compatibility.setWeeklyDemandTotalAggregate(weeklyDemandTotalAggregate);

            System.out.println("行数:"+index);
            //设置平均热值
            compatibility.setCalorificAvg(calorificSum/index);

            //设置平均灰分
            compatibility.setAshAvg(ashSum/index);

            //设置水分平均
            compatibility.setWaterAvg(waterSum/index);

            //设置cl平均
            compatibility.setClAvg(clSum/index);

            //设置硫平均
            compatibility.setsAvg(sSum/index);

            //设置磷平均
            compatibility.setpAvg(pSum/index);

            //设置氟平均
            compatibility.setfAvg(fSum/index);

            //设置ph平均
            compatibility.setPhAvg(phSum/index);
            //添加主表
            compatibilityService.addCompatibility(compatibility);

        }









        res.put("status", "success");
        res.put("message", "导入配伍周计划成功!");

    }


    catch (Exception e){
        e.printStackTrace();
        res.put("status", "fail");
        res.put("message", "导入配伍周计划失败!");

    }



    return res.toString();

}

    //加载数据
    @RequestMapping("getList1")
    @ResponseBody
    public String getList1(@RequestBody Page page){
        JSONObject res=new JSONObject();
        try {
         List<Compatibility> compatibilityList=compatibilityService.getWeekPlanList(page);
          res.put("compatibilityList",compatibilityList);
            res.put("status", "success");
            res.put("message", "查询成功");
        }
      catch (Exception e){
          e.printStackTrace();
          res.put("status", "fail");
          res.put("message", "查询失败");
      }
        return  res.toString();
    }
   //审批配伍计划
    @RequestMapping("approvalPw")
    @ResponseBody
    public String approvalPw(String pwId,String opinion){
      JSONObject res=new JSONObject();
       try {
        compatibilityService.approval(pwId,opinion);
           res.put("status", "success");
           res.put("message", "审批通过!");
       }
       catch (Exception e){
           e.printStackTrace();
           res.put("status", "fail");
           res.put("message", "审批失败!");
       }
        return res.toString();
    }
    //驳回配伍计划
    @RequestMapping("backPw")
    @ResponseBody
    public String backPw(String compatibilityId,String opinion){
        JSONObject res=new JSONObject();
        try {
            compatibilityService.back(compatibilityId,opinion);
            res.put("status", "success");
            res.put("message", "驳回通过!");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "驳回失败!");
        }
        return res.toString();
    }
    //作废配伍计划
    @RequestMapping("cancelPw")
    @ResponseBody
    public String cancelPw(String compatibilityId){
        JSONObject res=new JSONObject();
        try {
            compatibilityService.cancel(compatibilityId);
            res.put("status", "success");
            res.put("message", "作废成功!");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "作废失败!");
        }
        return res.toString();
    }

    //提交配伍计划
    @RequestMapping("submitPw")
    @ResponseBody
    public String submitPw(String compatibilityId){
        JSONObject res=new JSONObject();
        try {
            compatibilityService.submit(compatibilityId);
            res.put("status", "success");
            res.put("message", "提交成功!");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "提交失败!");
        }
        return res.toString();
    }

    //审批配伍周计划
    @RequestMapping("approvalCompatibility")
    @ResponseBody
    public String approvalCompatibility(String compatibilityId,String opinion){

        JSONObject res=new JSONObject();

        try {
            compatibilityService.approvalCompatibility(compatibilityId,opinion);
            res.put("status", "success");
            res.put("message", "审批成功!");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "审批失败!");
        }

        return  res.toString();

    }

    //通过序号获得信息
    @RequestMapping("getByPwId2")
    @ResponseBody
    public String getByPwId2(String pwId){
        System.out.println(pwId);
        JSONObject res=new JSONObject();
    try {
        Compatibility compatibility=compatibilityService.getByPwId1(pwId);
        res.put("data",compatibility);
        res.put("status", "success");
        res.put("message", "查询成功");
   }
   catch (Exception e){
       e.printStackTrace();
       res.put("status", "fail");
       res.put("message", "查询失败");
   }
        return res.toString();
    }
    //加载下拉框数据
    @RequestMapping("getSelectList")
    @ResponseBody
    public String getSelectList(){
        JSONObject res = new JSONObject();
        // 获取枚举
        //审核状态
        JSONArray checkStateList = JSONArray.fromArray(CheckState.values());
        res.put("checkStateList", checkStateList);
        //形态
        JSONArray formTypeList=JSONArray.fromArray(FormType.values());
        res.put("formTypeList", formTypeList);
        //处理类别
        JSONArray handleCategoryList=JSONArray.fromArray(HandleCategory.values());
        res.put("handleCategoryList", handleCategoryList);
        return res.toString();
    }
 //高级查询
//    @RequestMapping("searchPw")
//    @ResponseBody
//    public String searchPw(@RequestBody Compatibility compatibility){
//        System.out.println(compatibility.getKeyword()+"YH");
//        JSONObject res = new JSONObject();
//        try {
//            List<Compatibility> compatibilityList = compatibilityService.search(compatibility);
//            JSONArray data = JSONArray.fromArray(compatibilityList.toArray(new Compatibility[compatibilityList.size()]));
//            res.put("length",compatibilityList.size());
//            res.put("status", "success");
//            res.put("message", "查询成功");
//            res.put("data", data);
//        } catch (Exception e) {
//            e.printStackTrace();
//            res.put("status", "fail");
//            res.put("message", "查询失败");
//        }
//        return  res.toString();
//    }

    //根据配伍编号获取明细

    @RequestMapping("getWeekById")
    @ResponseBody
    public String getWeekById(String compatibilityId){
        JSONObject res=new JSONObject();
        try{
            List<CompatibilityItem> compatibilityItemList=compatibilityService.getWeekById(compatibilityId);
            res.put("array",compatibilityItemList);
            res.put("status", "success");
            res.put("message", "获取明细成功");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取明细失败");
        }


        return res.toString();
    }

    //更新配伍周计划明细
    @RequestMapping("updateCompatibilityItem")
    @ResponseBody
    public String updateCompatibilityItem(@RequestBody CompatibilityItem compatibilityItem){
        JSONObject res=new JSONObject();

        try {
            compatibilityService.updateCompatibilityItem(compatibilityItem);
            res.put("status", "success");
            res.put("message", "字表更新成功");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "字表更新失败");
        }

        return res.toString();


    }

    //更新配伍周计划主表
    @RequestMapping("updateCompatibility")
    @ResponseBody

    public String updateCompatibility(@RequestBody Compatibility compatibility){
        JSONObject res=new JSONObject();

        try {
         compatibilityService.updateCompatibility(compatibility);
           //更新完删除字表数据
            compatibilityService.deleteCompatibility(compatibility.getCompatibilityId());

            res.put("status", "success");
            res.put("message", "主表更新成功");
        }

        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "主表更新失败");
        }

        return res.toString();
    }


    //配伍周计划高级查询
    @RequestMapping("searchCompatibility")
    @ResponseBody
    public String searchCompatibility(@RequestBody Compatibility compatibility){
        JSONObject res=new JSONObject();
         try {
             List<Compatibility> compatibilityList=compatibilityService.searchCompatibility(compatibility);
             res.put("status", "success");
             res.put("message", "查询成功");
             res.put("compatibilityList", compatibilityList);
         }
         catch (Exception e){
             e.printStackTrace();
             res.put("status", "fail");
             res.put("message", "查询失败");

         }

        return res.toString();
    }


    //配伍周计划高级查询明细
    @RequestMapping("searchCompatibilityItem")
    @ResponseBody
    public String searchCompatibilityItem(@RequestBody CompatibilityItem compatibilityItem){
        JSONObject res=new JSONObject();
        List<Compatibility> compatibilityList=new ArrayList<>();
        try {
            List<String> compatibilityIdList=compatibilityService.searchCompatibilityItem(compatibilityItem);
           for(int i=0;i<compatibilityIdList.size();i++ ){
               compatibilityList.add(compatibilityService.getByCompatibilityId(compatibilityIdList.get(i)));
           }
            res.put("status", "success");
            res.put("message", "查询成功");
            res.put("compatibilityList", compatibilityList);
        }
        catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "查询失败");

        }
        return  res.toString();
    }


    //查询总数==>高级
    @RequestMapping("searchCompatibilityTotal")
    @ResponseBody
    public  int searchCompatibilityTotal(@RequestBody Compatibility compatibility){

        try {
            return compatibilityService.searchCount(compatibility);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }



    //点击生成物料需求功能
    @RequestMapping("generateSheet")
    @ResponseBody
    public String generateSheet(String compatibilityId){
        JSONObject res=new JSONObject();
        try {
            //1根据配伍单号获取明细
            List<CompatibilityItem> compatibilityItemList = compatibilityService.getCompatibilityItemById(compatibilityId);

            //Threshold threshold=thresholdService.list().get(0);//基础数据表对象

            //找出最新的物料需求编号

            Calendar cal = Calendar.getInstance();
            //获取年
            String year = String.valueOf(cal.get(Calendar.YEAR));
            //获取月
            String mouth = getMouth(String.valueOf(cal.get(Calendar.MONTH) + 1));
            //序列号
            String number = "001";
            List<String> materialRequireIdList = compatibilityService.getNewestMaterialRequireId();


            if (materialRequireIdList.size() == 0) {
                number = "001";
            } else {
                String s = materialRequireIdList.get(0);//原字符串
                String s2 = s.substring(s.length() - 3, s.length());//最后一个3字符
                number = getString3(String.valueOf(Integer.parseInt(s2) + 1));
            }
            //最新的物料编号
            String materialRequireId = year + mouth + number;

            //主表的结构

            //周生产计划量汇总
            float weeklyDemandTotal = 0;

            //目前库存量汇总
            float currentInventoryTotal = 0;

            //安全库存量汇总
            float safetyTotal = 0;

            //市场采购量汇总
            float marketPurchasesTotal = 0;

            //热值总量
            float calorificMaxSum = 0;
            float calorificMinSum = 0;

            float ashMaxSum = 0;
            float ashMinSum = 0;

            float waterMaxSum = 0;
            float waterMinSum = 0;

            float clMaxSum = 0;
            float clMinSum = 0;

            float sMaxSum = 0;
            float sMinSum = 0;

            float pMaxSum = 0;
            float pMinSum = 0;

            float fMaxSum = 0;
            float fMinSum = 0;

            float phMaxSum = 0;
            float phMinSum = 0;

            //主表数据结构
            MaterialRequire materialRequire = new MaterialRequire();

            //2对明细进行循环 做数据加减操作
            int length=0;
            for (int i = 0; i < compatibilityItemList.size(); i++) {
                length=compatibilityItemList.size();
//                System.out.println("长度:"+compatibilityItemList.size());
//                Threshold threshold=thresholdService.getThresholdByHandleCategoryAndFormType(compatibilityItemList.get(i).getHandleCategory().toString(),compatibilityItemList.get(i).getFormType().toString());
                MaterialRequireItem materialRequireItem = new MaterialRequireItem();//创建物料明细对象

                //射入外键物料编号
                materialRequireItem.setMaterialRequireId(materialRequireId);

                //1周生产计划量
                materialRequireItem.setWeeklyDemand(compatibilityItemList.get(i).getWeeklyDemandTotal());
                weeklyDemandTotal += compatibilityItemList.get(i).getWeeklyDemandTotal();
                //2目前库存量
                materialRequireItem.setCurrentInventory(0);
                currentInventoryTotal += 0;
                //3安全库存量
                materialRequireItem.setSafety(0);
//                System.out.println("安全库存量" + threshold.getSafety());
                safetyTotal += 0;
                //4市场采购量
                materialRequireItem.setMarketPurchases(0);
                marketPurchasesTotal += 0;
                //5进料方式

                if (compatibilityItemList.get(i).getHandleCategoryItem() != null) {
                    materialRequireItem.setHandleCategoryItem(compatibilityItemList.get(i).getHandleCategoryItem());
                }

                //6形态
                if (compatibilityItemList.get(i).getFormTypeItem() != null) {
                    materialRequireItem.setFormTypeItem(compatibilityItemList.get(i).getFormTypeItem());
                }

                //包装形式
                //散装料+固体==>吨袋
                if (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 30 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 3) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(121);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //破碎料+固体==>标准箱
                else if (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 31 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 3) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(122);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //废液+液态==>吨箱
                else if (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 29 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 2) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(123);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //悬挂连+固态==>小袋
                else if (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 33 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 3) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(124);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //污泥+半固态==>标准箱
                else if  (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 28 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 4) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(122);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //精馏残渣+固态==>铁桶
                else if  (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 32 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 3) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(125);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //精馏残渣+液态==>吨箱
                else if  (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 32 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 2) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(123);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                //精馏残渣+半固态==>铁桶
                else if  (compatibilityItemList.get(i).getHandleCategoryItem().getDataDictionaryItemId() == 32 && compatibilityItemList.get(i).getFormTypeItem().getDataDictionaryItemId() == 4) {
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(125);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                }
                else{
                    PackageTypeItem packageTypeItem = new PackageTypeItem();
                    packageTypeItem.setDataDictionaryItemId(127);
                    materialRequireItem.setPackageTypeItem(packageTypeItem);
                    }
                    //热值最大
                    materialRequireItem.setCalorificMax(compatibilityItemList.get(i).getCalorific() + compatibilityItemList.get(i).getCalorificThreshold());
                    calorificMaxSum += compatibilityItemList.get(i).getCalorific() + compatibilityItemList.get(i).getCalorificThreshold();
                    //热值最小
                    materialRequireItem.setCalorificMin(compatibilityItemList.get(i).getCalorific() - compatibilityItemList.get(i).getCalorificThreshold());
                    calorificMinSum += compatibilityItemList.get(i).getCalorific() - compatibilityItemList.get(i).getCalorificThreshold();
                    //灰分最大
                    materialRequireItem.setAshMax(compatibilityItemList.get(i).getAsh() + compatibilityItemList.get(i).getAshThreshold());
                    ashMaxSum += compatibilityItemList.get(i).getAsh() + compatibilityItemList.get(i).getAshThreshold();
                    //灰分最小
                    materialRequireItem.setAshMin(compatibilityItemList.get(i).getAsh() - compatibilityItemList.get(i).getAshThreshold());
                    ashMinSum += compatibilityItemList.get(i).getAsh() - compatibilityItemList.get(i).getAshThreshold();
                    //水分最大
                    materialRequireItem.setWaterMax(compatibilityItemList.get(i).getWater() + compatibilityItemList.get(i).getWaterThreshold());
                    waterMaxSum += compatibilityItemList.get(i).getWater() + compatibilityItemList.get(i).getWaterThreshold();
                    //水分最小
                    materialRequireItem.setWaterMin(compatibilityItemList.get(i).getWater() - compatibilityItemList.get(i).getWaterThreshold());
                    waterMinSum += compatibilityItemList.get(i).getWater() - compatibilityItemList.get(i).getWaterThreshold();
                    //氯最大
                    materialRequireItem.setClMax(compatibilityItemList.get(i).getCl() + compatibilityItemList.get(i).getClThreshold());
                    clMaxSum += compatibilityItemList.get(i).getCl() + compatibilityItemList.get(i).getClThreshold();
                    //氯最小
                    materialRequireItem.setClMin(compatibilityItemList.get(i).getCl() - compatibilityItemList.get(i).getClThreshold());
                    clMinSum += compatibilityItemList.get(i).getCl() - compatibilityItemList.get(i).getClThreshold();
                    //硫最大
                    materialRequireItem.setsMax(compatibilityItemList.get(i).getS() + compatibilityItemList.get(i).getsThreshold());
                    sMaxSum += compatibilityItemList.get(i).getS() + compatibilityItemList.get(i).getsThreshold();
                    //硫最小
                    materialRequireItem.setsMin(compatibilityItemList.get(i).getS() - compatibilityItemList.get(i).getsThreshold());
                    sMinSum += compatibilityItemList.get(i).getS() - compatibilityItemList.get(i).getsThreshold();
                    //磷最大
                    materialRequireItem.setpMax(compatibilityItemList.get(i).getP() + compatibilityItemList.get(i).getpThreshold());
                    pMaxSum += compatibilityItemList.get(i).getP() + compatibilityItemList.get(i).getpThreshold();
                    //磷最小
                    materialRequireItem.setpMin(compatibilityItemList.get(i).getP() - compatibilityItemList.get(i).getpThreshold());
                    pMinSum += compatibilityItemList.get(i).getP() - compatibilityItemList.get(i).getpThreshold();
                    //氟最大
                    materialRequireItem.setfMax(compatibilityItemList.get(i).getF() + compatibilityItemList.get(i).getfThreshold());
                    fMaxSum += compatibilityItemList.get(i).getF() + compatibilityItemList.get(i).getfThreshold();
                    //氟最小
                    materialRequireItem.setfMin(compatibilityItemList.get(i).getF() - compatibilityItemList.get(i).getfThreshold());
                    fMinSum += compatibilityItemList.get(i).getF() - compatibilityItemList.get(i).getfThreshold();
                    //ph最大
                    materialRequireItem.setPhMax(compatibilityItemList.get(i).getPh() + compatibilityItemList.get(i).getPhThreshold());
                    phMaxSum += compatibilityItemList.get(i).getPh() + compatibilityItemList.get(i).getPhThreshold();
                    //ph最小
                    materialRequireItem.setPhMin(compatibilityItemList.get(i).getPh() - compatibilityItemList.get(i).getPhThreshold());
                    phMinSum += compatibilityItemList.get(i).getPh() - compatibilityItemList.get(i).getPhThreshold();
                    compatibilityService.addMaterialRequireItem(materialRequireItem);
                }
                //主表添加数据
                materialRequire.setMaterialRequireId(materialRequireId);

                materialRequire.setCompatibilityId(compatibilityId);

                materialRequire.setWeeklyDemandTotal(weeklyDemandTotal);

                materialRequire.setCurrentInventoryTotal(currentInventoryTotal);

                materialRequire.setSafetyTotal(safetyTotal);

                materialRequire.setMarketPurchasesTotal(marketPurchasesTotal);

                materialRequire.setCalorificAvg(((calorificMaxSum/length) + (calorificMinSum/length)) / 2);

                materialRequire.setAshAvg(((ashMaxSum/length) + (ashMinSum/length)) / 2);

                materialRequire.setWaterAvg(((waterMaxSum/length) + (waterMinSum/length)) / 2);

                materialRequire.setClAvg(((clMaxSum/length) + (clMinSum/length)) / 2);

                materialRequire.setsAvg(((sMaxSum/length) + (sMinSum/length)) / 2);

                materialRequire.setpAvg(((pMaxSum/length) + (pMinSum/length)) / 2);

                materialRequire.setfAvg(((fMaxSum/length) + (fMinSum/length)) / 2);

                materialRequire.setPhAvg(((phMaxSum/length) + (phMinSum/length)) / 2);

                compatibilityService.addMaterialRequire(materialRequire);

                //最后将已生成的配伍计划单失效即可
                compatibilityService.disabledMaterialRequire(compatibilityId);

                res.put("status", "success");
                res.put("message", "生成物料需求单成功");

        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "生成物料需求单失败");
        }



        return res.toString();
    }

    //根据配伍单号查询配伍信息
    @RequestMapping("getByCompatibilityId")
    @ResponseBody
    public String getByCompatibilityId(String compatibilityId){
        JSONObject res=new JSONObject();

        try {
            Compatibility compatibility=compatibilityService.getByCompatibilityId(compatibilityId);
            res.put("status", "success");
            res.put("message", "查询成功");
            res.put("data", compatibility);

        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "查询失败");
        }



        return res.toString();

    }

    //获取最后一位四位编号
     public static String getId(String id){
        while (id.length()!=4){
            System.out.println(id.length());
          id="0"+id;
        }
        return id;
     }
    //获取两位月数
    public  static  String getMouth(String mouth){
        if(mouth.length()!=2){
            mouth="0"+mouth;
        }
      return mouth;
    }
    //获取三位序列号
    public static String getString3(String id){
        while (id.length()!=3){
            System.out.println(id.length());
            id="0"+id;
        }
        return id;
    }

    //获取总记录数
    @RequestMapping("totalCompatibilityRecord")
    @ResponseBody
    public int totalCompatibilityRecord( ){
        try {
            return compatibilityService.totalCompatibilityRecord();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    //系统添加配伍主表
    @RequestMapping("addCompatibilityNew")
    @ResponseBody
    public String addCompatibilityNew(@RequestBody Compatibility compatibility){
        JSONObject res=new JSONObject();

        try{

       compatibilityService.addCompatibility(compatibility);
            res.put("status", "success");
            res.put("message", "主表添加成功");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "主表添加失败");

        }

        return res.toString();
    }

    //系统添加配伍字表
    @RequestMapping("addCompatibilityItemNew")
    @ResponseBody
    public String addCompatibilityItemNew(@RequestBody CompatibilityItem compatibilityItem){
        JSONObject res=new JSONObject();
        try {
//            List<String> compatibilityIList= compatibilityService.check();
//            compatibilityItem.setCompatibilityId(compatibilityIList.get(0));
            compatibilityService.addCompatibilityItem(compatibilityItem);
            res.put("status", "success");
            res.put("message", "子表添加成功");
        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "子表添加失败");
        }



        return res.toString();
    }

    @RequestMapping("getNewCompatibilityId")
    @ResponseBody
    public String getNewCompatibilityId(){

        Calendar cal = Calendar.getInstance();
        //获取年
        String year=String.valueOf(cal.get(Calendar.YEAR));
        //获取月
        String mouth= getMouth(String.valueOf(cal.get(Calendar.MONTH)+1));
        //序列号
        String number = "001";

        //先查看数据库的配伍编号
        List<String> compatibilityIList= compatibilityService.check();
        if(compatibilityIList.size()==0){
            number="001";
        }
        if(compatibilityIList.size()>0) {
            String s= compatibilityIList.get(0);//原字符串
            String s2=s.substring(s.length()-3,s.length());//最后一个3字符
            number=getString3(String.valueOf( Integer.parseInt(s2)+1));
        }
        //配伍编号
        String compatibilityId=year+mouth+number;

        return compatibilityId;
    }

    //配伍周计划导出
    @RequestMapping("exportExcelWeekPlan")
    @ResponseBody
    public String exportExcelWeekPlan(String name, HttpServletResponse response, String sqlWords){
        JSONObject res = new JSONObject();

        try {
            DBUtil db = new DBUtil();
            String tableHead = "配伍计划单号/进料方式/物质形态/比例/每日配置量/周需求总量/热值/灰分/水分/CL/S/P/F/PH";
            name = "配伍周计划";   //重写文件名
            db.exportExcel2(name, response, sqlWords, tableHead);//HttpServletResponse response
            res.put("status", "success");
            res.put("message", "导出成功");

        }
        catch (Exception e){
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "导出失败，请重试！");

        }


        return res.toString();
    }

}
