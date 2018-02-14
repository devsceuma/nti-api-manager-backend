package br.com.ceuma.apimonitor;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by marcus on 10/02/18.
 */
public @Data class Servidor {

    private String codigo;
    private String endereco;
    private String nome;
    private List<Api> apis = new ArrayList<Api>();

    public Servidor(String key, LinkedHashMap<String, Object> params){
        this.codigo = key;
        this.nome = params.get("nome").toString();
        this.endereco = params.get("endereco").toString();

        try {
            Map<String, Object> apis = (LinkedHashMap) params.get("apis");

            for (Map.Entry<String, Object> o : apis.entrySet()) {
                this.apis.add(new Api(o.getKey(), (LinkedHashMap<String, Object>) o.getValue()));
            }
        }catch (ClassCastException ex){
            List apis = (ArrayList) params.get("apis");

            for (Integer i = 0; i < apis.size(); i++) {
                this.apis.add(new Api(i.toString(), (LinkedHashMap<String, Object>) apis.get(i)));
            }
        }

    }
}
