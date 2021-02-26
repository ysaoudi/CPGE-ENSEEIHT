import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

public class Solution {

    public static void execExo1() {

        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, 2, 3, 5, 7);
        System.out.println("Original List : " + list);
        System.out.println("Deleting from List : " + list.remove(2));
        System.out.println("New List : " + list);
        List<Integer> unmodifiableList = Collections.unmodifiableList(list);

        // Fails:
        System.out.println("Deleting from List : " + unmodifiableList.remove(2));
        System.out.println("New List : " + unmodifiableList);
    }

    public static class ProtectionHandler implements InvocationHandler {
        HashSet<String> prohibitedMethods = new HashSet<String>();
        Object object;

        public ProtectionHandler(Object o, String... prohibitedMethods) {
            this.object = o;

            for (String method : prohibitedMethods) {
                this.prohibitedMethods.add(method);
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (prohibitedMethods.contains(method.getName())) {
                throw new UnsupportedOperationException("Illegal Operation " + method.getName() +" for object " + proxy.getClass() +" !");
            } else {
                return method.invoke(this.object, args);
            }
        }
    }

    public static void execExo2() {
        List<Integer> list = new ArrayList<Integer>();
        Collections.addAll(list, 2, 3, 5, 7);
        System.out.println("Original List : " + list);
        System.out.println("Deleting from List : " + list.remove(2));
        System.out.println("New List : " + list);
        try {
            ProtectionHandler proxy = new ProtectionHandler(list, "add");

            proxy.invoke(list, List.class.getMethod("add", new Class[]{Object.class}), new Integer[] { 1 });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //execExo1();
        //execExo2();
    }
}