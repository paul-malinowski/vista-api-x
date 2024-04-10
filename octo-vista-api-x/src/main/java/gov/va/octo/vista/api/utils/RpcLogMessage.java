package gov.va.octo.vista.api.utils;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RpcLogMessage {

    String action;
    String context;
    String rpc;
    String station;
    List<String> params;
    @JsonIgnore
    boolean jsonResult;

    @Override
    public String toString() {

        String msg;
        ObjectMapper mapper = new ObjectMapper();
        try {
            msg = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            msg = this.getStation() + "^" + this.getContext() + "^" + this.getRpc();
        }

        return msg;
    }
}
