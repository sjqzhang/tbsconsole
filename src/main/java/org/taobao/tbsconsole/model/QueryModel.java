/**
* <b>项目名：</b>tbsconsole<br/>
* <b>包名：</b>org.taobao.tbsconsole.model<br/>
* <b>文件名：</b>QueryModel.java<br/>
* <b>版本信息：</b> @version 1.0.0<br/>
* <b>日期：</b>2014-3-10-上午10:37:06<br/>
* <b>Copyright (c)</b> 2014魅族公司-版权所有<br/>
*
*/
	
package org.taobao.tbsconsole.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>类名称：</b>QueryModel<br/>
 * <b>类描述：</b><pre>

</pre><br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-3-10 上午10:37:06<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */

public class QueryModel  implements Serializable{
	


	/**
			* serialVersionUID:TODO
			* @since 1.0.0
			*/
		
	private static final long serialVersionUID = 1L;

	// 排序类别
	public static final String ORDER_BY_ASC = "asc";

	public static final String ORDER_BY_DESC = "desc";

	// 定义一个页面的显示记录的条数,初始为10条
	private int limit = 50;

	// 从哪一条记录开始
	private int start;

	// 定义查询出来的记录条数
	private long recordCount;

	// 定义一个总页数
	private int pageTotal;

	// 排序列的数据库列名
	public String sortColName;

	// 排序方式升序或降序 value is: asc/desc
	public String sortType;

	// 排序方式升序或降序 value is: asc/desc desc往前翻，asc往后翻
	public String pageForward;

	// 表头所有对象存放
	// 查询条件
	public Object condition;

	public List result;

	public String getPageForward() {
		if (pageForward == null)
			return QueryModel.ORDER_BY_DESC;
		else
			return pageForward;
	}

	public void setPageForward(String pageForward) {
		this.pageForward = pageForward;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public QueryModel() {
		this(-1, "0");
	}

	public QueryModel(int recordCount) {
		this(recordCount, "0");
	}

	public QueryModel(int recordCount, String start) {
		this.recordCount = recordCount;
		this.pageTotal = (recordCount + limit - 1) / limit;
		this.start = Integer.parseInt(start);
		this.condition = new HashMap<String, Object>();
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setStart(String start) {
		this.start = Integer.parseInt(start);
	}

	public long getRecordCount() {
		return recordCount;
	}

	public int getPageTotal() {
		return pageTotal;
	}

	public int getLimit() {
		return limit;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
		// if (recordCount > 0) {
		// this.pageTotal = (int) ((recordCount + limit - 1) / limit);
		// if (this.start > recordCount) {
		// this.start = this.pageTotal * this.limit - limit;
		// }
		// }

	}

	public void setRecordCount(String recordCount) {
		setRecordCount(Integer.parseInt(recordCount));
	}

	/**
	 * 返回当前页起始记录
	 * 
	 * @return
	 */
	public long getStartRecord() {
		return start;
	}

	/**
	 * 返回当前页结尾记录
	 * 
	 * @return
	 */
	public long getEndRecord() {
		if (start < 0) {
			return 0;
		} else if (start >= recordCount) {
			return recordCount - ((recordCount / limit) * limit);
		} else {
			return start;
		}
	}

	public void setSortColName(String sortColName) {
		this.sortColName = sortColName;
	}

	public String getSortColName() {
		return this.sortColName;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getSortType() {
		return this.sortType;
	}

	public <T> T getCondition(Class<T> clazz) {
		return (T) (getCondition());
	}

	public Object getCondition() {

		if (condition instanceof Map) {
			Map map = (Map) condition;
			if(map.get("start")==null) {
			map.put("start", this.getStart());
			}
			if(map.get("limit")==null) {
			map.put("limit", this.getLimit());
			}
			if(map.get("sortType")==null) {
			map.put("sortType", this.getSortType());
			}
			map.put("sortColName", this.getSortColName());
			map.put("pageForward", this.getPageForward());
			condition = map;
		}
		return condition;
	}

	public void setCondition(Object condition) {
		this.condition = condition;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}


}
