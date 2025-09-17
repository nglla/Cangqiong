package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.sky.constant.AutoFillConstant.*;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    @Pointcut("execution(* com.sky.mapper.*.*(..))&& @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始公共字段填充");

        //数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();
        //获取操作对象
        Object[] args = joinPoint.getArgs();//参数列表
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
        if(args.length==0||args==null){
            return;
        }
        Object entity = args[0];

        //inset update
        LocalDateTime localDateTime = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();
        if(operationType==OperationType.INSERT){
            try {
                Method setCreatTimeMethod = entity.getClass().getDeclaredMethod(SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTimeMethod = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreatUserMethod = entity.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
                Method setUpdateUserMethod = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                setCreatTimeMethod.invoke(entity,localDateTime);
                setUpdateTimeMethod.invoke(entity,localDateTime);
                setCreatUserMethod.invoke(entity,userId);
                setUpdateUserMethod.invoke(entity,userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if(operationType==OperationType.UPDATE){
            Method setUpdateTimeMethod = null;
            try {
                setUpdateTimeMethod = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUserMethod = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);
                setUpdateTimeMethod.invoke(entity,localDateTime);
                setUpdateUserMethod.invoke(entity,userId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
