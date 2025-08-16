package com.blockchain.base.data;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;

@Configuration
@EnableJpaRepositories(basePackages = "com.pcng", repositoryBaseClass = BaseDao.class)
public class BaseDataConfig {
  @Resource
  private DataSource dataSource;

  @Bean("entityManager")
  public EntityManager entityManager(EntityManager entityManager) {
    return entityManager;
  }

  @Bean("jpaQuery")
  public JPAQueryFactory jpaQueryFactory(EntityManager entityManager) {
    return new JPAQueryFactory(entityManager);
  }

  @Bean("sqlQuery")
  public MySQLQueryFactory mySQLQueryFactory() {
    SpringConnectionProvider provider = new SpringConnectionProvider(dataSource);

    return new MySQLQueryFactory(provider);
  }

  // @Bean("sqlQueryFactory")
  public SQLQueryFactory sqlQuery() {
    SQLTemplates templates = new MySQLTemplates();
    com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
    SQLQueryFactory queryFactory = new SQLQueryFactory(configuration, dataSource);

    return queryFactory;
  }  
  
  @Bean("idGenerator")
  public IdGenerator idGenerator() {
    return new IdGenerator();
  }

}
