package com.wj.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局事务控制 和 接口日志有冲突
 *
 * @author wangjie
 * @date 10:43 2022年07月26日
 **/
@Slf4j
@Aspect
@Configuration
public class TransactionGlobalConfig {

    /** 切面表达式
     * 全局事务位置配置 在哪些地方需要进行事务处理：具体如下
     * 配置切入点表达式
     * 1.execution(): 表达式主体
     * 2.第一个*号:表示返回类型，*号表示所有的类型
     * 3.com.fangchui.platform.service表示切入点的包名
     * 4.第二个*号:表示实现包
     * 5.*(..)*号表示所有方法名,..表示所有类型的参数
     */
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.fangchui.cooperation.service.*.*(..))";

    /** 配置超时时间 */
    private static final int TX_METHOD_TIME_OUT = 30;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * @description 通知：事务管理规则
     *              REQUIRED ：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。
     *              SUPPORTS ：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
     *              MANDATORY ：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
     *              REQUIRES_NEW ：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
     *              NOT_SUPPORTED ：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
     *              NEVER ：以非事务方式运行，如果当前存在事务，则抛出异常。
     *              NESTED ：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于 REQUIRED 。
     *              指定方法：通过使用 propagation 属性设置，例如：@Transactional(propagation = Propagation.REQUIRED)
     *
     * @author wangjie
     * @date 11:51 2021年08月25日
     * @param
     * @return org.springframework.transaction.interceptor.TransactionInterceptor
     */
    @Bean
    public TransactionInterceptor txAdvice() {
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        Map<String, TransactionAttribute> txNameMap = new HashMap<>(16);

        /** 事务管理规则：只读事务 */
        RuleBasedTransactionAttribute readOnlyRule = new RuleBasedTransactionAttribute();

        /** 设置当前事务为只读 */
        readOnlyRule.setReadOnly(true);

        /** transactiondefinition 定义事务的隔离级别；
         *  PROPAGATION_REQUIRED 如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中 */
        readOnlyRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        /** 增删改的事务 */
        RuleBasedTransactionAttribute requireRule = new RuleBasedTransactionAttribute();

        /** 遇到异常回滚 */
        requireRule.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));

        requireRule.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        /** 设置事务失效时间 */
        requireRule.setTimeout(TX_METHOD_TIME_OUT);

        /** 增删改 */
        txNameMap.put("add*", requireRule);
        txNameMap.put("save*", requireRule);
        txNameMap.put("insert*", requireRule);
        txNameMap.put("set*", requireRule);
        txNameMap.put("update*", requireRule);
        txNameMap.put("delete*", requireRule);
        txNameMap.put("remove*", requireRule);
        txNameMap.put("batch*", requireRule);
        txNameMap.put("send*", requireRule);

        /** TODO 查询 加入了日志，日志需要再当前事务下进行存数据库，暂时将全局事务取消只读事务 */
//        txNameMap.put("get*", readOnlyRule);
//        txNameMap.put("query*", readOnlyRule);
//        txNameMap.put("find*", readOnlyRule);
//        txNameMap.put("select*", readOnlyRule);
//        txNameMap.put("count*", readOnlyRule);
//        txNameMap.put("list", readOnlyRule);
        txNameMap.put("get*", requireRule);
        txNameMap.put("query*", requireRule);
        txNameMap.put("find*", requireRule);
        txNameMap.put("select*", requireRule);
        txNameMap.put("count*", requireRule);
        txNameMap.put("list", requireRule);

        source.setNameMap(txNameMap);

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(platformTransactionManager);
        transactionInterceptor.setTransactionAttributeSource(source);
        return transactionInterceptor;
    }

    /**
     * @description 切面配置： [切点AOP_POINTCUT_EXPRESSION, 通知txAdvice]
     *
     * @author wangjie
     * @date 11:51 2021年08月25日
     * @param
     * @return org.springframework.aop.Advisor
     */
    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(aspectJExpressionPointcut, txAdvice());
    }

}
