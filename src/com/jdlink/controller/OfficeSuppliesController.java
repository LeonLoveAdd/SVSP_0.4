package com.jdlink.controller;

import com.jdlink.domain.Dictionary.CheckStateItem;
import com.jdlink.domain.OfficeSuppliesInbound;
import com.jdlink.domain.OfficeSuppliesItem;
import com.jdlink.domain.OfficeSuppliesOutbound;
import com.jdlink.domain.User;
import com.jdlink.service.OfficeSuppliesService;
import com.jdlink.service.UserService;
import com.jdlink.util.RandomUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 办公用品控制器
 */
@Controller
public class OfficeSuppliesController {

    @Autowired
    OfficeSuppliesService officeSuppliesService;

    @Autowired
    UserService userService;

    /*******************************************************入库部分********************************************************/

    /**
     * 列出所有入库单数据
     * @return 入库单数据
     */
    @RequestMapping("listOfficeSuppliesInbound")
    @ResponseBody
    public String listOfficeSuppliesInbound(@RequestBody OfficeSuppliesItem officeSuppliesItem) {
        JSONObject res = new JSONObject();
        try {
            List<OfficeSuppliesItem> officeSuppliesItemList = officeSuppliesService.listOfficeSuppliesInbound(officeSuppliesItem);
            JSONArray data = JSONArray.fromArray(officeSuppliesItemList.toArray(new OfficeSuppliesItem[officeSuppliesItemList.size()]));
            res.put("status", "success");
            res.put("message", "获取数据成功");
            res.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取数据失败");
        }
        return res.toString();
    }

    /**
     * 获取办公用品入库单条目数量
     * @return 入库单条目数量
     */
    @RequestMapping("countOfficeSuppliesInboundItem")
    @ResponseBody
    public String countOfficeSuppliesInboundItem(@RequestBody OfficeSuppliesItem officeSuppliesItem) {
        JSONObject res = new JSONObject();
        try {
            int count = officeSuppliesService.countOfficeSuppliesInboundItem(officeSuppliesItem);
            res.put("status", "success");
            res.put("message", "获取数据成功");
            res.put("data", count);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取数据失败");
        }
        return res.toString();
    }

    /**
     * 增加办公用品入库单
     * @param officeSuppliesInbound 办公用品入库单
     * @return 成功与否
     */
    @RequestMapping("addOfficeSuppliesInbound")
    @ResponseBody
    public String addOfficeSuppliesInbound(@RequestBody OfficeSuppliesInbound officeSuppliesInbound, HttpSession session) {
        JSONObject res = new JSONObject();
        try {
            // 获取用户的信息
            String author = "";
            User user = userService.getCurrentUserInfo(session);
            if (user != null) author = user.getName();
            // 设置编号
            officeSuppliesInbound.setInboundId(officeSuppliesService.getOfficeSupplierInboundId(officeSuppliesInbound.getInboundDate()));
            // 设置审批状态
            CheckStateItem checkStateItem = new CheckStateItem();
            checkStateItem.setDataDictionaryItemId(75);
            officeSuppliesInbound.setCheckStateItem(checkStateItem);
            // 遍历条目
            for (OfficeSuppliesItem officeSuppliesItem : officeSuppliesInbound.getOfficeSuppliesItemList()) {
                // 设置字条目的父编号
                officeSuppliesItem.setInboundId(officeSuppliesInbound.getInboundId());
                // 设置子条目的编号，规则为：父编号+随机八位（包含字母和数字）
                officeSuppliesItem.setItemId(officeSuppliesInbound.getInboundId() + RandomUtil.getRandomEightChar());
                // 设置制单人
                officeSuppliesItem.setAuthor(author);
            }
            // 增加入库单
            officeSuppliesService.addOfficeSuppliesInbound(officeSuppliesInbound);
            res.put("status", "success");
            res.put("message", "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "新增失败");
        }
        return res.toString();
    }

    /*******************************************************入库部分********************************************************/



    /*******************************************************出库部分********************************************************/

    /**
     * 列出所有出库单数据
     * @return 出库单数据
     */
    @RequestMapping("listOfficeSuppliesOutbound")
    @ResponseBody
    public String listOfficeSuppliesOutbound(@RequestBody OfficeSuppliesItem officeSuppliesItem) {
        JSONObject res = new JSONObject();
        try {
            List<OfficeSuppliesOutbound> officeSuppliesOutboundList = officeSuppliesService.listOfficeSuppliesOutbound(officeSuppliesItem);
            JSONArray data = JSONArray.fromArray(officeSuppliesOutboundList.toArray(new OfficeSuppliesOutbound[officeSuppliesOutboundList.size()]));
            res.put("status", "success");
            res.put("message", "获取数据成功");
            res.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取数据失败");
        }
        return res.toString();
    }

    /**
     * 增加办公用品出库单
     * @param officeSuppliesOutbound 办公用品出库单
     * @return 成功与否
     */
    @RequestMapping("addOfficeSuppliesOutbound")
    @ResponseBody
    public String addOfficeSuppliesOutbound(@RequestBody OfficeSuppliesOutbound officeSuppliesOutbound) {
        JSONObject res = new JSONObject();
        try {
            officeSuppliesService.addOfficeSuppliesOutbound(officeSuppliesOutbound);
            res.put("status", "success");
            res.put("message", "新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "新增失败");
        }
        return res.toString();
    }

    /*******************************************************出库部分********************************************************/



    /*******************************************************条目部分********************************************************/

    /**
     * 获取办公用品入库单条目
     * @param id 编号
     * @return 办公用品入库单条目
     */
    @RequestMapping("getOfficeSuppliesInboundItemById")
    @ResponseBody
    public String getOfficeSuppliesInboundItemById(String id) {
        JSONObject res = new JSONObject();
        try {
            OfficeSuppliesItem officeSuppliesItem = officeSuppliesService.getOfficeSuppliesInboundItemById(id);
            JSONObject data = JSONObject.fromBean(officeSuppliesItem);
            res.put("status", "success");
            res.put("message", "获取数据成功");
            res.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取数据失败");
        }
        return res.toString();
    }

    /**
     * 更新办公用品条目
     * @param officeSuppliesItem 办公用品条目
     * @return 成功与否
     */
    @RequestMapping("updateOfficeSuppliesInboundItem")
    @ResponseBody
    public String updateOfficeSuppliesInboundItem(@RequestBody OfficeSuppliesItem officeSuppliesItem) {
        JSONObject res = new JSONObject();
        try {
            officeSuppliesService.updateOfficeSuppliesInboundItem(officeSuppliesItem);
            res.put("status", "success");
            res.put("message", "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "更新失败");
        }
        return res.toString();
    }

    /**
     * 作废办公用品入库单
     * @param id 编号
     * @return 成功与否
     */
    @RequestMapping("setInvalidOfficeSuppliesInboundItem")
    @ResponseBody
    public String setInvalidOfficeSuppliesInboundItem(String id) {
        JSONObject res = new JSONObject();
        try {
            officeSuppliesService.setInvalidOfficeSuppliesInboundItem(id);
            res.put("status", "success");
            res.put("message", "作废成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "作废失败");
        }
        return res.toString();
    }

    /**
     * 获取办公用品出库单条目
     * @param id 编号
     * @return 出库单条目
     */
    @RequestMapping("getOfficeSuppliesOutboundItemById")
    @ResponseBody
    public String getOfficeSuppliesOutboundItemById(String id) {
        JSONObject res = new JSONObject();
        try {
            OfficeSuppliesItem officeSuppliesItem = officeSuppliesService.getOfficeSuppliesOutboundItemById(id);
            JSONObject data = JSONObject.fromBean(officeSuppliesItem);
            res.put("status", "success");
            res.put("message", "获取数据成功");
            res.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取数据失败");
        }
        return res.toString();
    }

    /**
     * 更新办公用品条目
     * @param officeSuppliesItem 办公用品条目
     * @return 成功与否
     */
    @RequestMapping("updateOfficeSuppliesOutboundItem")
    @ResponseBody
    public String updateOfficeSuppliesOutboundItem(@RequestBody OfficeSuppliesItem officeSuppliesItem) {
        JSONObject res = new JSONObject();
        try {
            officeSuppliesService.updateOfficeSuppliesOutboundItem(officeSuppliesItem);
            res.put("status", "success");
            res.put("message", "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "更新失败");
        }
        return res.toString();
    }

    /**
     * 作废办公用品出库单
     * @param id 编号
     * @return 成功与否
     */
    @RequestMapping("setInvalidOfficeSuppliesOutboundItem")
    @ResponseBody
    public String setInvalidOfficeSuppliesOutboundItem(String id) {
        JSONObject res = new JSONObject();
        try {
            officeSuppliesService.setInvalidOfficeSuppliesOutboundItem(id);
            res.put("status", "success");
            res.put("message", "作废成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "作废失败");
        }
        return res.toString();
    }

    /*******************************************************条目部分********************************************************/



}
