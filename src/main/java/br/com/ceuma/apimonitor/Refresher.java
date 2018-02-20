package br.com.ceuma.apimonitor;

import br.com.ceuma.apimonitor.model.Api;
import br.com.ceuma.apimonitor.model.RestTemplateBuilder;
import br.com.ceuma.apimonitor.model.Servidor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.*;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import com.google.gson.internal.LinkedTreeMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by marcus on 10/02/18.
 */
public class Refresher extends TimerTask {

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    private void refresh() throws ExecutionException, InterruptedException {
        System.out.println("CARREGANDO SERVIDORES");
        List<Servidor> servidores = new ArrayList<>();
        Object restTemplate = new RestTemplateBuilder().build().getForObject("https://api-manager-42907.firebaseio.com/servidores.json", Object.class);
        Map<String, Object> response = (LinkedHashMap) restTemplate;

        for (Map.Entry<String, Object> o : response.entrySet()) {
            servidores.add(new Servidor(o.getKey(), (LinkedHashMap<String, Object>) o.getValue()));
        }

        System.out.println(String.format("%d SERVIDORES CARREGADOS", servidores.size()));
        pingInServers(servidores);

    }


    private void pingInServers(List<Servidor> servidores) throws ExecutionException, InterruptedException {
        for (Servidor s: servidores) {
            s.getApis().stream().forEach(e -> {
                try {
                    System.out.println(String.format("VERIFICANDO API: %s", e.getNome()));
                    e.setOnline(isOnline(e.getHost(s)));
                    e.setUltimaVerificacao(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
                    update(s,e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                } catch (ExecutionException e1) {
                    e1.printStackTrace();
                }
            });
        }
    }

    private void update(Servidor s, Api api) throws ExecutionException, InterruptedException {
        System.out.println(String.format("ATUALIZANDO API: %s", api.getNome()));
        Map<String, Object> apiMap = new ObjectMapper().convertValue(api, Map.class);

        Task<Void> voidTask = firebaseDatabase.getReference().child("servidores/" + s.getCodigo() + "/apis/").updateChildren(apiMap);
        Tasks.await(voidTask);
        System.out.println(String.format("API: %s ATUALIZADA", api.getNome()));
    }

    private Boolean isOnline(String host) throws IOException, InterruptedException {
        Process exec = Runtime.getRuntime().exec("curl -i " + host + " --max-time 15");
        exec.waitFor();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(exec.getInputStream()));

        String line = "";
        while ((line = reader.readLine())!= null) {
            if(line.contains("200 OK"))
                return true;
        }

        if(line == null || line.equals("")) {
            System.out.println("API ESTÁ OFFLINE");
            return false;
        }

        return false;
    }

    @Override
    public void run() {
        try {
            this.refresh();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
