Reproducer to show a thread deadlock with ignite 2.11 because they switched their default behavior executing IgniteFuture no longer on stripe threadpool but java's ForkJoin. 

Run/Debug config in IntelliJ requires vm-options to be set: `-XX:ActiveProcessorCount=1`

https://ignite.apache.org/docs/latest/key-value-api/basic-cache-operations#asynchronous-execution
