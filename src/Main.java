import com.google.gson.Gson;
import modelos.Moeda;
import modelos.MoedaApi;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitor = new Scanner(System.in);
        String busca = "";

        String key = "ac7668a84caf2983cee1b6b8";
        String mainCurrency = "";
        String targetCurrency = "";

        do {
            System.out.println(String.format("""
                **********************************
                Bem-vindo/a ao Conversor de Moeda

                Selecione uma opção válida:

                1) Dólar => Peso argentino
                2) Peso argentino => Dólar
                3) Dólar => Real brasileiro
                4) Real brasileiro => Dólar
                5) Dólar => Peso colombiano
                6) Peso colombiano => Dólar
                7) Boliviano boliviano => Peso chileno
                8) Peso chileno => Boliviano boliviano

                0) Sair

                **********************************
                """
            ));
            busca = leitor.nextLine();

            if (busca.equals("0") || (busca.equals("1") || busca.equals("2") || busca.equals("3") || busca.equals("4") || busca.equals("5") || busca.equals("6") || busca.equals("7") || busca.equals("8"))) {
                switch (busca) {
                    case "1":
                        mainCurrency = "USD";
                        targetCurrency = "ARS";
                        break;
                    case "2":
                        mainCurrency = "ARS";
                        targetCurrency = "USD";
                        break;
                    case "3":
                        mainCurrency = "USD";
                        targetCurrency = "BRL";
                        break;
                    case "4":
                        mainCurrency = "BRL";
                        targetCurrency = "USD";
                        break;
                    case "5":
                        mainCurrency = "USD";
                        targetCurrency = "COP";
                        break;
                    case "6":
                        mainCurrency = "COP";
                        targetCurrency = "USD";
                        break;
                    case "7":
                        mainCurrency = "BOB";
                        targetCurrency = "CLP";
                        break;
                    case "8":
                        mainCurrency = "CLP";
                        targetCurrency = "BOB";
                        break;
                    case "0":
                        System.out.println("\nSaindo do sistema...");
                        return;
                    default:
                        System.out.println("Opção inválida! Por favor, selecione uma opção válida.");
                        break;
                }

                System.out.println("Digite o valor que deseja converter: ");
                try {
                    Double amount = leitor.nextDouble();
                    leitor.nextLine();

                    if(amount <= 0) {
                        System.out.println("\nInsira um valor positivo maior que zero.\n");
                        continue;
                    }

                    String endereco = "https://v6.exchangerate-api.com/v6/"+ key +"/latest/" + mainCurrency;

                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create(endereco))
                            .build();
                    HttpResponse<String> response = client
                            .send(request, HttpResponse.BodyHandlers.ofString());

                    if (response.statusCode() == 200) {
                        String json = response.body();

                        Gson gson = new Gson();
                        MoedaApi moedaApi = gson.fromJson(json, MoedaApi.class);
                        Moeda moeda = new Moeda(moedaApi);

                        System.out.printf("\nValor original(%s): %.02f\n", moeda.getBaseCode(), amount);
                        System.out.printf("Taxa de conversão (%s): %.04f\n", targetCurrency, moeda.getConversionRates().get(targetCurrency));
                        Double resultado = moeda.convert(amount, moeda.getConversionRates().get(targetCurrency));
                        System.out.printf("\nResultado da conversão: %.02f %s\n", resultado, targetCurrency);
                        leitor.nextLine();
                    } else {
                        System.out.println("Erro na chamada da API: " + response.statusCode());
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\nValor inválido! Tente novamente com um número válido.\n");
                    leitor.nextLine();
                }
            } else if (!busca.equals("0")) {
                System.out.println("Opção inválida! Por favor, selecione uma opção válida.");
            }
        } while (!busca.equals("0"));
    }
}