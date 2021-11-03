package com.noenv.markus;

import io.reactivex.rxjava3.core.Completable;
import io.vertx.core.VertxOptions;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.rxjava3.CompletableHelper;
import io.vertx.rxjava3.core.Vertx;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

@RunWith(VertxUnitRunner.class)
public class ReproducerTest {

  @Test
  public void shouldWork(final TestContext ctx) {
    // GIVEN
    final var put = PutterVerticle.class.getCanonicalName();
    final var get = GetterVerticle.class.getCanonicalName();

    // SETUP
    Vertx.clusteredVertx(new VertxOptions())
      .flatMapCompletable(
        vertx -> Completable.complete()
          .andThen(vertx.rxDeployVerticle(put).ignoreElement())
        // THIS ONE WORKS .andThen(vertx.rxDeployVerticle(get).ignoreElement())
      )
      .andThen(Completable.defer(() ->
        Vertx.clusteredVertx(new VertxOptions())
          .flatMapCompletable(
            vertx -> Completable.complete()
              // THIS ONE DEADLOCKS
              .andThen(vertx.rxDeployVerticle(get).ignoreElement())
          ))
      )
      .delay(1, TimeUnit.MINUTES)
      .subscribe(CompletableHelper.toObserver(ctx.asyncAssertSuccess()));
  }
}
