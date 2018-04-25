package co.yiiu.web.admin;

import co.yiiu.config.SiteConfig;
import co.yiiu.core.base.BaseController;
import co.yiiu.core.bean.Result;
import co.yiiu.core.exception.ApiException;
import co.yiiu.core.util.FreemarkerUtil;
import co.yiiu.core.util.HttpUtils;
import co.yiiu.core.util.IpUtil;
import co.yiiu.module.node.model.Node;
import co.yiiu.module.node.service.NodeService;
import co.yiiu.module.security.model.Role;
import co.yiiu.module.security.service.RoleService;
import co.yiiu.module.user.dto.ExistUserModel;
import co.yiiu.module.user.dto.ExistUserResultModel;
import co.yiiu.module.user.model.User;
import co.yiiu.module.user.service.UserService;
import co.yiiu.web.front.CommonController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Controller
@RequestMapping("/admin/user")
public class UserAdminController extends BaseController {
  private Logger log = LoggerFactory.getLogger(UserAdminController.class);
  @Autowired
  private SiteConfig siteConfig;
  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private NodeService nodeService;
  /**
   * 用户列表
   *
   * @param p
   * @param model
   * @return
   */
  @GetMapping("/list")
  public String list(Integer p, Model model,Integer pid) {
    model.addAttribute("page", userService.pageUser(p == null ? 1 : p, siteConfig.getPageSize(),pid,true));
    model.addAttribute("pid", pid);
    model.addAttribute("pnodes", nodeService.findByPid(0));
    return "admin/user/list";
  }

  @GetMapping("/add")
  public String add(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("roles", roleService.findAll());
    model.addAttribute("nodes", nodeService.findByPid(0));
    return "admin/user/add";
  }

  @GetMapping("/import")
  public  String importUser(Model model){
    model.addAttribute("nodes", nodeService.findByPid(0));
    return "admin/user/import";
  }

  @GetMapping("/importSave")
  @ResponseBody()
  public String importUser(String apiUrl,String apiTicket,Integer nodeid,String account,String name){
    //接口地址
    String url = apiUrl+"?Ticket="+apiTicket;
    String res = "";
    ExistUserResultModel result = new ExistUserResultModel();
    try{
      res = HttpUtils.doGet(url);
    }catch (Exception ex){
      result.setResult(false);
      result.setMessage("请求接口数据时发生了错误");
      return JSON.toJSONString(result);
    }
    //将结果转为Json
    JSONArray jsonArray= JSON.parseArray(res);
    //循环保存数据
    List<User> users = new ArrayList<User>();
    List<ExistUserModel> exist =new ArrayList<ExistUserModel>();
    for(int i=0;i<jsonArray.size();i++){
      JSONObject obj = (JSONObject)jsonArray.get(i);
      User e = userService.findByUsername(obj.get(account).toString());
      Node node =nodeService.findById(nodeid);
      if(e!=null){
        ExistUserModel existUserModel=new ExistUserModel();
        existUserModel.setId(e.getId());
        existUserModel.setUsername(e.getUsername());
        existUserModel.setExistRealName(e.getRealName());
        existUserModel.setNewRealName(obj.get(name).toString());
        if(e.getNodes().stream().map(Node::getId).collect(Collectors.toList()).contains(node.getId())){
          existUserModel.setErrorStatus(1);
          existUserModel.setErrorMsg(node.getName()+"中已存在此用户");
        }else{
          existUserModel.setErrorStatus(2);
          existUserModel.setErrorMsg("系统中已存在此用户,可添加"+node.getName()+"权限");
        }
        exist.add(existUserModel);
      }else {
        User u =new User();
        u.setRealName(obj.get(name).toString());
        u.setUsername(obj.get(account).toString());
        Set<Role> roles = new HashSet<>();
        Role role = roleService.findByName(siteConfig.getNewUserRole());
        roles.add(role);
        u.setRoles(roles);
        Set<Node> nodes = new HashSet<>();
        nodes.add(nodeService.findById(nodeid));
        u.setNodes(nodes);
        u.setAvatar(" ");
        u.setInTime(new Date());
        u.setPassword(new BCryptPasswordEncoder().encode(siteConfig.getDefaultPwd()));
        u.setScore(siteConfig.getScore());
        u.setSpaceSize(siteConfig.getUserUploadSpaceSize());
        u.setChecked(true);
        users.add(u);
      }
    }
    userService.saveAll(users);
    //格式化并返回重复的
    result.setResult(true);
    result.setExistUserModels(exist);
    return JSON.toJSONString(result);
  }

  /**
   * 禁用用户
   *
   * @param id
   * @param response
   * @return
   */
  @GetMapping("/{id}/block")
  public String block(@PathVariable Integer id, HttpServletResponse response) {
    userService.blockUser(id);
    return redirect(response, "/admin/user/list");
  }

  /**
   * 解禁用户
   *
   * @param id
   * @param response
   * @return
   */
  @GetMapping("/{id}/unblock")
  public String unblock(@PathVariable Integer id, HttpServletResponse response) {
    userService.unBlockUser(id);
    return redirect(response, "/admin/user/list");
  }

  /**
   * 配置用户的角色
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}/edit")
  public String edit(@PathVariable Integer id, Model model) {
    model.addAttribute("user", userService.findById(id));
    model.addAttribute("roles", roleService.findAll());
    model.addAttribute("nodes", nodeService.findByPid(0));
    return "admin/user/edit";
  }
  /**
   * 保存配置用户的角色
   *
   * @param id
   * @return
   */
  @PostMapping("/{id}/edit")
  public String saveEdit(@PathVariable Integer id,
                         //int score,
                         Integer roleId,Integer[] nodeIds, HttpServletResponse response) {
    User user = userService.findById(id);
    Set<Role> roles = new HashSet<>();
    Role role = roleService.findById(roleId);
    roles.add(role);
    //TODO 记录日志
    //user.setScore(score);
    user.setRoles(roles);
    if(nodeIds==null){
      user.setNodes( new HashSet<>());
    }else{
      Set<Node> nodes = new HashSet<>();
      nodes.addAll(nodeService.findByIds(nodeIds));
      user.setNodes(nodes);
    }
    userService.save(user);
    return redirect(response, "/admin/user/list");
  }

  @PostMapping("/add")
  public String save(String username,String realName,Integer roleId,Integer[] nodeIds) {
    User user =new User();
    user.setUsername(username);
    user.setRealName(realName==null?username:realName);
    Set<Role> roles = new HashSet<>();
    Role role = roleService.findById(roleId);
    roles.add(role);
    //TODO 记录日志
    //user.setScore(score);
    user.setRoles(roles);
    Set<Node> nodes = new HashSet<>();
    nodes.addAll(nodeService.findByIds(nodeIds));
    user.setNodes(nodes);
    user.setAvatar(" ");
    user.setInTime(new Date());
    user.setPassword(new BCryptPasswordEncoder().encode(siteConfig.getDefaultPwd()));
    user.setScore(siteConfig.getScore());
    user.setSpaceSize(siteConfig.getUserUploadSpaceSize());
    user.setChecked(true);
    userService.save(user);
    return redirect("/admin/user/list");
  }
}
