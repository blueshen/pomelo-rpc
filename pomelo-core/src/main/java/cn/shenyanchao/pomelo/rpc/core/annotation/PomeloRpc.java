package cn.shenyanchao.pomelo.rpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义某个类是RPC服务
 * @author shenyanchao
 * @since 2019-03-08 10:29
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PomeloRpc {

    String group() default "";

    String name() default "";


}
