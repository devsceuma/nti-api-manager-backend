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
        if(args.length == 0){
            throw new RuntimeException("Você precisa especificar o caminho da Chave (Google Cloud Plataform)");
        }

        Timer t = new Timer();

        GoogleCredentials googleCredential = GoogleCredentials.fromStream(new FileInputStream(args[0]));

        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(googleCredential).setDatabaseUrl("https://api-manager-42907.firebaseio.com/").build();
        FirebaseApp.initializeApp(options);
        Refresher r = new Refresher();
        t.scheduleAtFixedRate(r,0,1000);

        System.out.println();
    }
}
