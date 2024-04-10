package gov.va.octo.vista.api.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AppConfigTypeEnum {

    // @formatter:off
    ALLOW_DDR(1000000L, "ALLOW_DDR"),
    ALLOW_VISTA_API_X_TOKEN(1000001L, "ALLOW_VISTA_API_X_TOKEN");
    // @formatter:on

    private Long id;
    private String name;

    private AppConfigTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @JsonCreator
    public static AppConfigTypeEnum valueOf(Long value) {
        if (value == null) {
            return null;
        }
        for (AppConfigTypeEnum f : AppConfigTypeEnum.values()) {
            if (f.getId().equals(value))
                return f;
        }
        return null;
    }

}
