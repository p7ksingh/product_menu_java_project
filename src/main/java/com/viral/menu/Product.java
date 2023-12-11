package com.viral.menu;


	

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;




@Table("product")

public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	


	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static Long getSerialversion() {
		return serialVersion;
	}

	public static final Long serialVersion = 123456L;

	@Id
	@Column("product_id")
	private Integer productId;
	
	@Column("name")
	private String name;
	
	@Column("price")
	private Integer price;
	
	@Column("status")
	private Integer status;
}

