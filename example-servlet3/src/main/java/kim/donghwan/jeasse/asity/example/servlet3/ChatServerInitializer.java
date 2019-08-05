/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kim.donghwan.jeasse.asity.example.servlet3;

import io.cettia.asity.bridge.servlet3.AsityServlet;
import kim.donghwan.jeasse.asity.example.MyJeasseAsityAction;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;

/**
 * @author Donghwan Kim
 */
@WebListener
public class ChatServerInitializer implements ServletContextListener {
  @Override
  public void contextInitialized(ServletContextEvent event) {
    MyJeasseAsityAction httpAction = new MyJeasseAsityAction();

    ServletContext context = event.getServletContext();
    Servlet servlet = new AsityServlet().onhttp(httpAction);
    ServletRegistration.Dynamic reg = context.addServlet("chat", servlet);
    reg.setAsyncSupported(true);
    reg.addMapping("/send");
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {}
}