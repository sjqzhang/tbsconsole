/**
 * <b>项目名：</b>tbsconsole<br/>
 * <b>包名：</b>org.taobao.tbsconsole.controller<br/>
 * <b>文件名：</b>ConsoleController.java<br/>
 * <b>版本信息：</b> @version 1.0.0<br/>
 * <b>日期：</b>2014-2-28-下午4:05:52<br/>
 * <b>Copyright (c)</b> 2014魅族公司-版权所有<br/>
 *
 */

package org.taobao.tbsconsole.controller;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.taobao.tbsconsole.common.BeanUtils;
import org.taobao.tbsconsole.common.Utils;
import org.taobao.tbsconsole.model.ReplyModel;
import org.taobao.tbsconsole.model.ZKConfig;
import org.taobao.tbsconsole.service.impl.ConfigService;
import org.taobao.tbsconsole.service.impl.MConsoleManager;
import org.taobao.tbsconsole.service.impl.MutiConsoleManager;

import com.taobao.pamirs.schedule.strategy.ManagerFactoryInfo;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategy;
import com.taobao.pamirs.schedule.strategy.ScheduleStrategyRunntime;
import com.taobao.pamirs.schedule.taskmanager.ScheduleServer;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskItem;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskType;
import com.taobao.pamirs.schedule.taskmanager.ScheduleTaskTypeRunningInfo;

/**
 * <b>类名称：</b>ConsoleController<br/>
 * <b>类描述：</b>
 * 
 * <pre>
 </pre>
 * 
 * <br/>
 * <b>创建人：</b>张军强<br/>
 * <b>邮箱：</b>s_jqzhang@163.com<br/>
 * <b>修改时间：</b>2014-2-28 下午4:05:52<br/>
 * <b>修改备注：</b><br/>
 * 
 * @version 1.0.0<br/>
 */
@RequestMapping(value = "/manageService")
@Controller
public class ConsoleController {

	
	
	@Autowired
	@Qualifier("configService")
	private ConfigService configService;
	
	@Autowired
	@Qualifier("mutiConsoleManager")
	private MutiConsoleManager mutiConsoleManager;
	
	
	private MConsoleManager getConsoleManager(String projectName){
		
		MConsoleManager console = mutiConsoleManager.getConsoleManager(projectName);
		if(console==null){
			new RuntimeException("系统启动中，请稍候....");
		}
		return console;
		
	}
	
	
	
	@RequestMapping(value = "/getProjects")
	public @ResponseBody
	ReplyModel getProjects() {
		ReplyModel model=new ReplyModel();
		List<Map> projects = configService.getProjects();
		model.setReply(projects);
		return model;

	}
	
	

	
	@RequestMapping(value = "/loadAllManagerFactoryInfo")
	public @ResponseBody
	ReplyModel loadAllManagerFactoryInfo(@RequestParam("project") String project) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		List<ManagerFactoryInfo> list = console.getScheduleStrategyManager().loadAllManagerFactoryInfo();
		reply.setReply(list);
		return reply;

	}
	
	/*以下为任务管理*/
	@RequestMapping(value = "/getAllTaskTypeBaseInfo")
	public @ResponseBody
	ReplyModel getAllTaskTypeBaseInfo(@RequestParam("project") String project) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		List<ScheduleTaskType> list = console.getScheduleDataManager().getAllTaskTypeBaseInfo();
		reply.setReply(list);
		return reply;

	}
	

	
	
	
	
//http://127.0.0.1/schedule/taskTypeInfo.jsp?baseTaskType=Developer-http
	
	
	@RequestMapping(value = "/getAllTaskTypeRunningInfo")
	public @ResponseBody
	ReplyModel getAllTaskTypeRunningInfo(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		List<ScheduleTaskTypeRunningInfo> list = console.getScheduleDataManager().getAllTaskTypeRunningInfo(baseTaskType);
		reply.setReply(list);
		return reply;

	}
	
	
	
	@RequestMapping(value = "/clearTaskType")
	public @ResponseBody
	ReplyModel clearTaskType(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		console.getScheduleDataManager().clearTaskType(baseTaskType);
		return reply;

	}
	
	@RequestMapping(value = "/selectAllValidScheduleServer")
	public @ResponseBody
	ReplyModel selectAllValidScheduleServer(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		//	List<ScheduleServer> serverList = console.getScheduleDataManager().selectAllValidScheduleServer(taskTypeRunningInfoList.get(i).getTaskType());
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		List<ScheduleServer> list=new ArrayList<ScheduleServer>();
		if(baseTaskType!=null&&!baseTaskType.equalsIgnoreCase("")){
		 list = console.getScheduleDataManager().selectAllValidScheduleServer(baseTaskType);
		 }
		reply.setReply(list);
		return reply;

	}
	
	@RequestMapping(value = "/loadAllTaskItem")
	public @ResponseBody
	ReplyModel loadAllTaskItem(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		//	List<ScheduleServer> serverList = console.getScheduleDataManager().selectAllValidScheduleServer(taskTypeRunningInfoList.get(i).getTaskType());
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		List<ScheduleTaskItem> list=new ArrayList<ScheduleTaskItem>();
		if(baseTaskType!=null&&!baseTaskType.equalsIgnoreCase("")){
		 list = console.getScheduleDataManager().loadAllTaskItem(baseTaskType);
		 }
		reply.setReply(list);
		return reply;

	}
	
	
	
	
	@RequestMapping(value = "/getZKData")
	public @ResponseBody
	ReplyModel getZKData(@RequestParam("project") String project,@RequestParam(value="path", required = false) String path) throws Exception {
		//	List<ScheduleServer> serverList = console.getScheduleDataManager().selectAllValidScheduleServer(taskTypeRunningInfoList.get(i).getTaskType());
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		if(path==null)
		path = console.getScheduleStrategyManager().getRootPath();
		 StringWriter writer = new StringWriter();
		 console.getScheduleStrategyManager().printTree(
				  path,writer,"<br/>");
		reply.setReply(writer.getBuffer().toString());
		return reply;

	}
	
	@RequestMapping(value = "/exportConfig")
	public @ResponseBody
	ReplyModel exportConfig(@RequestParam("project") String project,@RequestParam(value="path", required = false) String path) throws Exception {
		//	List<ScheduleServer> serverList = console.getScheduleDataManager().selectAllValidScheduleServer(taskTypeRunningInfoList.get(i).getTaskType());
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		if(path==null)
		path = console.getScheduleStrategyManager().getRootPath();
		StringWriter tmpWriter = new StringWriter();
		StringWriter confWriter = new StringWriter();
		StringBuffer buffer = console.getScheduleStrategyManager().exportConfig(path, confWriter);
		reply.setReply(buffer.toString());
		return reply;

	}
	
	@RequestMapping(value = "/saveConfigInfo")
	public @ResponseBody
	ReplyModel saveConfigInfo(@RequestParam("project") String project,@RequestParam("config") String config) throws Exception {
		
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		ZKConfig zkconfig = BeanUtils.str2Bean(config, ZKConfig.class);
		if(console==null){
			console=mutiConsoleManager.saveConsoleManager(project, zkconfig);
		}
		console.saveConfigInfo(zkconfig);
		mutiConsoleManager.saveConsoleManager(project, zkconfig);
		return reply;

	}
	
	
	
	/** 以下为策略管理 **/


	@RequestMapping(value = "/createScheduleStrategy")
	public @ResponseBody
	ReplyModel createScheduleStrategy(@RequestParam("project") String project,@RequestParam("strategy") String strategy) throws Exception {
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		ScheduleStrategy scheduleStrategy = BeanUtils.str2Bean(strategy, ScheduleStrategy.class);
		if(scheduleStrategy.getIPList()==null||scheduleStrategy.getIPList().length==0){
			scheduleStrategy.setIPList("127.0.0.1".split(","));
		}
		console.getScheduleStrategyManager().createScheduleStrategy(scheduleStrategy);
		//console.getScheduleStrategyManager().updateScheduleStrategy(scheduleStrategy);
		return reply;

	}
	

	@RequestMapping(value = "/updateScheduleStrategy")
	public @ResponseBody
	ReplyModel updateScheduleStrategy(@RequestParam("project") String project,@RequestParam("strategy") String strategy) throws Exception {
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		ScheduleStrategy scheduleStrategy = BeanUtils.str2Bean(strategy, ScheduleStrategy.class);
		if(scheduleStrategy.getIPList()==null||scheduleStrategy.getIPList().length==0){
			scheduleStrategy.setIPList("127.0.0.1".split(","));
		}
		console.getScheduleStrategyManager().updateScheduleStrategy(scheduleStrategy);
		return reply;

	}
	@RequestMapping(value = "/deleteScheduleStrategy")
	public @ResponseBody
	ReplyModel deleteScheduleStrategy(@RequestParam("project") String project,@RequestParam("strategyName") String strategyName) throws Exception {
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		try{
		console.getScheduleStrategyManager().deleteMachineStrategy(strategyName);
		}catch (Exception e) {
			reply.setCode(500);
			reply.setMessage(e.getMessage());
		}
		return reply;

	}
	
	
	@RequestMapping(value = "/switchStrategyStatus")
	public @ResponseBody
	ReplyModel switchStrategyStatus(@RequestParam("project") String project,@RequestParam("strategyName") String strategyName,@RequestParam("sts") String sts) throws Exception {
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		if(sts.equalsIgnoreCase(ScheduleStrategy.STS_RESUME)) {
		console.getScheduleStrategyManager().pause(strategyName);
		} else {
			console.getScheduleStrategyManager().resume(strategyName);	
		}
		return reply;

	}
	
	
	@RequestMapping(value = "/loadAllScheduleStrategyRunntimeByTaskType")
	public @ResponseBody
	ReplyModel loadAllScheduleStrategyRunntimeByTaskType(@RequestParam("project") String project,@RequestParam(value="strategyName", required = false) String strategyName) throws Exception {
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		List<ScheduleStrategyRunntime> runntimeList=null;
		if(strategyName==null||strategyName.equalsIgnoreCase("")) {
			 runntimeList=new ArrayList<ScheduleStrategyRunntime>();
		} else {
			 runntimeList = console.getScheduleStrategyManager().loadAllScheduleStrategyRunntimeByTaskType(strategyName);
		}
		
		reply.setReply(runntimeList);
		return reply;

	}
	
	
	
	
	@RequestMapping(value = "/loadAllScheduleStrategy")
	public @ResponseBody
	ReplyModel loadAllScheduleStrategy(@RequestParam("project") String project) throws Exception {
		
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);

		 List<ScheduleStrategy> list = console.getScheduleStrategyManager().loadAllScheduleStrategy();
		 reply.setReply(list);
		return reply;

	}
	

	
	
	/** 以下为任务管理 **/
	
	
	@RequestMapping(value = "/createBaseTaskType")
	public @ResponseBody
	ReplyModel createBaseTaskType(@RequestParam("project") String project,@RequestParam("task") String task) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		ScheduleTaskType taskType = BeanUtils.str2Bean(task, ScheduleTaskType.class);
		console.getScheduleDataManager().createBaseTaskType(taskType);
		return reply;
	}
	
	@RequestMapping(value = "/updateBaseTaskType")
	public @ResponseBody
	ReplyModel updateBaseTaskType(@RequestParam("project") String project,@RequestParam("task") String task) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		ScheduleTaskType taskType = BeanUtils.str2Bean(task, ScheduleTaskType.class);
		console.getScheduleDataManager().updateBaseTaskType(taskType);
		return reply;
	}
	
	@RequestMapping(value = "/deleteTaskType")
	public @ResponseBody
	ReplyModel deleteTaskType(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		console.getScheduleDataManager().deleteTaskType(baseTaskType);
		return reply;
	}
	
	@RequestMapping(value = "/pauseAllServer")
	public @ResponseBody
	ReplyModel pauseAllServer(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		console.getScheduleDataManager().pauseAllServer(baseTaskType);
		return reply;
	}
	
	@RequestMapping(value = "/resumeAllServer")
	public @ResponseBody
	ReplyModel resumeAllServer(@RequestParam("project") String project,@RequestParam("baseTaskType") String baseTaskType) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		console.getScheduleDataManager().resumeAllServer(baseTaskType);
		return reply;
	}
	
	/*以下为机器管理*/
	
	
	@RequestMapping(value = "/loadAllScheduleStrategyRunntimeByUUID")
	public @ResponseBody
	ReplyModel loadAllScheduleStrategyRunntimeByUUID(@RequestParam("project") String project,@RequestParam("uuid") String uuid) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		 List<ScheduleStrategyRunntime> serverList =new ArrayList<ScheduleStrategyRunntime>();
		 if(!Utils.isEmpty(uuid)){
		  serverList = console.getScheduleStrategyManager().loadAllScheduleStrategyRunntimeByUUID(uuid);
		 }
		 reply.setReply(serverList);
		return reply;
	}
	
	@RequestMapping(value = "/selectScheduleServerByManagerFactoryUUID")
	public @ResponseBody
	ReplyModel selectScheduleServerByManagerFactoryUUID(@RequestParam("project") String project,@RequestParam("managerFactoryUUID") String managerFactoryUUID) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		 List<ScheduleServer> serverList =new ArrayList<ScheduleServer>();
		 if(!Utils.isEmpty(managerFactoryUUID)){
		  serverList = console.getScheduleDataManager().selectScheduleServerByManagerFactoryUUID(managerFactoryUUID);
		 }
		 reply.setReply(serverList);
		return reply;
	}
	
	@RequestMapping(value = "/updateManagerFactoryInfo")
	public @ResponseBody
	ReplyModel updateManagerFactoryInfo(@RequestParam("project") String project,@RequestParam("uuid") String uuid,@RequestParam("sts") boolean sts) throws Exception {
		ReplyModel reply = new ReplyModel();
		MConsoleManager console = getConsoleManager(project);
		console.getScheduleStrategyManager().updateManagerFactoryInfo(uuid,!sts);
		return reply;
	}
	



}
