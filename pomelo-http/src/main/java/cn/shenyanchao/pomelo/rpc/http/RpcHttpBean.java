package cn.shenyanchao.pomelo.rpc.http;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author shenyanchao
 */
public class RpcHttpBean implements Serializable {

    private static final long serialVersionUID = 856548453845561188L;

    private Object object;

    private String httpType;

    private String returnType;

    public RpcHttpBean(Object object, String httpType, String returnType) {
        super();
        this.object = object;
        this.httpType = httpType;
        this.returnType = returnType;
    }

    /**
     * @return the object
     */
    public Object getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * @return the httpType
     */
    public String getHttpType() {
        return httpType;
    }

    /**
     * @param httpType the httpType to set
     */
    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    /**
     * @return the returnType
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * @param returnType the returnType to set
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("RpcHttpBean{");
        sb.append("object=").append(object);
        sb.append(", httpType='").append(httpType).append('\'');
        sb.append(", returnType='").append(returnType).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RpcHttpBean that = (RpcHttpBean) o;
        return Objects.equals(object, that.object) &&
                Objects.equals(httpType, that.httpType) &&
                Objects.equals(returnType, that.returnType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, httpType, returnType);
    }
}
