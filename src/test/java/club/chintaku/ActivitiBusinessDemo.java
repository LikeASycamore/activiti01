package club.chintaku;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

/**
 * @description:
 * @author: chenzhuo
 * @create: 2021-04-09 16:29
 */
public class ActivitiBusinessDemo {

    /**
     * 添加业务key到activiti表
     */

    @Test
    public void addBusinessKey(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService service = engine.getRuntimeService();
        // 第一个参数：流程定义key，第二个参数：businesskey，会插入到ru_exection表中
        ProcessInstance ins = service.startProcessInstanceByKey("evection", "1001");
        System.out.println("业务id" + ins.getBusinessKey());
    }


    /**
     * 全部流程实例的挂起和激活
     */
    @Test
    public void suspendAllProcessInstance() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService service = defaultProcessEngine.getRepositoryService();
        // 查询流程定义，获取流程定义的查询对象
        ProcessDefinition definition = service.createProcessDefinitionQuery()
                .processDefinitionKey("evection")
                .singleResult();
        // 获取当前流程定义的状态（是否挂起）
        boolean suspended = definition.isSuspended();
        // 获取流程定义的id
        String definitionId = definition.getId();
        // 如果是挂起，改为激活状态
        if (suspended) {
            // 三个参数为：流程定义id、是否暂停、暂停时间
            service.activateProcessDefinitionById(definitionId,true,null);
            System.out.println("流程定义id:" + definitionId + "已激活");
        } else {
            service.suspendProcessDefinitionById(definitionId, true, null);
            System.out.println("流程定义id:" + definitionId + "已挂起");
        }
    }


    /**
     * 单个流程实例的挂起、激活
     */
    @Test
    public void suspendSingleProcessInstance() {
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // runtimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 获取流程实例对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("12501")
                .singleResult();
        String instanceId = instance.getId();
        // 获取当前流程实例的状态（是否挂起）
        // ru_task、ru_execution表中的SUSPENSION_STATE：1激活，2挂起
        boolean suspended = instance.isSuspended();
        if (suspended) {
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程实例id:" + instanceId + "已激活");
        } else {
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程实例id:" + instanceId + "已挂起");
        }
    }
}
