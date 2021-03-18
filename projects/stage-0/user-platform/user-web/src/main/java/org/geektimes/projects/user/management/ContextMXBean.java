package org.geektimes.projects.user.management;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

public abstract class ContextMXBean implements DynamicMBean {

  protected Map<String, MBeanAttributeInfo> attributeInfoMap = new ConcurrentHashMap<>();
  protected MBeanInfo dMBeanInfo;
  protected MBeanAttributeInfo[] mBeanAttributeInfos;

  public ContextMXBean() {
    createMBeanAttributeInfos();
    createMBeanInfo();
    makeAttributeInfoMap(mBeanAttributeInfos);
  }

  protected abstract void createMBeanInfo();
  protected abstract void createMBeanAttributeInfos();

  private void makeAttributeInfoMap(MBeanAttributeInfo[] infos) {
    for (MBeanAttributeInfo info : infos) {
        attributeInfoMap.put(info.getName(),info);
    }
  }

  @Override
  public Object getAttribute(String attribute)
      throws AttributeNotFoundException, MBeanException, ReflectionException {
    try {
      if (attributeInfoMap.containsKey(attribute)) {
        BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), Object.class);
        for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
          if (attribute.equals(descriptor.getName())) {
            Method readMethod = descriptor.getReadMethod();
            return readMethod.invoke(this, null);
          }
        }
      }
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    throw new AttributeNotFoundException(attribute);
  }

  @Override
  public void setAttribute(Attribute attribute)
      throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException,
      ReflectionException {
    try {
      if (!attributeInfoMap.containsKey(attribute.getName())) {
        throw new AttributeNotFoundException(attribute.getName());
      }
      BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), Object.class);
      for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
        if (attribute.getName().equals(descriptor.getName())) {
          Method writeMethod = descriptor.getWriteMethod();
          writeMethod.invoke(this, attribute.getValue());
        }
      }
      System.out.println("*** setAttribute success, this.toString() = " + this.toString());
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  public AttributeList getAttributes(String[] attributes) {
    AttributeList result = new AttributeList();
    Arrays.stream(attributes).filter(attr -> attributeInfoMap.containsKey(attr)).forEach(attributeName-> {
      try {
        Object o = getAttribute(attributeName);
        result.add(new Attribute(attributeName, o));
      } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
        e.printStackTrace();
      }
    });
    return result;
  }

  @Override
  public AttributeList setAttributes(AttributeList attributes) {
    if (attributes == null || attributes.size() == 0) {
      return null;
    }
    AttributeList result = new AttributeList();
    attributes.stream().filter(o -> o instanceof Attribute).map(o -> (Attribute) o).forEach(attribute -> {
      try {
        setAttribute(attribute);
        result.add(attribute);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });

    return result;
  }

  @Override
  public MBeanInfo getMBeanInfo() {
    return dMBeanInfo;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature)
      throws MBeanException, ReflectionException {
    throw new ReflectionException(new UnsupportedOperationException(actionName));
  }
}
