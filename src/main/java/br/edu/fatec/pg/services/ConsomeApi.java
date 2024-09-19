package br.edu.fatec.pg.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsomeApi {

private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:streamGenerateContent?alt=sse&key=AIzaSyDpItsubYa061gPKnoZxuln7nhKqPZUmMI";

private static final Pattern RESPOSTA_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");
 
public static String fazerPergunta(String pergunta) throws IOException, InterruptedException{
    try {
        String jsonRequest = gerarJsonRequest(pergunta);
        String respostaJson = enviarRequisicao(jsonRequest);
        return extrairResposta(respostaJson);
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();  // Exibe o erro
        return "Ocorreu um erro ao processar a pergunta.";
    }
  }

  public static String gerarJsonRequest(String pergunta) {
    String promptFormatado = "Question: " + pergunta;
    return "{"
        + "\"contents\": ["
        + "  {"
        + "    \"parts\": ["
        + "      {"
        + "        \"text\": \"" + promptFormatado + "\""
        + "      }"
        + "    ]"
        + "  }"
        + "]"
        + "}";
  }

  public static String enviarRequisicao(String jsonRequest) throws IOException, InterruptedException{
    try {  
HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            //System.out.println("Resposta da API: " + response.body());  // Debug
            return response.body();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String extrairResposta(String respostaJson){
    StringBuilder resposta = new StringBuilder();
    for (String linha: respostaJson.lines().toList()){
      Matcher matcher = RESPOSTA_PATTERN.matcher(linha);
      if (matcher.find()) {
        resposta.append(matcher.group(1)).append(" ");
      }
    }
        String respostaFinal = resposta.toString().trim();
        // Remove caracteres especiais (exceto letras, números e espaços)
        respostaFinal = respostaFinal.replaceAll("[^\\p{L}\\p{N}\\s:.]", "");
        respostaFinal = respostaFinal.replaceAll("nn","\n");    
        return respostaFinal = respostaFinal.substring(0, respostaFinal.length() - 1);
  }
}

