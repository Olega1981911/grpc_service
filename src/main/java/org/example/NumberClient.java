package org.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.grpc.NumberRequest;
import org.example.grpc.NumberResponse;
import org.example.grpc.NumberServiceGrpc;

public class NumberClient {
    private int lastNumberFromServer = 0;
    private int currentValue = 0;

    public static void main(String[] args) throws InterruptedException {
        NumberClient client = new NumberClient();
        client.start();
    }

    public void start() throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        NumberServiceGrpc.NumberServiceStub stub = NumberServiceGrpc.newStub(channel);

        NumberRequest request = NumberRequest.newBuilder().setFirstValue(0).setLastValue(30).build();
        stub.getNumbers(request, new StreamObserver<NumberResponse>() {
            @Override
            public void onNext(NumberResponse value) {
                lastNumberFromServer = value.getCurrentValue();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Stream completed");
            }
        });

        for (int i = 0; i <= 50; i++) {
            currentValue = currentValue + lastNumberFromServer + 1;
            System.out.println("currentValue: " + currentValue);
            lastNumberFromServer = 0;
            Thread.sleep(1000);
        }

        channel.shutdown();
    }
}
