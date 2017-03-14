package com.janabo.myim.entity;

/**
 * 消息实体类
 */
public class Msg {
	private String urlimg; // 图片消息
	private String code;
	private String msg;
	private String stro;

	public Msg(String msg) {
		this.msg = msg;
	}

	public String getUrlimg() {
		return urlimg;
	}

	public void setUrlimg(String urlimg) {
		this.urlimg = urlimg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStro() {
		return stro;
	}

	public void setStro(String stro) {
		this.stro = stro;
	}
}
