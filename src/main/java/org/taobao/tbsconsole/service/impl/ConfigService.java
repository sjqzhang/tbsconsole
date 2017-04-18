
package org.taobao.tbsconsole.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Service;
import org.taobao.tbsconsole.common.BeanUtils;
import org.taobao.tbsconsole.common.Constant;
import org.taobao.tbsconsole.model.Config;
import org.taobao.tbsconsole.model.QueryModel;
import org.taobao.tbsconsole.service.IConfigDao;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * <b>类名称：</b>ConfigService<br/>
 * <b>类描述：</b>
 * 
 * <pre>
 * 
 * </pre>
 * 
 * <br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-3-10 下午2:52:43<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 */
@Service("configService")
public class ConfigService extends SqlSessionDaoSupport implements IConfigDao, BeanFactoryAware {

	private static List<Config> listConfig = new ArrayList<Config>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.meizu.transformers.bimonitor.service.IConfigDao#add(com.meizu.
	 * transformers.bimonitor.model.Config)
	 */

	public Config getConfigByTypeAndName(String type, String name) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("type", type);

		return this.getSqlSession().selectOne("Config.find", map);
	}

	@Override
	public Config add(Config config) {

		HashMap<String, Object> map = new HashMap();
		BeanUtils.pojoToMap(config, map);
		if (config.getId() == 0) {
			this.getSqlSession().insert("Config.add", map);
			return getConfigByTypeAndName(config.getType(), config.getName());
		} else {
			edit(config);
		}
		listConfig = find(new QueryModel());// 重新加载所有配置
		return config;

	}

	@Override
	public List<Config> find(QueryModel query) {
		return this.getSqlSession().selectList("Config.find", query.getCondition());
	}

	@Override
	public Integer findCount(QueryModel query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Config edit(Config config) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		BeanUtils.pojoToMap(config, map);
		this.getSqlSession().update("Config.edit", map);
		listConfig = find(new QueryModel());
		return config;
	}

	@Override
	public Config del(long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		Config config = null;
		QueryModel query = new QueryModel();
		query.getCondition(Map.class).put("id", id);
		List<Config> list = find(query);
		if (list.size() > 0) {
			config = list.get(0);
		}
		this.getSqlSession().delete("Config.del", map);
		listConfig = find(new QueryModel());// 重新加载所有配置
		return config;
	}

	public static Config getConfig(String type, String name) {
		for (int i = 0; i < listConfig.size(); i++) {
			Config config = listConfig.get(i);
			if (config.getName().equalsIgnoreCase(name) && config.getType().equalsIgnoreCase(type)) {
				return config;
			}
		}
		return new Config();
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

		final ConfigService service = (ConfigService) beanFactory.getBean("configService");
		listConfig = service.find(new QueryModel());
        //定时刷新配置
		Thread freshConfigThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(5000);
						service.find(new QueryModel());
					} catch (Exception e) {
						logger.error(e);

					}
				}

			}
		});
		freshConfigThread.setDaemon(true);
//		freshConfigThread.start();

	}

	public List<Map> getProjects() {
		Config configs = getConfig(Constant.CONF_TYPE_PROJECT_NAME_CONFIG, "projectNames");
		List<Map> list = new ArrayList<Map>();
		try {
			list = JSONObject.parseArray(configs.getConfig(), Map.class);
		} catch (Exception e) {
			logger.error("项目名称配置 出错");
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.taobao.tbsconsole.service.IConfigDao#findByType(org.taobao.tbsconsole
	 * .model.QueryModel)
	 */

	@Override
	public List<Config> findByType(QueryModel query) {
		return this.getSqlSession().selectList("Config.findByType", query.getCondition());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.taobao.tbsconsole.service.IConfigDao#findOne(org.taobao.tbsconsole.
	 * model.QueryModel)
	 */

	@Override
	public Config findOne(QueryModel query) {
		if (query.getCondition(Map.class).get("type") == null || query.getCondition(Map.class).get("name") == null) {
			return new Config();
		}
		return this.getSqlSession().selectOne("Config.findOne", query.getCondition());
	}

}
