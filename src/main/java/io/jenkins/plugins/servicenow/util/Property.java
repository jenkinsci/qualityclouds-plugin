package io.jenkins.plugins.servicenow.util;

import java.lang.reflect.Method;

public class Property {
	
	private String name;
    private Method getter;
    private Method setter;

    public Property()
    {
    }

    Property(String name, Method getter, Method setter)
    {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
    }

    String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Method getGetter()
    {
        return getter;
    }

    public void setGetter(Method getter)
    {
        this.getter = getter;
    }

    Method getSetter()
    {
        return setter;
    }

    public void setSetter(Method setter)
    {
        this.setter = setter;
    }

    public Class<?> getType()
    {
        if (getter != null)
            return getter.getReturnType();
        else if (setter != null)
        {
            return setter.getParameterTypes()[0];
        }
        else
            return Object.class;
    }

    @Override
    public String toString()
    {
        return "Property(" + name + ", " + getter + ", " + setter + ")";
    }
}
