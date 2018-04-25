package co.yiiu.web.tag;

import co.yiiu.module.node.model.Node;
import co.yiiu.module.node.service.NodeService;
import co.yiiu.module.user.service.UserService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Component
public class NodesDirective implements TemplateDirectiveModel {

  @Autowired
  private NodeService nodeService;

  @Autowired
  private UserService userService;

  @Override
  public void execute(Environment environment, Map map, TemplateModel[] templateModels,
                      TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
    DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_25);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String username = ((User) authentication.getPrincipal()).getUsername();
    List<Integer> list =  userService.findByUsername(username)
            .getNodes().stream()
            .map(Node::getId).collect(Collectors.toList());
    Integer[] ids = new Integer[list.size()];
    ids = list.toArray(ids);
    List<Map<String, Object>> nodes = nodeService.findByIdsMap(ids);

    environment.setVariable("nodes", builder.build().wrap(nodes));
    templateDirectiveBody.render(environment.getOut());
  }
}