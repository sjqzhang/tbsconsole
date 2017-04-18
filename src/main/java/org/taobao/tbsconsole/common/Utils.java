package org.taobao.tbsconsole.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * <b>类名称：</b>Utils<br/>
 * <b>类描述：</b>
 * 
 * <pre>
 </pre>
 * 
 * <br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-3-10 下午2:44:12<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 */

public class Utils {

	public static Properties map2Properties(Map map) {
		Properties pro = new Properties();
		if (map == null) {
			return pro;
		}
		pro.putAll(map);
		return pro;
	}
	
	public static boolean isEmpty(Object obj){
		if(obj==null||obj.toString().equalsIgnoreCase("")){
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args) {
		
		HashMap<String, Object> map=new HashMap<String, Object>();
		
		map.put("abc", 34);
		map.put("dfdf", "dfdfdf");
		
		System.out.println(map2Properties(map));
		
	}
}


