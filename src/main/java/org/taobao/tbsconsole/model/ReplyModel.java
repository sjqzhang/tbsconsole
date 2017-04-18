package org.taobao.tbsconsole.model;


/**
 * 
* <b>类名称：</b>ReplyModel<br/>
* <b>类描述：</b><pre>

</pre><br/>
* <b>创建人：</b>张军强<br/>
* <b>邮箱：</b>s_jqzhang@163.com<br/>
* <b>修改时间：</b>2014-3-10 上午10:38:12<br/>
* <b>修改备注：</b><br/>
* @version 1.0.0<br/>
 */
public class ReplyModel {
	private Object reply;
	private int code = 0;
	private String message = "";

	
	public Object getReply() {
		return reply;
	}
	public void setReply(Object reply) {
		if (reply != null) {
			this.reply = reply;
		}
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
