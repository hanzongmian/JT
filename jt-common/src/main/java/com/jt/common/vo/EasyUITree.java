package com.jt.common.vo;

public class EasyUITree {

	//
	private Long id;  //元素id
	private String text; //元素文本内容
	private String state;//节点默认关闭 
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
