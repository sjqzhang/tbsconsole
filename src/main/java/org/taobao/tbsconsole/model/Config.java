/**
* <b>项目名：</b>bimonitor<br/>
* <b>包名：</b>com.meizu.transformers.bimonitor.model<br/>
* <b>文件名：</b>Config.java<br/>
* <b>版本信息：</b> @version 1.0.0<br/>
* <b>日期：</b>2014-2-13-上午9:47:26<br/>
* <b>Copyright (c)</b> 2014魅族公司-版权所有<br/>
*
*/
	
package org.taobao.tbsconsole.model;

/**
 * <b>类名称：</b>Config<br/>
 * <b>类描述：</b><pre>

</pre><br/>
 * <b>创建人：</b>张军强<br/>
 * <b>修改人：</b>张军强<br/>
 * <b>修改时间：</b>2014-2-13 上午9:47:26<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */

public class Config {
	
	private long id;
	private String config;
	private String type;
	private String name;
	private int status;
	private String desc;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
