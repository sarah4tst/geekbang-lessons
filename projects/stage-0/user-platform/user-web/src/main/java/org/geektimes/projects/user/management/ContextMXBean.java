package org.geektimes.projects.user.management;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import sun.rmi.runtime.Log;

public abstract class ContextMXBean implements DynamicMBean {

  private Map<String, MBeanAttributeInfo> attributeInfoMap;
  private MBeanInfo mBeanInfo;
  private final Logger logger = Logger.getLogger(ContextMXBean.class.getName());

  public ContextMXBean() {
    this.mBeanInfo = createMBeanInfo();
    makeAttributeInfoMap();
    try {
      registerMBean();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  protected abstract MBeanInfo createMBeanInfo();

  private void makeAttributeInfoMap() {
    attributeInfoMap = new ConcurrentHashMap<>();
    for (MBeanAttributeInfo info : mBeanInfo.getAttributes()) {
        attributeInfoMap.put(info.getName(),info);
    }
  }
  private void registerMBean()
      throws Throwable {
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    // Register MBean
    ObjectName objectName = new ObjectName("SERVLET:name=" + this.getClass().getSimpleName());
    mBeanServer.registerMBean(this, objectName);
    logger.log(Level.INFO, "registerMBean success! " + objectName);
  }

  @Override
  public Object getAttribute(String attribute)
      throws AttributeNotFoundException, MBeanException, ReflectionException {
    try {
      checkAttributeSupport(attribute);
      BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), Object.class);
      for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
        if (attribute.equals(descriptor.getName())) {
          Method readMethod = descriptor.getReadMethod();
          return readMethod.invoke(this, null);
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
      checkAttributeSupport(attribute.getName());
      BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass(), Object.class);
      for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
        if (attribute.getName().equals(descriptor.getName())) {
          Method writeMethod = descriptor.getWriteMethod();
          writeMethod.invoke(this, attribute.getValue());
        }
      }
      logger.log(Log.VERBOSE,"setAttribute success, this.toString() = " + this.toString());
    } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  private void checkAttributeSupport(String attributeName) throws AttributeNotFoundException {
    if (!attributeInfoMap.containsKey(attributeName)) {
      throw new AttributeNotFoundException(attributeName);
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
    return mBeanInfo;
  }

  @Override
  public Object invoke(String actionName, Object[] params, String[] signature)
      throws MBeanException, ReflectionException {
    throw new ReflectionException(new UnsupportedOperationException(actionName));
  }
}
