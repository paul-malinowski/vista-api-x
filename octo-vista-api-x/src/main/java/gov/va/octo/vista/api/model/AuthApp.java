package gov.va.octo.vista.api.model;


import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@Table(name = "authAppX")
@Getter
@Setter
public class AuthApp extends BaseModel implements Serializable {


    private static final long serialVersionUID = -1554545906009299012L;

    private static final String WILDCARD = "*";

    private String app;
    @Column(name = "[key]")
    private String key;
    private String comments;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AuthPermission> permissions;

    @OneToMany(mappedBy = "app")
    private List<AuthStation> stations;

    @OneToMany(mappedBy = "app")
    private List<AuthAppConfig> configs;


    public boolean canExecute(@NotNull String context, @NotNull String rpc) {

        Predicate<AuthPermission> matchContext =
                x -> (x.getContext().equals(context.toUpperCase()));
        Predicate<AuthPermission> matchRpc = x -> (x.getRpc().equals(rpc.toUpperCase()));
        Predicate<AuthPermission> wildcardContext = x -> (x.getContext().equals(WILDCARD));
        Predicate<AuthPermission> wildcardRpc = x -> (x.getRpc().equals(WILDCARD));

        Predicate<AuthPermission> unlimited = wildcardContext.and(wildcardRpc);
        Predicate<AuthPermission> allRpcsInContext = matchContext.and(wildcardRpc);
        Predicate<AuthPermission> fullmatch = matchContext.and(matchRpc);


        if (permissions == null || permissions.isEmpty())
            return false;

        return permissions.stream().anyMatch(unlimited.or(allRpcsInContext).or(fullmatch));



    }


    public boolean hasConfig(AppConfigTypeEnum c) {

        if (this.configs == null) {
            return false;
        }

        for (AuthAppConfig cf : this.configs) {
            if (cf.getConfig().equals(c)) {
                return true;
            }
        }

        return false;
    }

}
