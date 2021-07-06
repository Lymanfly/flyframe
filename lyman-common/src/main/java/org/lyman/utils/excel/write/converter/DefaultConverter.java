package org.lyman.utils.excel.write.converter;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lyman.utils.excel.write.converter.abs.ObjectConverter;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Slf4j(topic = "ObjectToMapConverter")
@NoArgsConstructor
public class DefaultConverter implements ObjectConverter {

    @Override
    public Map<String, Object> convert(Object obj) {
        Map<String, Object> res = new HashMap<>();
        Class c = obj.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(c);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String n = pd.getName();
                if (!"class".equals(n)) {
                    Method getter = pd.getReadMethod();
                    if (getter != null) {
                        Object v = getter.invoke(obj);
                        res.put(n, v);
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ignore) {}
        return res;
    }

}
