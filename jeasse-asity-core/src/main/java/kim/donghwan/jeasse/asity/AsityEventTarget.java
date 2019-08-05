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
package kim.donghwan.jeasse.asity;

import info.macias.sse.EventTarget;
import info.macias.sse.events.MessageEvent;
import io.cettia.asity.http.HttpStatus;
import io.cettia.asity.http.ServerHttpExchange;

/**
 * EventTarget for Asity
 *
 * @author Donghwan Kim
 */
public class AsityEventTarget implements EventTarget {
  private final ServerHttpExchange http;

  public AsityEventTarget(ServerHttpExchange http) {
    this.http = http;
  }

  @Override
  public EventTarget ok() {
    http.setStatus(HttpStatus.OK)
      .setHeader("Content-Type", "text/event-stream")
      .setHeader("Cache-Control", "no-cache")
      .setHeader("Connection", "keep-alive");
    return this;
  }

  @Override
  public EventTarget open() {
    http.write("event: open\n\n");
    return this;
  }

  @Override
  public EventTarget send(String event, String data) {
    return send(new MessageEvent.Builder().setData(data).setEvent(event).build());
  }

  @Override
  public EventTarget send(MessageEvent messageEvent) {
    http.write(messageEvent.toString());
    return this;
  }

  @Override
  public void close() {
    http.end();
  }
}
