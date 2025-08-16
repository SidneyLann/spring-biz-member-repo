package com.blockchain.base.data;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface IBaseDao<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID>, CrudRepository<T, ID> {
}
