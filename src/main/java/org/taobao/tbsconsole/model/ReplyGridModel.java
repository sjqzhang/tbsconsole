/**
* <b>项目名：</b>tbsconsole<br/>
* <b>包名：</b>org.taobao.tbsconsole.model<br/>
* <b>文件名：</b>GridDataModel.java<br/>
* <b>版本信息：</b> @version 1.0.0<br/>
* <b>日期：</b>2014-3-12-上午11:00:44<br/>
* <b>Copyright (c)</b> 2014魅族公司-版权所有<br/>
*
*/
	
package org.taobao.tbsconsole.model;

/**
 * <b>类名称：</b>GridDataModel<br/>
 * <b>类描述：</b><pre>

</pre><br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-3-12 上午11:00:44<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */

public class ReplyGridModel {
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
