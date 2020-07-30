package com.itheima.springmvc.pojo;

import java.util.List;

public class QueryVo {

	
	//商品
	private Items items;
	
	Integer[] ids;
	
	private List<Items> itemsList;
	
	
	
	
	

	public List<Items> getItemsList() {
		return itemsList;
	}

	public void setItemsList(List<Items> itemsList) {
		this.itemsList = itemsList;
	}

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}
	
}
