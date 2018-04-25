package co.yiiu.module.node.service;

import co.yiiu.module.node.model.Node;
import co.yiiu.module.node.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Service
@Transactional
@CacheConfig(cacheNames = "nodes")
public class NodeService {

  @Autowired
  private NodeRepository nodeRepository;

  @Cacheable
  public List findAll(boolean child) {
    if (child) {
      return nodeRepository.findByPidNot(0);
    } else {
      List<Map<String, Object>> nodes = new ArrayList<>();
      List<Node> pNodes = this.findByPid(0);
      for (Node pn : pNodes) {
        Map<String, Object> map = new HashMap<>();
        List<Node> cNodes = this.findByPid(pn.getId());
        map.put("name", pn.getName());
        map.put("list", cNodes);
        nodes.add(map);
      }
      return nodes;
    }
  }

  @Cacheable
  public List<Node> findByPid(int pid) {
    return nodeRepository.findByPid(pid);
  }

  @Cacheable
  public List findByIdsMap(Integer [] ids) {
    List<Map<String, Object>> nodes = new ArrayList<>();
    List<Node> pNodes = nodeRepository.findNodesByIdIn(ids);
    for (Node pn : pNodes) {
      Map<String, Object> map = new HashMap<>();
      List<Node> cNodes = this.findByPid(pn.getId());
      map.put("name", pn.getName());
      map.put("list", cNodes);
      nodes.add(map);
    }
    return nodes;
  }


  @CacheEvict(allEntries = true)
  public void save(Node node) {
    nodeRepository.save(node);
  }

  @CacheEvict(allEntries = true)
  public void deleteById(int id) {
    nodeRepository.delete(id);
  }

  @Cacheable
  public Node findById(int id) {
    return nodeRepository.findOne(id);
  }

  @Cacheable
  public Node findByValue(String value) {
    return nodeRepository.findByValue(value);
  }

  @CacheEvict(allEntries = true)
  public void deleteByPid(int pid) {
    nodeRepository.deleteByPid(pid);
  }

  // number : 1 or -1
  @CacheEvict(allEntries = true)
  public void dealTopicCount(Node node, int number) {
    //处理当前的模块
    node.setTopicCount(node.getTopicCount() + number);
    this.save(node);

    //处理当前模块的业务系统
    int pid = node.getPid();
    Node pNode = this.findById(pid);
    pNode.setTopicCount(pNode.getTopicCount() + number);
    this.save(pNode);
  }

  @CacheEvict(allEntries = true)
  public void clearCache(){}

  @CacheEvict(allEntries = true)
  public List<Node> findByPIds(Integer [] ids){
    return nodeRepository.findNodesByPidIn(ids);
  }

  @CacheEvict(allEntries = true)
  public List<Node> findByIds(Integer [] ids){
    return nodeRepository.findNodesByIdIn(ids);
  }

}