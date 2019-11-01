package com.suchaos.spring.annotation.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

/**
 * 切面
 *
 * @author suchao
 * @date 2019/10/30
 */
@Aspect
public class LogAspect {

    @Pointcut("execution(* com.suchaos.spring.annotation.aop.MathCalculator.div(..))")
    public void pointCut() {
    }

    @Pointcut("execution(* com.suchaos.spring.annotation.aop.MathCalculator.add(..))")
    public void pointCut2() {
    }


    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint) {
        System.out.printf("方法[%s]开始运行 --- 参数列表是：{%s}\n",
                joinPoint.getSignature().toLongString(), Arrays.toString(joinPoint.getArgs()));
    }

    @Before("pointCut2()")
    public void logStart2(JoinPoint joinPoint) {
        System.out.printf("方法[%s]开始运行 --- 参数列表是：{%s}\n",
                joinPoint.getSignature().toLongString(), Arrays.toString(joinPoint.getArgs()));
    }

    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint) {
        System.out.printf("方法[%s]结束\n", joinPoint.getSignature().toLongString());
    }

    @AfterReturning(value = "pointCut()", returning = "result")
    public void logReturn(JoinPoint joinPoint, Object result) {
        System.out.printf("方法[%s]正常方法 --- 返回值是：{%s}\n",
                joinPoint.getSignature().toLongString(), result.toString());
    }

    @AfterThrowing(value = "pointCut()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Exception exception) {
        System.out.printf("方法[%s]出现异常 --- 异常是：{%s}\n",
                joinPoint.getSignature().toLongString(), exception.toString());
    }
}
