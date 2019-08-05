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
package kim.donghwan.jeasse.asity.example.play2;

import io.cettia.asity.bridge.play2.AsityHttpAction;
import kim.donghwan.jeasse.asity.example.MyJeasseAsityAction;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

/**
 * @author Donghwan Kim
 */
public class ChatController extends Controller {
  private MyJeasseAsityAction httpAction = new MyJeasseAsityAction();

  @BodyParser.Of(BodyParser.Raw.class)
  public CompletionStage<Result> http(Http.Request request) {
    AsityHttpAction playAction = new AsityHttpAction();
    playAction.onhttp(httpAction);

    return playAction.apply(request);
  }
}
