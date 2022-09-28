package com.wj.boot.config.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wj.boot.exception.CommonException;
import com.wj.boot.exception.EmError;
import com.wj.boot.mapper.LogMapper;
import com.wj.boot.utils.BrowserAndIPUtils;
import com.wj.boot.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 日志切面
 *
 * @author wangjie
 * @date 16:51 2022年05月07日
 **/
@Aspect
@Configuration
@Slf4j
public class LogAspect {

    @Autowired
    private LogMapper logMapper;

    private volatile LogEntity logEntity;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * @param
     * @return void
     * @description 初始化日志信息实体，防止一次方法内多次调用注解数据重复的问题
     * @author wangjie
     * @date 18:10 2022年05月07日
     */
    private void initLogEntity() {
        logEntity = new LogEntity();
    }

    /**
     * 定义一个全局map，记录每个方法的logEntity，防止切面覆盖切面后logEntity也被覆盖的清空
     */
    private HashMap<Integer, LogEntity> logMap = new HashMap<>();

    /**
     * 拦截被LogAnnotation注解的方法；如果需要拦截指定package指定规则名称的方法，可以使用表达式execution(...)
     */
    @Pointcut("@annotation(com.wj.boot.config.log.LogAnnotation)")
    private void logPoint() {
        log.info("-----------------enter point cut-------------------");
    }

    /**
     * 切面前置方法
     *
     * @author wangjie
     * @date 10:30 2022年05月09日
     **/
    @Before(value = "logPoint()")
    void logBefore(JoinPoint point) {
        try {
            initLogEntity();

            // 将记录日志实体放到map里，K为切点方法的hashcode，V为实体
            // 取得切面方法的hashcode
            final MethodSignature sign = (MethodSignature) point.getSignature();
            final Method method = sign.getMethod();
            final Integer hashCode = method.hashCode();
            // 获取方法上的注解信息
            LogAnnotation annotation = getAnnotation(point);
            final String tableName = annotation.tableName();
            final OperateType operateType = annotation.operateType();
            final Integer paramOrder = annotation.paramOrder();
            Parameter[] parameters = method.getParameters();
            // 入参大于0, 根据类型填充log实体
            if (parameters.length > 0) {
                Parameter parameter = parameters[paramOrder];
                Class<?> type = parameter.getType();
                // 获得方法的入参
                Object[] args = point.getArgs();
                // 删除操作会提前查询一次，记录到旧值里
                // 修改操作会提前查询一次，并且修改后也会查询一次，分别记录到旧值和新值里
                if (OperateType.DELETE == operateType || OperateType.UPDATE == operateType) {
                    getOldValueByMapper(type, args[paramOrder], tableName);
                } else if (OperateType.PATCH_DELETE == operateType || OperateType.PATCH_UPDATE == operateType) {
                    getPatchOldValueByMapper(type, args[paramOrder], tableName);
                }
            }
            // 执行完后存入Map
            logMap.put(hashCode, logEntity);
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(EmError.LOG_FAILD);
        }
    }

    @AfterReturning(pointcut = "logPoint()", returning = "returnValue")
    void logAdvice(JoinPoint point, Object returnValue) throws Exception {
        MethodSignature sign = (MethodSignature) point.getSignature();
        // 获取方法
        Method method = sign.getMethod();
        // 根据方法hashcode获取存放在map里的log实体
        if (logMap.containsKey(method.hashCode())) {
            this.logEntity = logMap.get(method.hashCode());
        }
        // 获取当前请求
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String token = request.getHeader("token");
        // 解密
        if (token != null) {
            // 用户ID
            logEntity.setUid(jwtTokenUtil.getIdFromToken(token));
        }

        LogAnnotation annotation = method.getAnnotation(LogAnnotation.class);

        // 操作描述
        logEntity.setDescription(annotation.description());
        // 被操作的表名
        logEntity.setTableName(annotation.tableName());
        // 操作类型
        logEntity.setOperateType(annotation.operateType());
        // 当前时间
        logEntity.setLogAt(new Date());
        // 设置ip地址
        logEntity.setIp(BrowserAndIPUtils.getBrowserAndIP(request));
        // 设置请求头信息
        logEntity.setRequestHeader(request.getHeader("User-Agent"));

        // 参数和类型
        Integer paramOrder = annotation.paramOrder();
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Parameter parameter = parameters[paramOrder];
            Class<?> type = parameter.getType();
            Object[] args = point.getArgs();

            // 记录更新操作后的新值
            if (OperateType.UPDATE == annotation.operateType()) {
                // 获得方法的入参
                getNewValueByMapper(type, args[paramOrder], annotation.tableName());
            } else if (OperateType.INSERT == annotation.operateType()) {
                // 插入操作时直接将入参设置为新值
                logEntity.setNewValue(JSONObject.toJSONString(args[paramOrder]));
            } else if (OperateType.PATCH_UPDATE == annotation.operateType()) {
                // 保存新值
                getPatchNewValueByMapper(type, args[paramOrder], annotation.tableName());
            }
        }
        if (returnValue != null) {
            if (returnValue instanceof List) {
                if (!((List<?>) returnValue).isEmpty()) {
                    logEntity.setResult(true);
                } else {
                    logEntity.setResult(false);
                }
            } else if (returnValue instanceof Boolean) {
                if ((boolean) returnValue) {
                    logEntity.setResult(true);
                } else {
                    logEntity.setResult(false);
                }
            } else if (returnValue instanceof Integer) {
                if (Integer.parseInt(returnValue.toString()) > 0) {
                    logEntity.setResult(true);
                } else {
                    logEntity.setResult(false);
                }
            } else {
                logEntity.setResult(true);
            }
        } else {
            logEntity.setResult(true);
        }
        log.info("记录日志: [ " + JSONObject.toJSON(logEntity) + " ]");
        logMapper.insert(logEntity);
        // 保存日志后清除log实体
        logMap.remove(method.hashCode());
    }

    /**
     * 执行sql查询并返回旧值
     *
     * @author wangjie
     * @date 11:27 2022年05月09日
     **/
    void getOldValueByMapper(Class<?> type, Object arg, String tableName) {
        TreeMap<Object, Object> treeMap;

        try {
            // 当type为int或者Integer类型时，直接查询数据库，否则一定是对象，直接使用对象中的id字段查询
            if (Integer.TYPE.isInstance(type.getSimpleName())) {
                // 从数据库查出旧数据
                treeMap = logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + arg);
            } else {
                Object convert = ConvertUtils.convert(arg, type);
                Field id = convert.getClass().getDeclaredField("id");
                // 设置允许访问
                id.setAccessible(true);
                // 从数据库查出旧数据
                treeMap = logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + id.get(convert));
            }
            logEntity.setOldValue(JSONObject.toJSONString(treeMap));
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(EmError.LOG_FAILD);
        }
    }

    /**
     * 执行sql查询并返回批量操作的旧值
     *
     * @author wangjie
     * @date 11:27 2022年05月09日
     **/
    void getPatchOldValueByMapper(Class<?> type, Object arg, String tableName) {
        try {
            List<TreeMap<Object, Object>> treeMaps = new ArrayList<>();

            // TODO 批量操作目前是包含idList的对象或者为List的id集合——此段代码可根据项目变化灵活修改
            if ("List".equals(type.getSimpleName())) {
                List<Integer> ids = (ArrayList) arg;
                // 从数据库查出旧数据
                ids.forEach(id -> treeMaps.add(logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + id)));
            } else {
                Object convert = ConvertUtils.convert(arg, type);
                Field idList = convert.getClass().getDeclaredField("id");
                // 设置允许访问
                idList.setAccessible(true);
                List<Integer> ids = (List<Integer>) idList.get(convert);
                // 从数据库查出旧数据
                ids.forEach(id -> treeMaps.add(logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + id)));
            }
            logEntity.setOldValue(JSONArray.toJSONString(treeMaps));
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(EmError.LOG_FAILD);
        }
    }

    /**
     * 执行sql查询并返回新值
     *
     * @author wangjie
     * @date 14:40 2022年05月09日
     **/
    void getNewValueByMapper(Class<?> type, Object arg, String tableName) {
        try {
            TreeMap<Object, Object> treeMap;
            // 当type为int或者Integer类型时，直接查询数据库，否则一定是对象，直接使用对象中的id字段查询
            if ("Integer".equals(type.getSimpleName()) || "int".equals(type.getSimpleName())) {
                // 从数据库查出旧数据
                treeMap = logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + arg);
            } else {
                Object convert = ConvertUtils.convert(arg, type);
                Field id = convert.getClass().getDeclaredField("id");
                // 设置允许访问
                id.setAccessible(true);
                // 从数据库查出新数据
                treeMap = logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + id.get(convert));
            }
            logEntity.setNewValue(JSONObject.toJSONString(treeMap));
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(EmError.LOG_FAILD);
        }
    }

    /**
     * 执行sql查询并返回批量操作的新
     *
     * @author wangjie
     * @date 11:27 2022年05月09日
     **/
    void getPatchNewValueByMapper(Class<?> type, Object arg, String tableName) {

        try {
            List<TreeMap<Object, Object>> treeMaps = new ArrayList<>();

            // TODO 批量操作目前是包含idList的对象或者为List的id集合——此段代码可根据项目变化灵活修改
            if ("List".equals(type.getSimpleName())) {
                List<Integer> ids = (ArrayList) arg;
                // 从数据库查出旧数据
                ids.forEach(id -> treeMaps.add(logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + id)));
            } else {
                Object convert = ConvertUtils.convert(arg, type);
                Field idList = convert.getClass().getDeclaredField("id");
                // 设置允许访问
                idList.setAccessible(true);
                List<Integer> ids = (List<Integer>) idList.get(convert);
                // 从数据库查出新数据
                ids.forEach(id -> treeMaps.add(logMapper.executeSql("SELECT * FROM " + tableName + " WHERE ID = " + id)));
            }
            logEntity.setNewValue(JSONArray.toJSONString(treeMaps));
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException(EmError.LOG_FAILD);
        }
    }

    /**
     * @param point
     * @return com.fangchui.mall.log.logs.LogAnnotation
     * @description 根据切点获取注解
     * @author wangjie
     * @date 10:12 2022年05月09日
     */
    LogAnnotation getAnnotation(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(LogAnnotation.class);
    }
}
