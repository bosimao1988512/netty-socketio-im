package com.dcjt.dcjtim.bean;

/**
 * @author 滔哥 on 2020年7月17日
 *
 */
public enum REnum {

    SUCCESS("000", "正常结果返回"),
    NOTFOUND("100", "服务方不存在"),
    PARSEERROR("200", "服务方构造结果数据时异常"),
    SAVEFAILED("201","文件存储异常"),
    FAILED("999", "请求异常");
	
	private REnum(String code,String msg){
		this.code=code;
		this.msg=msg;
	}
	/**
	 * code编码
	 */
	final String code;
	/**
	 * 中文信息描述
	 */
	final String msg;
}
