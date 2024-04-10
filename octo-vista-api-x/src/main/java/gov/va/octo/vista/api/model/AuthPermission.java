package gov.va.octo.vista.api.model;


import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Model for AuthPermission
 * 
 * A AuthPermission is the root permission granting a AuthUser permissions to various operations in
 * the application
 * 
 * 
 * @author william.mccarty@va.gov
 *
 */
@Entity
@Table(name = "authPermissionX")
@Getter
@Setter
public class AuthPermission extends BaseModel implements Serializable {


    private static final long serialVersionUID = 4602107850693537733L;

    @ManyToOne
    @JoinColumn(name = "appId")
    private AuthApp app;

    private String context;
    private String rpc;


}
