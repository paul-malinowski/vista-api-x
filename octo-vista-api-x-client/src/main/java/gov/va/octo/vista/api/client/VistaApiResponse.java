package gov.va.octo.vista.api.client;

import java.io.Serializable;
import lombok.Data;

@Data
public class VistaApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 4409921838640923340L;

    private String path = "";
    private T data;

}
