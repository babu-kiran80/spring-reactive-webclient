package com.reactive.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet {

	private String msg;
	private String user;
	
	/*
	 * public Tweet(String m, String u){ this.msg = m; this.user = u; }
	 * 
	 * public String getMsg() { return msg; }
	 * 
	 * public void setMsg(String msg) { this.msg = msg; }
	 * 
	 * public String getUser() { return user; }
	 * 
	 * public void setUser(String user) { this.user = user; }
	 */
}
