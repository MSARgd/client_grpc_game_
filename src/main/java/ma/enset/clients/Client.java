package ma.enset.clients;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.subs.Game;
import ma.enset.subs.GameServiceGrpc;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class Client  {
    public static void main(String[] args) throws IOException {
       ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",2023)
               .usePlaintext()
               .build();
       GameServiceGrpc.GameServiceStub gameServiceStub = GameServiceGrpc.newStub(managedChannel);

        StreamObserver<Game.guessRequest> guessRequestStreamObserver = gameServiceStub.fullDirectionStream(new StreamObserver<Game.guessResponse>() {
            @Override
            public void onNext(Game.guessResponse guessResponse) {
                System.out.println(guessResponse);
                if(guessResponse.getIsTrue()){
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
            @Override
            public void onCompleted() {
                System.out.println("..........End");
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int randomNumber = new Random().nextInt(101);
                Game.guessRequest guessRequest = Game.guessRequest.newBuilder()
                        .setNumber(randomNumber)
                        .build();
                System.out.println(guessRequest.getNumber());
                guessRequestStreamObserver.onNext(guessRequest);

            }
        }, 0, 1000);

//        while(true){
//            int randomNumber = new Random().nextInt(101);
//            Game.guessRequest guessRequest = Game.guessRequest.newBuilder()
//                    .setNumber(randomNumber)
//                    .build();
//            System.out.println(guessRequest.getNumber());
//            guessRequestStreamObserver.onNext(guessRequest);
//
//
//        }
    }
}

