package org.example;

import org.example.grpc.*;
import io.grpc.stub.StreamObserver;


public class NumberServer extends NumberServiceGrpc.NumberServiceImplBase{

    @Override
    public void getNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        int firstValue = request.getFirstValue();
        int lastValue = request.getLastValue();

        for (int i = firstValue; i <= lastValue; i++) {
            NumberResponse response = NumberResponse.newBuilder()
                    .setCurrentValue(i)
                    .build();

            responseObserver.onNext(response);

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        responseObserver.onCompleted();
    }
}
