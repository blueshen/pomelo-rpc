package cn.shenyanchao.pomelo.rpc.util;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;

/**
 * @author shenyanchao
 */
public class KryoUtils {

    private static final List<Class> classList = new ArrayList<Class>();
    private static final List<Serializer> serializerList = new ArrayList<Serializer>();
    private static final List<Integer> idList = new ArrayList<Integer>();
    private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            int size = idList.size();
            for (int i = 0; i < size; i++) {
                kryo.register(classList.get(i), serializerList.get(i), idList.get(i));
            }
            kryo.setRegistrationRequired(false);
            kryo.setReferences(false);
            return kryo;
        }
    };

    private KryoUtils() {
    }

    /**
     * @param className
     * @param serializer
     * @param id
     */
    public static synchronized void registerClass(Class className, Serializer serializer, int id) {
        classList.add(className);
        serializerList.add(serializer);
        idList.add(id);
    }

    /**
     * @return
     */
    public static Kryo getKryo() {
        return kryos.get();
    }
}
