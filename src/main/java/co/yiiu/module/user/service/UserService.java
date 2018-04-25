package co.yiiu.module.user.service;

import co.yiiu.module.node.model.Node;
import co.yiiu.module.node.repository.NodeRepository;
import co.yiiu.module.user.model.User;
import co.yiiu.module.user.repository.UserRepository;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Service
@Transactional
@CacheConfig(cacheNames = "users")
public class UserService {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private NodeRepository nodeRepository;
  /**
   * search user by score desc
   *
   * @param p
   * @param size
   * @return
   */
  @Cacheable
  public Page<User> findByScore(int p, int size) {
    Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "score"));
    Pageable pageable = new PageRequest(p - 1, size, sort);
    return userRepository.findByBlock(false, pageable);
  }

  @Cacheable
  public User findById(int id) {
    return userRepository.findById(id);
  }

  /**
   * 根据用户名判断是否存在
   *
   * @param username
   * @return
   */
  @Cacheable
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  public User findByUsernameAndChecked(String username,boolean checked) {
    return userRepository.findByUsernameAndChecked(username,checked);
  }
  public User findByRealName(String realName){
    return userRepository.findByRealName(realName);
  }

  @Cacheable
  public User findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @CacheEvict(allEntries = true)
  public void save(User user) {
    userRepository.save(user);
  }

  @CacheEvict(allEntries = true)
  public void saveAll(List<User> users){
    for (User u:users
         ) {
      userRepository.save(u);
    }
  }
  /**
   * 分页查询用户列表
   *
   * @param p
   * @param size
   * @return
   */
  @Cacheable
  public Page<User> pageUser(int p, int size,Integer pid,boolean checked) {
    Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "inTime"));
    Pageable pageable = new PageRequest(p - 1, size, sort);
    if(pid==null)
      return userRepository.findByChecked(checked,pageable);
    Node node = nodeRepository.findOne(pid);
    return userRepository.findUsersByNodesAndChecked(node,pageable,checked);
  }
  public Page<User> pageUser(int p, int size, Set<Node> nodes, boolean checked) {
    Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "inTime"));
    Pageable pageable = new PageRequest(p - 1, size, sort);
    if(nodes==null)
      return userRepository.findByChecked(checked,pageable);
    return userRepository.findUsersByNodesAndChecked(nodes,pageable,checked);
  }
  /**
   * 禁用用户
   *
   * @param id
   */
  @CacheEvict(allEntries = true)
  public void blockUser(Integer id) {
    User user = findById(id);
    user.setBlock(true);
    save(user);
  }

  /**
   * 用户解禁
   *
   * @param id
   */
  @CacheEvict(allEntries = true)
  public void unBlockUser(Integer id) {
    User user = findById(id);
    user.setBlock(false);
    save(user);
  }

  /**
   * 根据令牌查询用户
   *
   * @param token
   * @return
   */
  @Cacheable
  public User findByToken(String token) {
    return userRepository.findByToken(token);
  }

  /**
   * 删除用户
   * 注：这会删除用户的所有记录，慎重操作
   * @param id
   */
  //@CacheEvict(allEntries = true)
    public void deleteById(int id) {
        User user = findById(id);
        userRepository.deleteById(id);
    }

}
