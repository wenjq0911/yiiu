package co.yiiu.web.admin;

import co.yiiu.core.base.BaseController;
import co.yiiu.module.node.service.NodeService;
import co.yiiu.module.topic.service.TopicSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Controller
@RequestMapping("/admin")
public class IndexAdminController extends BaseController {

  @Autowired
  private TopicSearch topicSearch;

  @Autowired
  private NodeService nodeService;
  /**
   * 首页
   *
   * @return
   */
  @GetMapping("/index")
  public String index() {
    return "admin/index";
  }


  /**
   * 索引首页
   *
   * @return
   */
  @GetMapping("/indexed")
  public String indexed(String s, Model model) {
    model.addAttribute("s", s);
    return "admin/indexed/index";
  }

  /**
   * 索引全部话题
   *
   * @param response
   * @return
   */
  @GetMapping("/indexed/indexAll")
  public String indexedAll(HttpServletResponse response) {
    topicSearch.indexAll();
    return redirect(response, "/admin/indexed?s=add");
  }

}
