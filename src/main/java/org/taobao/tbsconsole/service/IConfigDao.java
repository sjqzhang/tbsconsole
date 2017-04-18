/**
* <b>项目名：</b>transformers<br/>
* <b>包名：</b>com.meizu.transformers.bimonitor.service<br/>
* <b>文件名：</b>IAlarmDao.java<br/>
* <b>版本信息：</b> @version 1.0.0<br/>
* <b>日期：</b>2013-10-31-下午1:52:21<br/>
* <b>Copyright (c)</b> 2013魅族公司-版权所有<br/>
*
*/
	
package org.taobao.tbsconsole.service;

import java.util.List;

import org.taobao.tbsconsole.model.Config;
import org.taobao.tbsconsole.model.QueryModel;

/**
 * <b>类名称：</b>IAlarmDao<br/>
 * <b>类描述：</b><pre>

</pre><br/>
 * <b>创建人：</b>张军强<br/>
 * <b>修改人：</b>张军强<br/>
 * <b>修改时间：</b>2013-10-31 下午1:52:21<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */

public interface IConfigDao {
	public Config add(Config config);	
	public Config edit(Config config);	
	public List<Config> find(QueryModel query);
	public Config findOne(QueryModel query);
	public List<Config> findByType(QueryModel query);
	public Config del(long	id);
	public Integer findCount(QueryModel query);

}
