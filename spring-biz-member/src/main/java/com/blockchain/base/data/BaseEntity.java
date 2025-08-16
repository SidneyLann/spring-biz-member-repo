package com.blockchain.base.data;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity extends RootEntity {
	@CreatedBy
	private Long createBy;

	@CreatedDate
	// @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "remark")
	@Size(min = 0, max = 20)
	private String remark;

	@LastModifiedBy
	private Long updateBy;

	@LastModifiedDate
	// @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private Date updateTime;

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
