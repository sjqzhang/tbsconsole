package org.taobao.tbsconsole.common;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;

/**
 * 
 * <b>类名称：</b>BeanUtils<br/>
 * <b>类描述：Bean工具类</b>
 * 
 * <pre>
 </pre>
 * 
 * <br/>
 * <b>创建人：</b>张军强<br/>
 * <b>修改人：</b>张军强<br/>
 * <b>修改时间：</b>2012-12-4 下午1:28:28<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 */
public class BeanUtils {

	private static final Logger logger = Logger.getLogger(BeanUtils.class);

	private BeanUtils() {
	}

	@SuppressWarnings("rawtypes")
	public static <T> List<T> map2Bean(List<Object> list, Class<T> cls) {
		List<T> retlist = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			retlist.add(map2Bean((Map) list.get(i), cls));
		}
		return retlist;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T str2Bean(String str, Class<T> cls) throws UnsupportedEncodingException {
		T obj = JSON.parseObject(str, cls);
		Map map = BeanUtils.pojoToMap(obj, null);
		Iterator it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			if (map.get(key) != null) {
				map.put(key, URLDecoder.decode(map.get(key).toString(), "UTF-8"));
			}
		}
		if (map.size() > 0) {
			obj = BeanUtils.map2Bean(map, cls);
		}
		return obj;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T map2Bean(Map map, Class<T> cls) {

		Object obj = null;
		try {
			obj = cls.newInstance();
		} catch (IllegalAccessException e1) {
			logger.error(e1.getMessage());
			return null;
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
			return null;
		}
		// 取出bean里的所有方法
		Method[] methods = cls.getMethods();
		for (int i = 0; i < methods.length; i++) {
			// 取方法名
			String method = methods[i].getName();
			// 取出方法的类型
			Class[] cc = methods[i].getParameterTypes();
			if (cc.length != 1) {
				continue;
			}
			// 如果方法名没有以set开头的则退出本次for
			if (method.indexOf("set") < 0) {
				continue;
			}
			// 类型
			String type = cc[0].getSimpleName();
			// 转成小写
			Object value = method.substring(3, 4).toLowerCase() + method.substring(4);
			// 如果map里有该key
			if (map.containsKey(value) && map.get(value) != null) {
				Object o = map.get(value);
				Object[] objval;
				if (o instanceof Object[]) {
					objval = (Object[]) o;
					if (objval.length > 0) {
						setValue(type, objval[0], i, methods, obj);
					} else {
						setValue(type, objval, i, methods, obj);
					}
				} else {
					setValue(type, o, i, methods, obj);
				}

			}
		}
		return (T) obj;
	}

	private static void setValue(String type, Object value, int i, Method[] method, Object bean) {
		if (value != null && !value.equals("")) {
			try {
				if (type.equals("String")) {
					// 第一个参数:从中调用基础方法的对象 第二个参数:用于方法调用的参数
					method[i].invoke(bean, new Object[] { value });
				} else if (type.equals("int") || type.equals("Integer")) {
					method[i].invoke(bean, new Object[] { new Integer("" + value) });
				} else if (type.equals("long") || type.equals("Long")) {
					method[i].invoke(bean, new Object[] { new Long("" + value) });
				} else if (type.equals("boolean") || type.equals("Boolean")) {
					method[i].invoke(bean, new Object[] { Boolean.valueOf("" + value) });
				} else if (type.equals("BigDecimal")) {
					method[i].invoke(bean, new Object[] { new BigDecimal("" + value) });
				} else if (type.equals("Date")) {
					Date date = null;
					if (value.getClass().getName().equals("java.util.Date")) {
						date = (Date) value;
					} else if (value.getClass().getName().equals("java.sql.Timestamp")) {
						date = new Date(((Timestamp) (value)).getTime());
					} else if (value.getClass().getName().equals("java.lang.Integer")) {
						date = new Date(((Integer) (value)));
					} else {
						String format = ((String) value).indexOf(":") > 0 ? "yyyy-MM-dd hh:mm:ss" : "yyyy-MM-dd";
						SimpleDateFormat fmt = new SimpleDateFormat(format);
						date = fmt.parse((String) value);

					}
					if (date != null) {
						method[i].invoke(bean, new Object[] { date });
					}
				} else if (type.equals("byte[]")) {
					method[i].invoke(bean, new Object[] { new String(value + "").getBytes() });
				} else if(type.equals("Object")){
					method[i].invoke(bean, new Object[] { value });
				}
			} catch (InvocationTargetException e) {
				logger.error("将linkHashMap 或 HashTable 里的值填充到javabean时出错,请检查!" + e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error("将linkHashMap 或 HashTable 里的值填充到javabean时出错,请检查!" + e.getMessage());
			} catch (ParseException e) {
				logger.error("将linkHashMap 或 HashTable 里的值填充到javabean时出错,请检查!" + e.getMessage());
			} catch (IllegalAccessException e) {
				// TODO: handle exception
			}
		}
	}

	private static PropertyDescriptor[] getPropertyDescriptors(Object obj) {
		BeanInfo ObjInfo;
		try {
			ObjInfo = Introspector.getBeanInfo(obj.getClass());
		} catch (IntrospectionException e) {
			return new PropertyDescriptor[0];
		}

		return ObjInfo.getPropertyDescriptors();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map pojoToMap(Object obj, Map map) {
		if (map == null) {
			map = new HashMap<String, Object>();
		}
		PropertyDescriptor[] propertyDesc = getPropertyDescriptors(obj);
		for (int i = 0; i < propertyDesc.length; i++) {
			if (propertyDesc[i].getName().compareToIgnoreCase("class") == 0) {
				continue;
			}
			propertyDesc[i].getName();
			try {
				map.put(propertyDesc[i].getName(), propertyDesc[i].getReadMethod().invoke(obj));
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage());
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage());
			}
		}
		return map;
	}
}
