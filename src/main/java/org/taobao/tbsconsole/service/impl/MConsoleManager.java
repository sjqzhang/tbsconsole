package org.taobao.tbsconsole.service.impl;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.taobao.tbsconsole.common.Constant;
import org.taobao.tbsconsole.model.Config;
import org.taobao.tbsconsole.model.QueryModel;
import org.taobao.tbsconsole.model.ZKConfig;

import com.alibaba.fastjson.JSON;
import com.taobao.pamirs.schedule.strategy.TBScheduleManagerFactory;
import com.taobao.pamirs.schedule.taskmanager.IScheduleDataManager;
import com.taobao.pamirs.schedule.zk.ScheduleStrategyDataManager4ZK;
/**
 * 
* <b>类名称：</b>MConsoleManager<br/>
* <b>类描述：</b><pre>

</pre><br/>
* <b>创建人：</b>张军强<br/>
* <b>邮箱：</b>s_jqzhang@163.com<br/>
* <b>修改时间：</b>2014-3-10 下午2:28:55<br/>
* <b>修改备注：</b><br/>
* @version 1.0.0<br/>
 */
public class MConsoleManager {	
	protected static transient Logger logger = LoggerFactory.getLogger(MConsoleManager.class);


	private String projectName;
	private Properties config;
	
	private ConfigService configService;
	
	

	private  TBScheduleManagerFactory scheduleManagerFactory;	
	
	
	/**
	 * 创建一个新的实例 MConsoleManager.
	 */

	public MConsoleManager(String projectName) {
		this.projectName=projectName;
	}
    
	public  boolean isInitial() {
		return scheduleManagerFactory != null;
	}

	
	public  boolean initial(Properties config) {
		
		
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxx"+config.toString());
		
		if(!isInitial()){
		this.config=config;
		scheduleManagerFactory = new TBScheduleManagerFactory();
		scheduleManagerFactory.start = false;
		
		}
		try {
			System.out.println("xxxxxxxxxxxxxxxxxx"+ config.toString());
		scheduleManagerFactory.init(config);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("初始化scheduleManagerFactory失败",e);
			return false;
		}
		return true;
	}
	public  TBScheduleManagerFactory getScheduleManagerFactory() throws Exception {
		if(isInitial() == false){
			initial(config);
		}
		return scheduleManagerFactory;
	}
	public  IScheduleDataManager getScheduleDataManager() throws Exception{
		if(isInitial() == false){
			initial(config);
		}
		return scheduleManagerFactory.getScheduleDataManager();
	}
	public  ScheduleStrategyDataManager4ZK getScheduleStrategyManager() throws Exception{
		if(isInitial() == false){
			initial(config);
		}
		return scheduleManagerFactory.getScheduleStrategyManager();
	}
    public  ZKConfig loadConfig() throws IOException{
    	QueryModel query=new QueryModel();
    	query.getCondition(Map.class).put("name", projectName);
    	query.getCondition(Map.class).put("type", Constant.CONF_TYPE_ZK_CONFIG);
    	
     return  JSON.parseObject(	configService.findOne(query).getConfig(),ZKConfig.class);
		
		
    	
    }
    
    private QueryModel getQueryModel(){
    	QueryModel query=new QueryModel();
    	query.getCondition(Map.class).put("name", projectName);
    	query.getCondition(Map.class).put("type", Constant.CONF_TYPE_ZK_CONFIG);
    	return query;
    }
	public  void saveConfigInfo(ZKConfig conf) throws Exception {
    	Config config=new Config();
    	config.setConfig(JSON.toJSONString(conf));
    	config.setType( Constant.CONF_TYPE_ZK_CONFIG);
    	config.setName(projectName);
    	config.setDesc("ZK配置");
    	Config c = configService.findOne(getQueryModel());
    	if(c==null||c.getId()==0){
    		configService.add(config);
    	} else {
    		config.setId(c.getId());
    		configService.edit(config);
    	}
	}
	public  void setScheduleManagerFactory(
			TBScheduleManagerFactory scheduleManagerFactory) {
		scheduleManagerFactory = scheduleManagerFactory;
	}

	public ConfigService getConfigService() {
		return configService;
	}

	public void setConfigService(ConfigService configService) {
		this.configService = configService;
	}
	
}
