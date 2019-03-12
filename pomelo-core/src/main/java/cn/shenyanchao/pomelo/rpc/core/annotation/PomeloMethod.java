package cn.shenyanchao.pomelo.rpc.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 特定方法提供服务
 * @author shenyanchao
 * @since 2019-03-12 12:26
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PomeloMethod {
}
