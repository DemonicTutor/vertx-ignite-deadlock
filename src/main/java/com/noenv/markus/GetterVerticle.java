package com.noenv.markus;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.shareddata.AsyncMap;

public final class GetterVerticle extends AbstractVerticle {

  private int count = 0;

  @Override
  public Completable rxStart() {
    return super.vertx.sharedData().<String, String>getAsyncMap("testMap").doOnSuccess(this::schedule).ignoreElement();
  }

  private void schedule(final AsyncMap<String, String> map) {
    super.vertx.periodicStream(1_000).toObservable()
      .map(ignore -> map)
      .doOnNext(this::execute)
      .subscribe();
  }

  private void execute(final AsyncMap<String, String> map) {
    count++;
    if (2 <= count) {
      System.err.println("did not complete");
    }
    map.rxGet("key").doOnSuccess(value -> System.err.println(deploymentID() + " '" + value + "'")).doOnTerminate(() -> count--).subscribe();
  }
}
