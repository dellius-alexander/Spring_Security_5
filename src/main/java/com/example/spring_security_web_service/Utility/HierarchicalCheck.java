package com.example.spring_security_web_service.Utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Checks an object for inner object and check inner object attributes for null values.
 * Function create a hierarchical check of object values and removes null objects from
 * the return object.
 * @see Map
 */
public class HierarchicalCheck<T extends Map<String, Object>> implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(HierarchicalCheck.class);
    protected  T payLoad = (T) new HashMap<String, Object>();
    /**
     * Checks if an object or list of objects are null and returns the first non null object.
     * User only with same DataType Objects to find non-null object in list of objects.
     * @param object a single Object, a list or array
     * @return the first Not_Null object
     */
    public List<? extends Object> hierarchicalCheck(Object[] object){
        log.info("Testing Object[]: {}",object);
        List<Object> list = new ArrayList<>();
        // check if object if an array of objects
        if (object instanceof Object[] && ((Object[]) object).length > 1){
            for (Object o : (Object[]) object ) {
                log.info("Testing Object: {}", o);
                if (o == null){continue;}
                log.info("Not Null Object Found: {}", o);
                list.addAll(List.of(hierarchicalCheck(o)));
            }
        }
        // return the first Not_Null object we find
        else if(object != null){
            log.info("Not Null Object[] Found: {}",object);
            return List.of(object);
        }
        else {
            return List.of("null");
        }
        return list;
    }
    //////////////////////////////////////////////////////////////////////
    /**
     * Checks if an object is null and returns the Not_Null object.
     * @param object a single Object
     * @return the first Not_Null object
     */
    public Object hierarchicalCheck(Object object){
        log.info("Testing: {}",object);
        if (object instanceof Object[] && ((Object[]) object).length > 1 && object != null){
            log.info("Found Array Object[]: {}",object);
            return hierarchicalCheck((Object[])  object);
        }
        else if(object != null){
            log.info("Not Null Object Found: {}",object);
            return object;
        }
        return null;
    }

    /**
     * Deconstructs Map objects and checks each part for null objects and return all
     * non-null objects into separate Map.
     * @param payLoad
     * @return {@literal  T extends Map<String, Object>}
     */
    public T hierarchicalCheck(T payLoad){
        log.info("Testing Map (T): {}",payLoad.values().stream().collect(Collectors.toList()));
        int cnt=0;
        if (  payLoad.size() > 0 ){
            Set set = payLoad.entrySet();  // convert to set so we can traverse Set
            Iterator it = set.iterator();  // get iterator of Set to iterate over each set
            while (it.hasNext()){ // check if we have any objects in the set
                // get an entry set if we have objects in the set
                T.Entry entry = (T.Entry) it.next();
                log.info("\nMap<index, Key, Value> [{}: =>\nKey: {}\nValue: {}]\n", cnt, entry.getKey(), entry.getValue());
                // replace the old object with new object if its not null
                hierarchicalCheck( entry.getValue());
                payLoad.replace( entry.getKey().toString(), entry.getValue(),  hierarchicalCheck( entry.getValue()));
            }
        }
        return payLoad;
    }

////////////////////////////////////////////////////////////////////////////////////////
//    public static void main(String[] args) {
//        try {
//            SearchBook sb = new SearchBook("title","author","course");
//            HierarchicalCheck lg = new HierarchicalCheck();
//            lg.payLoad.put("objects", new Object[]{
//                    "Hello World 1",
//                    null,
//                    sb,
//                    "Hello World 2"});
//            log.info("Dirty List: {}", lg.payLoad.values().stream().collect(Collectors.toList()));
//            lg.payLoad = lg.hierarchicalCheck(lg.payLoad);
//            log.info("Cleaned List: {}", lg.payLoad.values().stream().collect(Collectors.toList()));
//        } catch (Exception e) {
//            log.error(e.getMessage());
//            e.printStackTrace();
//        }
//    }

}
