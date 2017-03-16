package com.bdd.pojo;

import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @Description
 * @Author TenSong
 * @Date 2017/3/16 11:26
 */
public class ExceptionParam {
    private Integer id;

    public ExceptionParam(){}
    public ExceptionParam(String msg, Exception e){
        int id;
        List<StackTraceElement> stackTraceElements = Arrays.asList(e.getStackTrace());
        if (CollectionUtils.isEmpty(stackTraceElements)) {
            id = msg.hashCode();
        } else {
            StackTraceElement element = stackTraceElements.get(0);
            id = (element.getClassName() + element.getMethodName() + element.getLineNumber()).hashCode();
        }

        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ExceptionParam) {
            ExceptionParam param = (ExceptionParam) obj;
            System.out.println("equal"+ param.id);
            return (id.equals(param.id));
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
