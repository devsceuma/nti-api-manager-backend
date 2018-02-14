package br.com.ceuma.apimonitor;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by marcus on 10/02/18.
 */
public class Main {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
            if (args.length != 1) {
                throw new RuntimeException("Você precisa especificar o caminho da KEY do Google Cloud Platform");
            }
            GoogleCredentials googleCredential = GoogleCredentials.fromStream(new FileInputStream(args[0]));
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(googleCredential).setDatabaseUrl("https://api-manager-42907.firebaseio.com/").build();
            FirebaseApp.initializeApp(options);
            new Timer().scheduleAtFixedRate(new Refresher(), 0, 50000); //5 MINUTOS DE INTERVALO
    }
}
