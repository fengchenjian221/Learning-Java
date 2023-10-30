package boot.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class MyAopAnnotation {
    //切入点：增强标有MyLogAnnotation注解的方法
    @Pointcut(value="@annotation(boot.MyAnnotation.MyLogAnnotation)")
    //返回通知
    @AfterReturning(returning = "ret", pointcut = "logAnnotation()")
    //切入点签名
    public void logAnnotation(){
        System.out.println("pointCut签名。。。");
    }

    //前置通知
    @Before("logAnnotation()")
    public void deBefore(JoinPoint jp) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        System.out.println("URL : " + request.getRequestURL().toString());
    }

    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("返回通知：方法的返回值 : " + ret);
    }

    //异常通知
    @AfterThrowing(throwing = "ex", pointcut = "logAnnotation()")
    public void throwss(JoinPoint jp,Exception ex){
        System.out.println("异常通知：方法异常时执行.....");
        System.out.println("产生异常的方法："+jp);
        System.out.println("异常种类："+ex);
    }

    //后置通知
    @After("logAnnotation()")
    public void after(JoinPoint jp){
        System.out.println("后置通知：最后且一定执行.....");
    }
}

