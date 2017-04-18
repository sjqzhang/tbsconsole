
package org.taobao.tbsconsole.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Repository;

/**
 * <b>类名称：</b>ServiceLocator<br/>
 * <b>类描述：</b>
 * 
 * <pre>
 </pre>
 * 
 * <br/>
 * <b>创建人：</b>张军强<br/>
 * <b>修改人：</b>张军强<br/>
 * <b>修改时间：</b>2013-10-31 下午2:57:48<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 */

@Repository("serviceLocator")
public class ServiceLocator implements BeanFactoryAware {

	private static BeanFactory beanFactory = null;

	private static ServiceLocator serviceLocator = new ServiceLocator();

	public static ServiceLocator getInstance() {
		return serviceLocator;
	}
	
	private ServiceLocator(){
		
	}

	@SuppressWarnings("unchecked")
	public static <T> T getService(String name) {

		return (T) beanFactory.getBean(name);
	}
	
	public static <T> T getService(Class<T> cls) {

		return (T) beanFactory.getBean(cls);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

		ServiceLocator.beanFactory = beanFactory;
		

	}

}
