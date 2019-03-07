/**
 *
 */
package cn.shenyanchao.pomelo.rpc.core.protocol;

import cn.shenyanchao.pomelo.rpc.core.bytebuffer.RpcByteBuffer;
import cn.shenyanchao.pomelo.rpc.core.message.Message;

/**
 * rpc 协议接口
 *
 * @author shenyanchao
 */
public interface RpcProtocol {

    /**
     * 编码
     *
     * @param message
     * @param byteBufferWrapper
     *
     * @return
     *
     * @throws Exception
     */
    RpcByteBuffer encode(Message message, RpcByteBuffer byteBufferWrapper) throws Exception;

    /**
     * 解码
     *
     * @param wrapper
     * @param errorObject
     * @param originPos
     *
     * @return
     *
     * @throws Exception
     */
    Message decode(RpcByteBuffer wrapper, Message errorObject, int... originPos) throws Exception;
}
