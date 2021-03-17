package org.geektimes.projects.user.web.listener;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.geektimes.projects.user.web.controller.RegisterController;

public class JMXContextInitializerListener implements ServletContextListener {

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try {
      registerMBean();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  private void registerMBean()
      throws Throwable {
    MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
    // Register MBean
    mBeanServer.registerMBean(new RegisterController(), new ObjectName("SERVLET:name=RegisterController"));
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
