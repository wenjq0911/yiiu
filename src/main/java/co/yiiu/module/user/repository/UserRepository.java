package co.yiiu.module.user.repository;

import co.yiiu.module.node.model.Node;
import co.yiiu.module.user.model.GithubUser;
import co.yiiu.module.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * https://yiiu.co
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

  User findById(int id);

  Page<User> findByBlock(boolean block, Pageable pageable);

  User findByUsername(String username);

  void deleteById(int id);

  User findByToken(String token);

  User findByEmail(String email);

  User findByGithubUser(GithubUser githubUser);

  User findByRealName(String realName);

  Page<User> findUsersByNodesAndChecked(Node node, Pageable pageable,boolean checked);
  Page<User> findUsersByNodesAndChecked(Set<Node> node, Pageable pageable, boolean checked);
  Page<User> findUsersByNodes(Node node, Pageable pageable);

  User findByUsernameAndChecked(String username,boolean checked);

  Page<User> findByChecked(boolean checked, Pageable pageable);
}
