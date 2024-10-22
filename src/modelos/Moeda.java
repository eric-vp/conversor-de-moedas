package modelos;
import java.util.Map;

public class Moeda {
    private String base_code;
    private Map<String, Double> conversion_rates;

    public Moeda(MoedaApi moedaApi) {
        this.base_code = moedaApi.base_code();
        this.conversion_rates = (Map<String, Double>) moedaApi.conversion_rates();
    }

    public String getBaseCode() {
        return base_code;
    }

    public Map<String, Double> getConversionRates() {
        return conversion_rates;
    }

    public Double convert(Double amount, Double targetCurrency) {
        return amount * targetCurrency;
    }

}
