package com.blockchain.base.data;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.Transient;
import jakarta.persistence.EntityListeners;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class RootEntity implements java.io.Serializable {
	@Id
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(name = "action")
	private Short action = 1;

	@Column(name = "status")
	private Short status = 1;

	@Transient
	private Short pageNo;

	@Transient
	private Short pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getAction() {
		return action;
	}

	public void setAction(Short action) {
		this.action = action;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getPageNo() {
		return pageNo;
	}

	public void setPageNo(Short pageNo) {
		this.pageNo = pageNo;
	}

	public Short getPageSize() {
		return pageSize;
	}

	public void setPageSize(Short pageSize) {
		this.pageSize = pageSize;
	}

}
