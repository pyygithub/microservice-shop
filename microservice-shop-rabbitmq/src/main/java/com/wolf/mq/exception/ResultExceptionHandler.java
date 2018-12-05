package com.wolf.mq.exception;



import com.wolf.base.Result;
import com.wolf.common.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用领域result异常处理类
 */
public class ResultExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(ResultExceptionHandler.class);

    /**
     * 对调用领域的结果的code值 统一处理
     * @param result Result结果
     * @param description  对调用领域接口的描述 eg：perApp调用enc获取就诊信息
     * @date 2018-7-5 15:18:16
     * @author itw_chenghf
     */
    public static void handler(Result result, String description) {
        if(result ==null){
            logger.error(description+"-错误:{}",result.getMsg());
            throw new RabbitmqException("2","rest服务错误");
        }
        if(result.getCode() == 200){   //成功：记录日志
            logger.info(description+"-成功");
        }else if(result.getCode() == 500){ //业务异常
            logger.info(description+"-异常:{}",result.getMsg());
            throw new RabbitmqException("1",result.getMsg());
        }else{  //系统异常
            logger.error(description+"-错误:{}",result.getMsg());
            throw new RabbitmqException("2",description+"-错误");
        }
    }
    
}
