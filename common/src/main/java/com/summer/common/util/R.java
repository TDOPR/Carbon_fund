
package com.summer.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	
	public R() {
		put("code", 0);
		put("msg", "success");
	}

	public static R error() {
		return error(500, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(500, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	public static R ok(int code, String msg) {
		R r = new R();
		r.put("msg", msg);
		r.put("code", code);
		return r;
	}


	public static R OkData(int code,String  data) {
		R r = new R();
		r.put("code", code);
		r.put("data", data);
		return r;
	}

	public static R ok(Object data) {
		R r = new R();
		r.put("data", data);
		return r;
	}

	public static R ok(int code, Object data) {
		R r = new R();
		r.put("data", data);
		r.put("code", code);
		return r;
	}

	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}
	
	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
