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
package kim.donghwan.jeasse.asity.example;

import info.macias.sse.EventBroadcast;
import info.macias.sse.EventTarget;
import info.macias.sse.events.MessageEvent;
import io.cettia.asity.action.Action;
import io.cettia.asity.http.HttpStatus;
import io.cettia.asity.http.ServerHttpExchange;
import kim.donghwan.jeasse.asity.AsityEventTarget;

import java.io.IOException;
import java.util.Scanner;

/**
 * An example web fragment for Jeasse Asity.
 * <p/>
 * Most code snippets are copied from the original example in jEaSSE.
 *
 * @author Donghwan Kim
 * @see <a href="https://github.com/mariomac/jeasse/blob/master/examples/">jEaSSE examples</a>
 */
public class MyJeasseAsityAction implements Action<ServerHttpExchange> {
  private EventBroadcast broadcaster = new EventBroadcast();

  @Override
  public void on(ServerHttpExchange http) {
    switch (http.method()) {
      case GET:
        EventTarget eventTarget = new AsityEventTarget(http);
        try {
          broadcaster.addSubscriber(eventTarget, new MessageEvent.Builder().setData("*** Welcome " +
            "to the chat server ***").build());
        } catch (IOException e) {
          e.printStackTrace();
        }
        break;
      case POST:
        http.<String>onbody(body -> {
          Scanner scanner = new Scanner(body);
          StringBuilder sb = new StringBuilder();
          while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
          }

          broadcaster.broadcast("message", dirtyJsonParse(sb.toString()));
          http.end();
        }).readAsText();
        break;
      default:
        http.setStatus(HttpStatus.METHOD_NOT_ALLOWED).end();
        break;
    }
  }

  private static String dirtyJsonParse(String json) {
    String senderChunk = "\"sender\":\"";
    String messageChunk = "\"message\":\"";
    String sender = json.substring(json.indexOf(senderChunk) + senderChunk.length(),
      json.indexOf("\"," + messageChunk));
    String message = json.substring(json.indexOf(messageChunk) + messageChunk.length(),
      json.indexOf("\"}"));
    if ("".equals(sender.trim())) sender = "Anonymous";
    return sender + " says: " + message;
  }
}