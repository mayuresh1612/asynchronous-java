package com.java.asynchronous;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class AsynchronousJavaApplication {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    Future<Double> priceGoogle = getAsyncStockPrice("GOOGL");
    log.info("1st run");
    Future<Double> priceMCRL = getAsyncStockPrice("MCRL");
    log.info("2nd run");
    Future<Double> priceApple = getAsyncStockPrice("APPL");
    log.info("3rd run");

    log.info("priceGoogle " + priceGoogle.get());
    log.info("priceMCRL " + priceMCRL.get());
    log.info("priceApple " + priceApple.get());
  }

  private static Future<Double> getAsyncStockPrice(String symbol) {
    CompletableFuture<Double> futureStockPrice = new CompletableFuture<>();
    new Thread(
            () -> {
              try {
                Thread.sleep(1000);
                log.info("Future called :: " + symbol);
                futureStockPrice.complete(getPrice(symbol));
              } catch (Exception cause) {
                futureStockPrice.completeExceptionally(cause);
              }
            })
        .start();

    return futureStockPrice;
  }

  private static double getPrice(String symbol) {
    double value = ThreadLocalRandom.current().nextDouble(.0001, .001) * symbol.hashCode();
    // Truncate to 2 decimal place
    return Math.floor(value * 100) / 100;
  }
}
