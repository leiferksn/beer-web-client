package tech.bouncystream.beerclient.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Beer {

    @Null
    private String id;
    @NotBlank
    private String beerName;
    @NotBlank
    private BeerStyle beerStyle;
    private String upc;
    private Integer quantityOnHand;
    private String price;
    private String version;
    private Date createdDate;
    private Date lastModifiedDate;

}
