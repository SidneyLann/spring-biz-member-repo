package com.blockchain.base;

import java.io.Serializable;

public interface IBaseService<T, ID extends Serializable> {

  public T save(T entity);

  public T load(ID id);

}
