package br.com.maxsueleinstein.stratega.infrastructure.service;

import br.com.maxsueleinstein.stratega.domain.model.Currency;
import br.com.maxsueleinstein.stratega.domain.service.ExchangeRateService;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.entity.ExchangeRateEntity;
import br.com.maxsueleinstein.stratega.infrastructure.persistence.repository.SpringDataExchangeRateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final SpringDataExchangeRateRepository repository;
    private final RestTemplate restTemplate;

    @Value("${exchangerate.api.key:YOUR_KEY}")
    private String apiKey;

    public ExchangeRateServiceImpl(SpringDataExchangeRateRepository repository) {
        this.repository = repository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public BigDecimal getRate(Currency from, Currency to) {
        if (from == to) return BigDecimal.ONE;

        String pair = from.name() + "_" + to.name();
        Optional<ExchangeRateEntity> cached = repository.findById(pair);

        if (cached.isPresent() && isCacheValid(cached.get())) {
            return cached.get().getRate();
        }

        return fetchAndStoreRate(from, to);
    }

    private boolean isCacheValid(ExchangeRateEntity entity) {
        // Cache is valid if updated in the last 24 hours
        return entity.getLastUpdated().isAfter(LocalDateTime.now().minusHours(24));
    }

    private BigDecimal fetchAndStoreRate(Currency from, Currency to) {
        try {
            // Free tier usually requires fetching all rates for a base currency
            String url = String.format("https://v6.exchangerate-api.com/v6/%s/latest/%s", apiKey, from.name());
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && "success".equals(response.get("result"))) {
                Map<String, Object> rates = (Map<String, Object>) response.get("conversion_rates");
                Object rateObj = rates.get(to.name());
                
                if (rateObj != null) {
                    BigDecimal rate = new BigDecimal(rateObj.toString());
                    
                    repository.save(new ExchangeRateEntity(
                            from.name() + "_" + to.name(),
                            rate,
                            LocalDateTime.now()
                    ));
                    
                    // Also save the inverse
                    repository.save(new ExchangeRateEntity(
                            to.name() + "_" + from.name(),
                            BigDecimal.ONE.divide(rate, 10, java.math.RoundingMode.HALF_UP),
                            LocalDateTime.now()
                    ));
                    
                    return rate;
                }
            }
        } catch (Exception e) {
            // Log error and return fallback or throw
            System.err.println("Erro ao buscar taxa de câmbio: " + e.getMessage());
        }

        // Fallback for demo purposes if API fails or no key
        return getFallbackRate(from, to);
    }

    private BigDecimal getFallbackRate(Currency from, Currency to) {
        if (from == Currency.USD && to == Currency.BRL) return new BigDecimal("5.00");
        if (from == Currency.BRL && to == Currency.USD) return new BigDecimal("0.20");
        if (from == Currency.EUR && to == Currency.BRL) return new BigDecimal("5.40");
        if (from == Currency.BRL && to == Currency.EUR) return new BigDecimal("0.18");
        return BigDecimal.ONE;
    }
}
