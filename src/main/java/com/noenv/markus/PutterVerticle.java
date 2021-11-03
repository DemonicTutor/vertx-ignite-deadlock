package com.noenv.markus;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.rxjava3.core.AbstractVerticle;

public final class PutterVerticle extends AbstractVerticle {

  @Override
  public Completable rxStart() {
    return vertx.sharedData().rxGetAsyncMap("testMap").flatMapCompletable(map -> map.rxPut("key", "value"));
  }
}
