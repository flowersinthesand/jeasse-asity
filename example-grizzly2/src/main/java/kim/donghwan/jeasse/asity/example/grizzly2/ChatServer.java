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
package kim.donghwan.jeasse.asity.example.grizzly2;

import io.cettia.asity.bridge.grizzly2.AsityHttpHandler;
import kim.donghwan.jeasse.asity.example.MyJeasseAsityAction;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;

/**
 * @author Donghwan Kim
 */
public class ChatServer {
  public static void main(String[] args) throws Exception {
    MyJeasseAsityAction httpAction = new MyJeasseAsityAction();

    HttpServer httpServer = HttpServer.createSimpleServer();
    ServerConfiguration config = httpServer.getServerConfiguration();
    config.addHttpHandler(new AsityHttpHandler().onhttp(httpAction), "/send");
    config.addHttpHandler(new CLStaticHttpHandler(ChatServer.class.getClassLoader(), "static/"));
    httpServer.start();

    System.in.read();
  }
}
