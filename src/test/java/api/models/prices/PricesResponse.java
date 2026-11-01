package api.models.prices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PricesResponse {

    private boolean success;
    private String currency;
    private List<TicketPrice> data;
}
