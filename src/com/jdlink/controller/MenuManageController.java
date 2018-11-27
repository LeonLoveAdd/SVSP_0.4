package com.jdlink.controller;


import com.jdlink.domain.Produce.Organization;
import com.jdlink.domain.User;
import com.jdlink.service.MenuManageService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MenuManageController {
    @Autowired
    MenuManageService menuManageService;

    private User user; // 当前登陆用户

    private Organization organizationA; // 暂存菜单树状结构数据

    /**
     * 获取菜单数据
     *
     * @return
     */
    @RequestMapping("loadMenuList")
    @ResponseBody
    public String loadMenuList() {
        JSONObject res = new JSONObject();
        try {
            List<Organization> organizationList = menuManageService.list();
            JSONArray data = JSONArray.fromArray(organizationList.toArray(new Organization[organizationList.size()]));
            res.put("data", data);
            res.put("status", "success");
            res.put("message", "数据获取成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "数据获取失败！");
        }
        // 返回结果
        return res.toString();
    }

    /**
     * 获取页面数据
     *
     * @return
     */
    @RequestMapping("loadMenuPageList")
    @ResponseBody
    public String loadMenuPageList() {
        JSONObject res = new JSONObject();
        try {
            List<Organization> organizationList = menuManageService.listMenuPage();
            JSONArray data = JSONArray.fromArray(organizationList.toArray(new Organization[organizationList.size()]));
            res.put("data", data);
            res.put("status", "success");
            res.put("message", "数据获取成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "数据获取失败！");
        }
        // 返回结果
        return res.toString();
    }

    public void getCurrentUserInfo(HttpSession session) {
        user = (User) session.getAttribute("user");   // 获取用户信息
    }

    /**
     * 新增
     *
     * @param organization
     * @return
     */
    @RequestMapping("addMenu")
    @ResponseBody
    public String addMenu(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            Organization organization1 = new Organization();
            int id = menuManageService.countById(organization.getId()); // 获取其子节点个数+1
            id += organization.getId() * 10;
            while (menuManageService.getMenuById(id) != null) {
                id += 1;
            }
            String name = "新页面";
            int i = 1;
            while (menuManageService.getMenuByName(name, organization.getId()) != null) { // 同一节点下名称不予许重复
                i++;
                name = "新页面";
                name += i;
            }
            organization1.setId(id);
            organization1.setpId(organization.getId());
            organization1.setName(name);
            organization1.setUrl(organization.getUrl());// 为空
            organization1.setIcon(organization.getIcon()); // 为空
            organization1.setLevel(organization.getLevel());
            organization1.setCreationDate(new Date());
            if (user != null) {
                organization1.setFounder(user.getName());
            } else {
                organization1.setFounder("未登录");
            }
            menuManageService.add(organization1);
            res.put("status", "success");
            res.put("message", "新增成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "新增失败！");
        }
        return res.toString();
    }

    /**
     * 重命名
     *
     * @param organization
     * @return
     */
    @RequestMapping("updateMenuName")
    @ResponseBody
    public String updateMenuName(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            menuManageService.updateName(organization);
            res.put("status", "success");
            res.put("message", "修改成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "修改失败！");
        }
        return res.toString();
    }

    /**
     * 设置页面链接
     *
     * @param organization
     * @return
     */
    @RequestMapping("updateMenuUrl")
    @ResponseBody
    public String updateMenuUrl(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            menuManageService.updateMenuUrl(organization);
            res.put("status", "success");
            res.put("message", "设置成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "设置失败！");
        }
        return res.toString();
    }

    /**
     * 设置一级菜单图标
     *
     * @param organization
     * @return
     */
    @RequestMapping("updateMenuIcon")
    @ResponseBody
    public String updateMenuIcon(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            menuManageService.updateMenuIcon(organization);
            res.put("status", "success");
            res.put("message", "设置成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "设置失败！");
        }
        return res.toString();
    }


    /**
     * 设置顺序
     *
     * @param
     * @return
     */
    @RequestMapping("updateMenuOrder")
    @ResponseBody
    public String updateMenuOrder(String idList, int pId) {
        JSONObject res = new JSONObject();
        try {
            List<Integer> oldIdList = menuManageService.getMenuIdListByPId(pId);
            // System.out.println("旧顺序");
            for (Integer integer : oldIdList) {
                System.out.print(integer + "/");
            }
            // System.out.println();
            String idOrder[] = idList.split("/");
            List<Integer> newIdList = new ArrayList<>();
            int j = 0;
            int id = -1;   // 用于储存第一个顺序发生变化的ID
            for (int i = 0; i < idOrder.length; i++) {
                //   System.out.print(idOrder[i]+"/");
                int newId = Integer.parseInt(idOrder[i]);
                int oldId = oldIdList.get(i);
                newIdList.add(newId);
                Organization organization = new Organization();
                if (newId != oldId) { // 如果顺序发生改变则进行调整
                    if (j == 0) { // 第一次设置
                        organization.setId(id);  // 将第一个对象的ID暂时设为-1,避免冲突
                        id = newId; // 将新ID暂存到ID中
                        organization.setOldId(oldId);
                        menuManageService.updateMenuOrder(organization);
                    } else {
                        organization.setId(newId);
                        organization.setOldId(oldId);
                        menuManageService.updateMenuOrder(organization);
                    }
                    j++;   // 计时器
                }
            }
            if (j != 0) {
                Organization organization1 = new Organization(); // 将第一个对象的顺序调整回来
                organization1.setId(id);
                organization1.setOldId(-1);
                menuManageService.updateMenuOrder(organization1);
            }
            res.put("status", "success");
            res.put("message", "设置成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "设置失败！");
        }
        return res.toString();
    }

    /**
     * 删除
     *
     * @param organization
     * @return
     */
    @RequestMapping("deleteMenu")
    @ResponseBody
    public String deleteMenu(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            List<Organization> organizationList = getTreeMenuList(organization);  // 获取孩子节点
            organization.setOrganizationList(organizationList); // 设置树状结构
            delete(organization);
            res.put("status", "success");
            res.put("message", "删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "删除失败！");
        }
        return res.toString();
    }

    /**
     * 递归删除节点及其子节点
     *
     * @param organization
     */
    public void delete(Organization organization) {
        menuManageService.delete(organization);
        if (organization.getOrganizationList() != null && organization.getOrganizationList().size() > 0) {
            for (Organization organization1 : organization.getOrganizationList()) {
                delete(organization1);
            }
        }
    }

    /**
     * 根据ID获取对象
     *
     * @param
     * @return
     */
    @RequestMapping("getMenuById")
    @ResponseBody
    public String getMenuById(int id) {
        JSONObject res = new JSONObject();
        try {
            Organization organization = menuManageService.getMenuById(id);
            JSONObject data = JSONObject.fromBean(organization);
            res.put("data", data);
            res.put("status", "success");
            res.put("message", "获取成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取失败！");
        }
        return res.toString();
    }

    /**
     * 根据name 和 pId获取对象
     *
     * @param
     * @return
     */
    @RequestMapping("getMenuByName")
    @ResponseBody
    public String getMenuByName(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            Organization organization1 = menuManageService.getMenuByName(organization.getName(), organization.getpId());
            JSONObject data = JSONObject.fromBean(organization1);
            res.put("data", data);
            res.put("status", "success");
            res.put("message", "获取成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取失败！");
        }
        return res.toString();
    }

    /**
     * 根据name 和 pId获取子类对象及其树状结构
     *
     * @param
     * @return
     */
    @RequestMapping("getChildrenMenuByName")
    @ResponseBody
    public String getChildrenMenuByName(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            List<Organization> organizationList = getTreeMenuList(organization);
            JSONArray data = JSONArray.fromArray(organizationList.toArray(new Organization[organizationList.size()]));
            res.put("data", data);
            res.put("status", "success");
            res.put("message", "获取成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取失败！");
        }
        return res.toString();
    }

    /**
     * 递归获取树状结构
     *
     * @param organization
     * @return
     */
    public List<Organization> getTreeMenuList(Organization organization) {
        List<Organization> organizationList = menuManageService.getChildrenMenuByName(organization);
        for (int i = 0; i < organizationList.size(); i++) {
            organizationList.get(i).setOrganizationList(getTreeMenuList(organizationList.get(i)));
        }
        return organizationList;
    }

    /**
     * 获取一级菜单父节点及其树状结构
     *
     * @param
     * @return
     */
    @RequestMapping("getLevelOneMenuByUrlAndPName")
    @ResponseBody
    public String getLevelOneMenuByUrlAndPName(@RequestBody Organization organization) {
        JSONObject res = new JSONObject();
        try {
            organization.setpId(1);  // 设置一级菜单父节点
            List<Organization> organizationList = getTreeMenuList(organization); // 获取一级菜单及其树状结构
            organization.setOrganizationList(organizationList);
            Organization organization1 = new Organization();
            if (organizationList != null && organizationList.size() > 0) {
                organization1 = getChildMenuByUrl(organization);
            }
            organization1.setUrl("firstPage.html");
            organization1.setName(organization.getName());
            JSONObject data = JSONObject.fromBean(organization1);
            res.put("data", data);
            res.put("status", "success");
            res.put("message", "获取成功!");
        } catch (Exception e) {
            e.printStackTrace();
            res.put("status", "fail");
            res.put("message", "获取失败！");
        }
        return res.toString();
    }

    /**
     * 递归根据URL查询子节点
     *
     * @param organization1
     * @return
     */
    public Organization getChildMenuByUrl(Organization organization1) {
        Organization organization2 = new Organization();
        for (Organization organization : organization1.getOrganizationList()) {
            if (organization.getUrl() != null && organization.getUrl().equals(organization1.getUrl())) { // 如果相等则说明是要寻找的子节点
                organization2.setName(organization1.getName());
                organization2.getOrganizationList().add(organization);  // 添加子节点路径
            } else { // 如果不相等则寻找子节点的子节点是否相等
                if (organization.getOrganizationList() != null && organization.getOrganizationList().size() > 0) {
                    organization.setUrl(organization1.getUrl());
                    Organization organization3 = getChildMenuByUrl(organization);
                    if (organization3 != null && !organization3.getName().equals("")){
                        organization2.setName(organization1.getName());
                        organization2.getOrganizationList().add(organization3);  // 添加路径
                    }
                }
            }
        }
        return organization2;
    }

    public MenuManageService getMenuManageService() {
        return menuManageService;
    }

    public void setMenuManageService(MenuManageService menuManageService) {
        this.menuManageService = menuManageService;
    }

    public Organization getOrganizationA() {
        return organizationA;
    }

    public void setOrganizationA(Organization organizationA) {
        this.organizationA = organizationA;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
