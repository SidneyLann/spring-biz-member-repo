package com.blockchain.base.data;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

@Configuration
public class UserIDAuditor implements AuditorAware<Long> {
  private ThreadLocal<Long> threadUserId = new ThreadLocal<>();

  public void setUserId(Long userId) {
    threadUserId.set(userId);
  }

  @Override
  public Optional<Long> getCurrentAuditor() {
    Long userId = threadUserId.get();
    if (userId == null)
      userId=168168168L;

    Optional<Long> opt = Optional.of(userId);

    return opt;
  }

//  @Override
//  public Optional<Long> getCurrentAuditor() {
//
//      UserDetails user;
//      try {
//          user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//          return Optional.ofNullable(user.getUsername());
//      }catch (Exception e){
//          return Optional.empty();
//      }
//  }
}