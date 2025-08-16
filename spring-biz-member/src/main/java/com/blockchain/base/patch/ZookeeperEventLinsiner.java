package com.blockchain.base.patch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperAutoServiceRegistration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ZookeeperEventLinsiner implements ApplicationListener<ContextRefreshedEvent>{
	@Autowired
	ZookeeperAutoServiceRegistration regist;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		regist.start();
	}
}
