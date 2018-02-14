package br.com.ceuma.apimonitor;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;

public @Data class Api {

    private String codigo;
    private String contextPath;
    private String endPointTester;
    private String nome;
    private Boolean online;
    private Integer porta;
    private String ultimaVerificacao;
    private String versao;

    public Api(String codigo, LinkedHashMap<String, Object> params){
        this.codigo = codigo;
        this.contextPath = params.get("contextPath").toString();
        this.nome = params.get("nome").toString();
        this.endPointTester = params.get("endPointTester").toString();
        this.online = Boolean.getBoolean(params.get("online").toString());
        this.porta = Integer.valueOf(Double.valueOf(params.get("porta").toString()).intValue());
        this.versao = params.get("versao").toString();
    }

    public String getHost(Servidor s){
        String host = String.format("%s:%d%s%s",s.getEndereco(),this.getPorta(),this.getContextPath(), this.getEndPointTester());
        return host;
    }

}
