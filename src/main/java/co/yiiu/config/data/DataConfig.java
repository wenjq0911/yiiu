package co.yiiu.config.data;

import co.yiiu.module.node.model.Node;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Configuration
@ConfigurationProperties
public class DataConfig {

  private DataUser _user;
  private List<DataRole> _roles;
  private List<DataPermission> _permissions;
  private List<DataNode> _nodes;

  public DataUser get_user() {
    return _user;
  }

  public void set_user(DataUser _user) {
    this._user = _user;
  }

  public List<DataRole> get_roles() {
    return _roles;
  }

  public void set_roles(List<DataRole> _roles) {
    this._roles = _roles;
  }

  public List<DataPermission> get_permissions() {
    return _permissions;
  }

  public void set_permissions(List<DataPermission> _permissions) {
    this._permissions = _permissions;
  }

  public List<DataNode> get_nodes() {
    return _nodes;
  }

  public void set_nodes(List<DataNode> _nodes) {
    this._nodes = _nodes;
  }
}