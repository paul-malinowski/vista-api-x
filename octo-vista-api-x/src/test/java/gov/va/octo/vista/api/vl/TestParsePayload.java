package gov.va.octo.vista.api.vl;

import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.octo.vista.api.client.RpcRequestX;

public class TestParsePayload {

    private String payload = """
            {
              "context": "OR CPRS GUI CHART",
              "rpc": "TIU CREATE RECORD",
              "jsonResult": false,
              "parameters" : [
                {"string": "418"},
                {"string": "1660"},
                {"string": ""},
                {"string": ""},
                {"string": ""},
                {"namedArray": {
                  "1701": "",
                  "1301": "N",
                  "1205": "64",
                  "\\"TEXT\\",1,0)": "this is a sample note ..."
                }}
              ]
            }
            """;


    @Test
    public void testDeserialize() {

        ObjectMapper mapper = new ObjectMapper();

        try {

            RpcRequestX xreq = mapper.readValue(payload, RpcRequestX.class);

            System.out.println(xreq.toString());

        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
}
