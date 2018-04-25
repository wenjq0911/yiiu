package co.yiiu.web.tag;

import co.yiiu.config.SiteConfig;
import co.yiiu.module.node.model.Node;
import co.yiiu.module.node.service.NodeService;
import co.yiiu.module.topic.model.Topic;
import co.yiiu.module.topic.service.TopicService;
import co.yiiu.module.user.service.UserService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Component
public class TopicsDirective implements TemplateDirectiveModel {

  @Autowired
  private TopicService topicService;
  @Autowired
  private SiteConfig siteConfig;

  @Autowired
  private UserService userService;

  @Autowired
  private NodeService nodeService;

  @Override
  public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                      TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
    DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);

    String tab = StringUtils.isEmpty(map.get("tab")) ? "default" : map.get("tab").toString();
    if (StringUtils.isEmpty(tab)) tab = "default";

    int p = map.get("p") == null ? 1 : Integer.parseInt(map.get("p").toString());

    //获取到有权限的子系统
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = ((User) authentication.getPrincipal()).getUsername();
    Set<Node> list =  userService.findByUsername(username)
            .getNodes();
    //获取所有权限的
    List<Integer> ids = list.stream().map(Node::getId).collect(Collectors.toList());
    List<Node> hasOpt = nodeService.findByPIds(ids.toArray(new Integer[ids.size()]));
    hasOpt.addAll(list);
    Page<Topic> page = topicService.page(p, siteConfig.getPageSize(), tab,hasOpt);

    environment.setVariable("page", builder.build().wrap(page));
    templateDirectiveBody.render(environment.getOut());
  }

}