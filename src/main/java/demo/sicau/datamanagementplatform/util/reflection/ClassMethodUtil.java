package demo.sicau.datamanagementplatform.util.reflection;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @Author beifengtz
 * @Site www.beifengtz.com
 * @Date Created in 14:17 2018/11/15
 * @Description: 通过反射获得类信息
 */
public class ClassMethodUtil {
    public static void main(String[] args) {
        Class<?> c = null;
        try{
            c = Class.forName("demo.sicau.datamanagementplatform.constants.ResourceConstants");
            //1、获取本类中的所有方法
            Method[] methods = c.getDeclaredMethods();
            System.out.println("\n本类中的所有方法：");
            for(int i=0;i<methods.length;i++){
                System.out.print("自定义：");
                getMethodInfo(methods[i]);
            }

            //2、输出全部方法：本类中的方法+父类中的方法
            Method[] methodes = c.getMethods();
            System.out.println("\n本类和父类的所有方法：");
            for(int i=0;i<methodes.length;i++){
                System.out.println("系统："+methodes[i]);
                System.out.print("自定义：");
                getMethodInfo(methodes[i]);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //获取方法信息
    public static void getMethodInfo(Method method){
        System.out.print(Modifier.toString(method.getModifiers())+" ");//修饰符
        System.out.print(method.getReturnType().getName()+" ");//返回值类型
        System.out.print(method.getName());//方法名称
        //各个参数
        Class<?> parames[] = method.getParameterTypes();
        System.out.print("(");
        for(int i=0;i<parames.length;i++){
            System.out.print(parames[i].getName()+" arg"+i);//参数名称
            if(i<parames.length-1){//添加逗号隔开
                System.out.print(",");
            }
        }
        System.out.print(")");

        //获取异常信息
        Class<?> ex[] = method.getExceptionTypes();
        if(ex.length>0){//说明有异常
            System.out.print(" throws ");
            for(int j=0;j<ex.length;j++){
                System.out.print(ex[j].getName());
                if(j<ex.length-1) System.out.print(",");
            }
        }
        System.out.println();
    }
}
