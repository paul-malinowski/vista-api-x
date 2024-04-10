package gov.va.octo.vista.api.enumeration;

public enum LoginTypeEnum {

    // @formatter:off
    DUZ(1L, "DUZ"),
    VPID(2L, "VPID"),
    APP_PROXY(3L, "APP_PROXY");
    // @formatter:on

    private Long id;
    private String name;

    private LoginTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

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


    public static LoginTypeEnum valueOf(Long value) {
        for (LoginTypeEnum f : LoginTypeEnum.values()) {
            if (f.getId().equals(value))
                return f;
        }
        return null;
    }



}
