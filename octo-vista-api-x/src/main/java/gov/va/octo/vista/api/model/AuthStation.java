package gov.va.octo.vista.api.model;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Model: AuthStation
 * 
 * @author william.mccarty@va.gov
 *
 */
@Entity
@Table(name = "authStationX")
@Getter
@Setter
public class AuthStation extends BaseModel implements Serializable {


    private static final long serialVersionUID = 5142362421830761071L;


    @ManyToOne
    @JoinColumn(name = "appId")
    private AuthApp app;

    private String station;
    private String duz;

}
