package com.dcjt.dcjtim.bean;

import java.io.Serializable;

/**
 * 
 * @author 唐文滔 on 2020年7月17日
 *
 */
public class RWrapper<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private T data;
	private String msg;

	private RWrapper(REnum rEnum) {
		this.code = rEnum.code;
		this.msg = rEnum.msg;
	}

	private RWrapper(REnum rEnum, T data) {
		this(rEnum.code, rEnum.msg, data);
	}

	private RWrapper(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private RWrapper(String code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public RWrapper<T> setCode(String code) {
		this.code = code;
		return this;
	}

	public T getData() {
		return data;
	}

	public RWrapper<T> setData(T data) {
		this.data = data;
		return this;
	}

	public String getMsg() {
		return msg;
	}

	public RWrapper<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public static <T> RWrapper<T> success() {
		return new RWrapper<T>(REnum.SUCCESS);
	}

	public static <T> RWrapper<T> success(T data) {
		return new RWrapper<T>(REnum.SUCCESS, data);
	}

	public static <T> RWrapper<T> failture(T data) {
		return new RWrapper<T>(REnum.FAILED, data);
	}

	public static <T> RWrapper<T> failture(REnum rEnum) {
		return new RWrapper<T>(rEnum);
	}

	public static <T> RWrapper<T> failture(String code, String msg) {
		return new RWrapper<>(code, msg);
	}

	public static <T> RWrapper<T> failture(REnum rEnum, T data) {
		return new RWrapper<T>(rEnum, data);
	}

	@Override
	public String toString() {
		return "RWrapper [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}
}
