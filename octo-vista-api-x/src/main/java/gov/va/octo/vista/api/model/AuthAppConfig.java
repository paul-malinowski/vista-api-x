package gov.va.octo.vista.api.model;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import gov.va.octo.vista.api.enumeration.AppConfigTypeEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Model: AuthUser
 * 
 * @author william.mccarty@va.gov
 *
 */
@Entity
@Table(name = "authAppConfigX")
@Getter
@Setter
public class AuthAppConfig extends BaseModel implements Serializable {

    private static final long serialVersionUID = 3441553660897780108L;

    @ManyToOne
    @JoinColumn(name = "appId")
    private AuthApp app;

    @Column(name = "configId")
    private AppConfigTypeEnum config;

}
