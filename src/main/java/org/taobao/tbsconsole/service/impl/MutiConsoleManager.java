/**
* <b>项目名：</b>tbsconsole<br/>
* <b>包名：</b>org.taobao.tbsconsole.service<br/>
* <b>文件名：</b>MutiConsoleManager.java<br/>
* <b>版本信息：</b> @version 1.0.0<br/>
* <b>日期：</b>2014-3-10-下午2:19:55<br/>
* <b>Copyright (c)</b> 2014魅族公司-版权所有<br/>
*
*/
	
package org.taobao.tbsconsole.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.taobao.tbsconsole.common.Utils;
import org.taobao.tbsconsole.model.Config;
import org.taobao.tbsconsole.model.QueryModel;
import org.taobao.tbsconsole.model.ZKConfig;

import com.alibaba.fastjson.JSON;


/**
 * <b>类名称：</b>MutiConsoleManager<br/>
 * <b>类描述：</b><pre>

</pre><br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-3-10 下午2:19:55<br/>
 * <b>修改备注：</b><br/>
 * @version 1.0.0<br/>
 */

@Service("mutiConsoleManager")
public class MutiConsoleManager {
	
	
	private static Logger logger=LoggerFactory.getLogger(MutiConsoleManager.class);
	
	private static ConcurrentHashMap<String, MConsoleManager> gManger=new ConcurrentHashMap<String, MConsoleManager>();
	@Autowired
	@Qualifier("configService")
	private ConfigService configSerice;
	
	

	

	
	public MConsoleManager  saveConsoleManager(String project,ZKConfig zkcnf){
		Config config=new Config();
		config.setName(project);
		config.setConfig(JSON.toJSONString( zkcnf));
		return addConsoleManager(config);
	}
	
	public MConsoleManager getConsoleManager(String projectName){
		return gManger.get(projectName);
	}
	
	private MConsoleManager addConsoleManager(Config config) {
		MConsoleManager manager=new MConsoleManager(config.getName());
		Map map = JSON.parseObject(config.getConfig(),Map.class); 
		manager.initial(Utils.map2Properties(map));
		manager.setConfigService(configSerice);
		return gManger.put(config.getName(), manager);
	}
	
	
	@PostConstruct
	public void init(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				QueryModel queryModel=new QueryModel();
				queryModel.getCondition(Map.class).put("type", "ZK_CONFIG");
				List<Config> list = configSerice.findByType(queryModel);
				for (int i = 0; i < list.size(); i++) {
					Config config = list.get(i);
					addConsoleManager(config);
				}
				logger.info("MutiConsoleManager-init");
				
			}

			
		},"InitConsoleManager").start();

		
	}
		
	
	
	

}
