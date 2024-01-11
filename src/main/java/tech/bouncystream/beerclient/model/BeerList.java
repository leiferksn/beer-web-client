package tech.bouncystream.beerclient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;

public class BeerList extends PageImpl<Beer> implements Serializable {

    private static final long serialVersionUID = 1234567L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public BeerList(@JsonProperty("content") List<Beer> content,
                    @JsonProperty("number") int number,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements")  Long totalElements,
                    @JsonProperty("pageable") JsonNode pageable,
                    @JsonProperty("last") boolean last,
                    @JsonProperty("totalPages") int totalPages,
                    @JsonProperty("sort") JsonNode sort,
                    @JsonProperty("first") boolean first,
                    @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public BeerList(List<Beer> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public BeerList(List<Beer> content) {
        super(content);
    }
}
