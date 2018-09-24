package io.jenkins.plugins.servicenow.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtilConvertor {

	
	public static <T> T toObject(String jsonContent, Class<? extends T> clz, T prototype)
    {
        try
        {
            if (prototype == null)
            {
                prototype = clz.newInstance();
            }
            JSONObject jsonObject = new JSONObject(jsonContent);
            parseJsonObject(prototype, jsonObject);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException("could not instantiate from class " + clz, e);
        }
        catch (IllegalAccessException e)
        {
            throw new RuntimeException("could not instantiate from class. Illegal access. " + clz, e);
        }
        catch (JSONException e)
        {
            throw new RuntimeException("invalid json" + clz, e);
        }
        catch (NoSuchFieldException e)
        {
            throw new RuntimeException("could not find such a field " + clz, e);
        }
        catch (SecurityException e)
        {
            throw new RuntimeException("Security Exception. " + clz, e);
        }

        return prototype;
    }

    private static void parseJsonObject(Object bean, JSONObject jsonObject) throws JSONException, InstantiationException, IllegalAccessException,
            NoSuchFieldException, SecurityException
    {
        List<Property> props = getProperties(bean.getClass());
        
        for (Property p : props)
        {
            if (p.getSetter() == null || !jsonObject.has(p.getName()))
                continue;

            Object value = jsonObject.get(p.getName());
                
            try {
            	
				BeanUtils.setProperty(bean, p.getName(), value);
				
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
            
        }

    }
    private static List<Property> getProperties(Class<?> clazz)
    {
        List<Property> result = new ArrayList<>();
        
        for (PropertyDescriptor descriptor : PropertyUtils.getPropertyDescriptors(clazz))
        {
            if (descriptor.getReadMethod() != null && !descriptor.getName().equals("class"))
            {
                result.add(new Property(descriptor.getName(), descriptor.getReadMethod(), descriptor.getWriteMethod()));
            }
        }
        return result;

    }
}
