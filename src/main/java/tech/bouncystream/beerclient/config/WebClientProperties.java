package tech.bouncystream.beerclient.config;

public class WebClientProperties {

    public static final String BASE_URL = "http://api.springframework.guru";

    public static final String BEER_V1_PATH = "/api/v1/beer";

    public static final String BEER_BY_ID_V1_PATH = BEER_V1_PATH + "/{beerId}";

    public static final String BEER_UPC_V1_PATH = "/api/v1/beerUpc/{upc}";
}
