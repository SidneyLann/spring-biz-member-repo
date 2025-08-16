package com.blockchain.base.data;

import java.io.Serializable;

import jakarta.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class BaseDao<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements IBaseDao<T, ID> {
  private EntityManager entityManager;

  public BaseDao(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityManager = entityManager;
  }

  public BaseDao(Class<T> entityClass, EntityManager entityManager) {
    super(JpaEntityInformationSupport.getEntityInformation(entityClass, entityManager), entityManager);
    this.entityManager = entityManager;
  }
}
