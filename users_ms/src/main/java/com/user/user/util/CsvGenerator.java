package com.user.user.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.user.user.domain.athlete.AthletewDesc;

public class CsvGenerator {
     public static void leExibeArquivoCsv(AthletewDesc athletewDesc, String nomeArq){
        FileReader arq = null;
        Scanner entrada = null;
        @SuppressWarnings("unused")
        Boolean deuRuim = false;
        
        nomeArq += ".csv";

        try{
            arq = new FileReader(nomeArq);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        }
        catch (FileNotFoundException erro) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }


        //aqui tem q mudar
        try{
            System.out.printf("%4S %9S %8S %9S %7S %6S %4S %5S\n", "nome" , "sobrenome" , "Categoria" , "Posição" , "altura" , "peso" , "Idade");
            
            while (entrada.hasNext()) {
                String Nome = athletewDesc.athlete().getFirstName();;
                String Sobrenome = athletewDesc.athlete().getLastName();
                String Categoria = athletewDesc.athlete().getCategory();
                String Posicao = athletewDesc.athleteDesc().getPosition();
                Double Altura = athletewDesc.athleteDesc().getHeight();
                Double Peso = athletewDesc.athleteDesc().getWeight();
                int Idade = LocalDate.now().getYear() - athletewDesc.athlete().getBirthDate().getYear();

                System.out.printf("%-15s %-15s %-6S %9s %2.2f %2.2f %2d\n" , Nome, Sobrenome, Categoria, Posicao, Altura, Peso, Idade);
            }
        }
        catch(NoSuchElementException erro){
            System.out.println("Arquivo com problemas");
            erro.printStackTrace();
            deuRuim = true;
        }
        catch(IllegalStateException erro){
            System.out.println("Erro na leitura da arquivo");
            erro.printStackTrace();
            deuRuim = true;
        }
        finally {
            entrada.close();
            try{
                arq.close();
            }
            catch(IOException erro){
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }

            if (deuRuim = true) {
                System.exit(1);
            }
        }
    }


    // aqui tem q mudar
    public static void gravaArquivoCsv(List<AthletewDesc> athletes, String nomeArquivo) {
        FileWriter arq = null;
        Formatter saida = null;
        @SuppressWarnings("unused")
        Boolean deuRuim = false;

        nomeArquivo += ".csv";

        try{
            arq = new FileWriter(nomeArquivo);
            saida = new Formatter(arq);
        }
        catch(IOException erro){
            System.out.println("Erro ao abrir o arquivo");
            erro.printStackTrace();
            System.exit(1);
        }


        try{
            for(AthletewDesc athletewDesc : athletes){
                int Idade = LocalDate.now().getYear() - athletewDesc.athlete().getBirthDate().getYear();  
                saida.format("%-15s;%-15s;%-6S;%9s;%2.2f;%2.2f;%2d\n" , athletewDesc.athlete().getFirstName(),
                                athletewDesc.athlete().getLastName(), athletewDesc.athlete().getCategory(),
                                athletewDesc.athleteDesc().getPosition(), athletewDesc.athleteDesc().getHeight(),
                                athletewDesc.athleteDesc().getWeight(), Idade
                            );
            }
        }
        catch(FormatterClosedException erro){
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
            deuRuim = true;
        }

        finally {
            saida.close();
            try{
                arq.close();
            }
            catch(IOException erro){
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim = true) {
                System.exit(1);
            }
        }
    }
}
