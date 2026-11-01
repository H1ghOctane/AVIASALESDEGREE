package api.models.prices;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketPrice {

    private String origin;
    private String destination;
    private int price;
    private String airline;

    @JsonProperty("departure_at")
    private String departureAt;

    private int transfers;
    private String link;
}
