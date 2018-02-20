package br.com.ceuma.apimonitor.model;

import lombok.Data;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by markiing on 15/02/18.
 */
public @Data
class RestTemplateBuilder {

    private String username;
    private String password;
    private Integer port;
    private String host;

    private boolean useProxy = false;


    public RestTemplateBuilder builder(){
        return this;
    }

    public RestTemplateBuilder withProxy(){
        this.username = "4580";
        this.password = "cartagenes12";
        this.port = 3128;
        this.host = "proxy.ceuma.edu.br";
        this.useProxy = true;
        return this;
    }

    public RestTemplate build(){
        if(this.useProxy){
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(new AuthScope(this.host, this.port), new UsernamePasswordCredentials(this.username, this.password));

            HttpHost myProxy = new HttpHost(host,port);
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();
            clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(provider);

            HttpClient client = clientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(client);

            return new RestTemplate(factory);
        }else{
            return new RestTemplate();
        }
    }


}
