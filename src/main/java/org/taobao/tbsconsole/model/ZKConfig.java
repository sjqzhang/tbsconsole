/**
* <b>项目名：</b>tbsconsole<br/>
* <b>包名：</b>org.taobao.tbsconsole.model<br/>
* <b>文件名：</b>ZKConfig.java<br/>
* <b>版本信息：</b> @version 1.0.0<br/>
* <b>日期：</b>2014-3-11-下午3:45:32<br/>
* <b>Copyright (c)</b> 2014魅族公司-版权所有<br/>
*
*/
	
package org.taobao.tbsconsole.model;

/**
 * <b>类名称：</b>ZKConfig<br/>
 * <b>类描述：</b><pre>

</pre><br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-3-11 下午3:45:32<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */

public class ZKConfig{
	String zkConnectString;
	String userName;
	String rootPath;
	String password;
	long zkSessionTimeout;
	public String getZkConnectString() {
		return zkConnectString;
	}
	public void setZkConnectString(String zkConnectString) {
		this.zkConnectString = zkConnectString;
	}
	public String getUserName() {
		return userName==null?"":userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	public String getPassword() {
		return password==null?"":password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getZkSessionTimeout() {
		return zkSessionTimeout;
	}
	public void setZkSessionTimeout(long zkSessionTimeout) {
		this.zkSessionTimeout = zkSessionTimeout;
	}

	
}
