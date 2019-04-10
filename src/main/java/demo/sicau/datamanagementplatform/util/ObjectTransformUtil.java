package demo.sicau.datamanagementplatform.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 13:37 2018/11/9
 * @Description: 对象转换工具类
 */
public class ObjectTransformUtil {

    /**
     * Map转换为Object
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            throw new NullPointerException();
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * Object转换为Map
     * @param obj
     * @return
     */
    public static Map<?, ?> objectToMap(Object obj) throws NullPointerException{
        if(obj == null)
            throw new NullPointerException();
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    /**
     * Map转换为Json字符串
     * @param map
     * @return
     */
    public static String mapToJson(Map<String,Object> map) throws NullPointerException{
        if (map == null) {
            throw new NullPointerException();
        }
        return new Gson().toJson(map);
    }

    /**
     * json转换为Map
     * @param json
     * @return
     */
    public static Map<?,?> jsonToMap(String json){
        if(json == null){
            throw new NullPointerException();
        }
        Type type = new TypeToken<Map<?, ?>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    /**
     * list转换为json
     * @param list 集合
     * @return
     */
    public static String listToJson(List<?> list){
        Gson gson = new Gson();
        return gson.toJson(list);
    }

    /**
     * json转换为list
     * @param json
     * @return
     */
    public static List<?> jsonToList(String json){
        Gson gson = new Gson();
        List<?> list = gson.fromJson(json, new TypeToken<List<?>>() {
        }.getType());
        return list;
    }
}
