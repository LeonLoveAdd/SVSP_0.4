package com.jdlink.controller;

import com.jdlink.domain.Client;
import com.jdlink.domain.Page;
import com.jdlink.domain.Salesman;
import com.jdlink.service.ClientService;
import com.jdlink.service.SalesmanService;
import com.jdlink.util.ImportUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by matt on 2018/4/23.
 */
@Controller
public class SalesmanController {

    @Autowired
    SalesmanService salesmanService;

    @Autowired
    ClientService clientService;

    @RequestMapping("addSalesman")
    @ResponseBody
    public String addSalesman(@RequestBody Salesman salesman) {
        JSONObject res = new JSONObject();
        try {
            // 新增业务员信息
            salesmanService.add(salesman);
            res.put("status", "success");
            res.put("message", "业务员信息新增成功");
        } catch (Exception e) {
            res.put("status", "fail");
            res.put("message", "新增失败，检查输入信息是否有误");
        }
        return res.toString();
    }

    @RequestMapping("showSalesman")
    public ModelAndView showSalesman(String salesmanId) {
        ModelAndView mav = new ModelAndView();
        Salesman salesman = salesmanService.getBySalesmanId(salesmanId);
        mav.addObject("salesman", salesman);
        mav.setViewName("jsp/showSalesman.jsp");
        return mav;
    }

    @RequestMapping("getSalesmanById")
    @ResponseBody
    public String getSalesmanById(String salesmanId) {
        JSONObject rs=new JSONObject();
        try {
            Salesman salesman = salesmanService.getBySalesmanId(salesmanId);
            List<String> clientIdList= salesmanService.getClientBySalesId(salesmanId);
            JSONArray json=JSONArray.fromObject(clientIdList);
            JSONObject res = JSONObject.fromBean(salesman);
            res.put("json",json);
            return res.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject res = new JSONObject();
            res.put("status", "fail");
            return res.toString();
        }
    }

    @RequestMapping("updateSalesman")
    @ResponseBody
    public String updateSalesman(@RequestBody Salesman salesman) {
        JSONObject res = new JSONObject();
        try {
            salesmanService.update(salesman);
            res.put("message", "业务员信息更新成功");
            res.put("status", "success");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("message", "信息输入错误，请重试！");
            res.put("status", "fail");
        }
        return res.toString();
    }

    @RequestMapping("deleteSalesman")
    @ResponseBody
    public String deleteSalesman(String salesmanId) {
        JSONObject res = new JSONObject();
        try {
            salesmanService.delete(salesmanId);
            res.put("message", "业务员信息删除成功");
            res.put("status", "success");
        } catch (Exception e) {
            res.put("message", "删除失败，请重试！");
            res.put("status", "fail");
        }
        return res.toString();
    }

    @RequestMapping("listSalesman")//查看业务员
    @ResponseBody
    public String listSalesman() {
        try {
            List<Salesman> salesmanList = salesmanService.list();//查询业务员 是一个list数组
            JSONArray array = JSONArray.fromArray(salesmanList.toArray(new Salesman[salesmanList.size()]));
            // 返回结果
            return array.toString();
        } catch (Exception e) {
            e.printStackTrace();
            JSONObject res = new JSONObject();
            res.put("status", "fail");
            return res.toString();
        }
    }

    @RequestMapping("searchSalesman")
    @ResponseBody
    public String searchSalesman(@RequestBody Salesman salesman) {
        JSONObject res = new JSONObject();
        try {
            List<Salesman> salesmanList = salesmanService.search(salesman);
            JSONArray data = JSONArray.fromArray(salesmanList.toArray(new Salesman[salesmanList.size()]));
            res.put("status", "success");
            res.put("message", "查询成功");
            res.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "查询失败");
        }
        return res.toString();
    }

    @RequestMapping("searchSalesmanTotal")
    @ResponseBody
    public int searchSalesmanTotal(@RequestBody Salesman salesman) {
        try {
            return salesmanService.searchCount(salesman);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @RequestMapping("bindClient")
    @ResponseBody
    public ModelAndView bindClient(Salesman salesman) {
        ModelAndView mav = new ModelAndView();
        // 将客户信息加入mav
        List<Client> clientList = clientService.list();
        mav.addObject("clientList", clientList);
        // 加入当前业务员信息
        mav.addObject("salesman", salesman);
        // 跳转到绑定客户页面
        mav.setViewName("bindClient");
        return mav;
    }

//    @RequestMapping("assignClient")
//    @ResponseBody
//    public String assignClient(Salesman salesman) {
//        JSONObject res = new JSONObject();
//        try {
//            String companyName = clientService.getByClientId(salesman.getClientId()).getCompanyName();
//            salesman.setCompanyName(companyName);
//            salesmanService.assignClient(salesman);
//            res.put("status", "success");
//            res.put("message", "操作成功!");
//        } catch (Exception e) {
//            res.put("status", "fail");
//            res.put("message", "操作失败!");
//        }
//        return res.toString();
//    }

    /**
     * 获取目前的业务员编号
     * @return
     */
    @RequestMapping("getCurrentSalesmanId")
    @ResponseBody
    public String getCurrentSalesmanId() {
        //得到一个NumberFormat的实例
        NumberFormat nf = NumberFormat.getInstance();
        //设置是否使用分组
        nf.setGroupingUsed(false);
        //设置最大整数位数
        nf.setMaximumIntegerDigits(4);
        //设置最小整数位数
        nf.setMinimumIntegerDigits(4);
        // 获取最新编号
        String id;
        int index = salesmanService.count();
        // 获取唯一的编号
        do {
            index += 1;
            id = nf.format(index);
        } while (salesmanService.getBySalesmanId(id) != null);
        JSONObject res = new JSONObject();
        res.put("salesmanId", id);
        return res.toString();
    }

    /**
     * 获取总记录数
     * @return
     */
    @RequestMapping("totalSalesmanRecord")
    @ResponseBody
    public int totalSalesmanRecord(){
        try {
            return salesmanService.count();
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    @RequestMapping("loadPageSalesManList")
    @ResponseBody
    public  String loadPageSalesManList(@RequestBody Page page){
        try {
            // 取出查询客户
            List<Salesman> salesmenList = salesmanService.listPage(page);
            // 计算最后页位置
            JSONArray array = JSONArray.fromArray(salesmenList.toArray(new Salesman[salesmenList.size()]));
            // 返回结果
            return array.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("getAllSalesman")
    @ResponseBody
    public String getAllSalesman() {
        try {
            List<Salesman> salesmenList = salesmanService.list();
            JSONArray array = JSONArray.fromArray(salesmenList.toArray(new Salesman[salesmenList.size()]));
            return array.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 导入excel文件来增加化验单
     * @param excelFile 化验单excel文件
     * @return 导入成功与否
     */
    @RequestMapping("importSalesmanExcel")
    @ResponseBody
    public String importSalesmanExcel(MultipartFile excelFile) {
        JSONObject res = new JSONObject();
        try {
            Object[][] data = ImportUtil.getInstance().getExcelFileData(excelFile).get(0);
            for (int i = 1; i < data.length; i++) {
                Salesman salesman = new Salesman();
                salesman.setSalesmanId(data[i][0].toString());
                salesman.setName(data[i][1].toString());
                salesman.setSex(data[i][2].toString().equals("男"));
                salesman.setAge(Integer.parseInt(data[i][3].toString().split("\\.")[0]));
                salesmanService.add(salesman);
            }
            res.put("status", "success");
            res.put("message", "导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "导入失败，请检查文件！");
            res.put("exception", e.getMessage());
        }
        return res.toString();
    }

}
