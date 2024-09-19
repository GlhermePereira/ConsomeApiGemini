package br.edu.fatec.pg;
import br.edu.fatec.pg.services.*;
import java.io.IOException;
import java.util.Scanner;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    String opcao = "";
    Scanner ler = new Scanner(System.in);
    
    while (!opcao.equals("sair")){
      System.out.println("Digite uma pergunta para o Gemini ou escreva 'sair': \n");
      opcao = ler.nextLine();
    try {  
    String resposta = ConsomeApi.fazerPergunta(opcao);
    System.out.println("\n");
    System.out.println(resposta);
    System.out.println("\n");
    } catch (IOException | InterruptedException e) {
      System.out.println(e);
      // TODO: handle exception
    }
    }
    ler.close();
    }
}
