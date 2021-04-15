package club.chintaku;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * @description:
 * @author: chenzhuo
 * @create: 2021-04-05 23:13
 */
public class TestDeployment {

    /**
     * 测试部署流程
     */
    @Test
    public void test() {
        // 1.创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3.使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据库
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请流程")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection1.png")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }


    /**
     * 开始流程
     */
    @Test
    public void testStratProcess(){
        // 1.创建processEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取runtimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3.根据流程定义id，启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("evection");

        System.out.println("流程定义id" + instance.getProcessDefinitionId());
        System.out.println("流程实例id" + instance.getId());
        System.out.println("当前活动的id" + instance.getActivityId());
    }

    /**
     * 查询个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList(){
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3.根据流程key和任务的负责人查询任务
        List<Task> list = taskService.createTaskQuery()
                .processDefinitionKey("evection") //流程key
                .taskAssignee("zhangsan") //要查询的负责人,即使在bpmn中用的是大写字母，最终存到数据库中的也是小写
                .list();
        //4.输出
        for (Task task: list) {
            System.out.println("流程实例id" + task.getProcessInstanceId());
            System.out.println("任务id" + task.getId());
            System.out.println("任务负责人" + task.getAssignee());
            System.out.println("任务名称" + task.getName());
        }
    }

    /**
     * 完成任务
     */
    @Test
    public void testCompletePersonalTask() {
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3.完成任务
        taskService.complete("2505");
    }

    @Test
    public void completeTask() {
        //1.获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //2.获取taskService
        TaskService taskService = processEngine.getTaskService();
        //3.根据流程key和任务的负责人查询任务
        Task task= taskService.createTaskQuery()
                .processDefinitionKey("evection") //流程key
                .taskAssignee("财务") //要查询的负责人
                .singleResult();
        taskService.complete(task.getId());
        System.out.println("流程实例id" + task.getProcessInstanceId());
        System.out.println("任务id" + task.getId());
        System.out.println("任务负责人" + task.getAssignee());
        System.out.println("任务名称" + task.getName());
    }

}
