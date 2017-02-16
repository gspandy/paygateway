package com.qiyu.paygateway.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Method;

/**
 * Created by Qidi on 2017/1/15.
 */
public class DataSourceAdvice implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    private Logger logger = LoggerFactory.getLogger(DataSourceAdvice.class);

    // service方法执行之前被调用
    public void before(Method method, Object[] args, Object target) throws Throwable {
        logger.info("切入点: " + target.getClass().getName() + "类中" + method.getName() + "方法");
        if(method.getName().startsWith("add")
                || method.getName().startsWith("create")
                || method.getName().startsWith("save")
                || method.getName().startsWith("edit")
                || method.getName().startsWith("update")
                || method.getName().startsWith("delete")
                || method.getName().startsWith("remove")){
            System.out.println("切换到: master");
            DataSourceSwitcher.setMaster();
        }
        else  {
            logger.info("切换到: slave");
            DataSourceSwitcher.setSlave();
        }
    }

    // service方法执行完之后被调用
    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
    }

    // 抛出Exception之后被调用
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        DataSourceSwitcher.setSlave();
        logger.info("出现异常,切换到: slave");
    }

}