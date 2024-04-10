# Dev Setup


## VA Network
Vista-API must be run on the VA Network.  It can be run over VPN but CAG environments will not allow connectivity to VistA.

## Wildfly
Tested with Wildfly [24.0.1.Final](https://download.jboss.org/wildfly/24.0.1.Final/wildfly-preview-24.0.1.Final.zip) but the Wildfly infrastructure that VistaLink uses has been pretty stable so new versions of Wildfly may work just fine.


## Wildfly Modules
Wildfly modules are in namespaced hierarchy similar to a maven repository.
Create the given directory structure, place the jar file(s) and or specified property file(s) and the given `module.xml` file.


### JTDS
jar: [jtds-1.3.1](https://repo1.maven.org/maven2/net/sourceforge/jtds/jtds/1.3.1/jtds-1.3.1.jar)<br/>
path: <samp>$WILFLY_HOME/modules/net/sourceforge/jtds/main</samp><br/>
module: <samp>module.xml</samp>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="net.sourceforge.jtds">
    <resources>
        <resource-root path="jtds-1.3.1.jar" />
    </resources>
    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

### XSTREAM
jar: [xstream-1.4.18.jar](https://repo1.maven.org/maven2/com/thoughtworks/xstream/xstream/1.4.18/xstream-1.4.18.jar)<br/>
path: <samp>$WILFLY_HOME/modules/com/thoughtworks/xstream/main</samp><br/>
module: <samp>module.xml</samp>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="com.thoughtworks.xstream">
    <resources>
        <resource-root path="xstream-1.4.18.jar" />
    </resources>
    <dependencies>
        <module name="javaee.api" />
    </dependencies>  	
</module>
```

### VistA Link
jar: [vlj-connector-1.6.0.030.jar](https://maven.lom.med.va.gov/repository/gov/va/med/vistalink/vlj-connector/1.6.0.030/vlj-connector-1.6.0.030.jar)<br/>
jar: [vlj-foundations-1.6.0.030.jar](https://maven.lom.med.va.gov/repository/gov/va/med/vistalink/vlj-foundations/1.6.0.030/vlj-foundations-1.6.0.030.jar)<br/>
path: <samp>$WILFLY_HOME/modules/gov/va/med/vistalink/main</samp><br/>
file:  <samp>gov.va.med.vistalink.connectorConfig.xml</samp>

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<connectors xmlns="http://med.va.gov/vistalink/adapter/config" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" encryptionScoped="false" xsi:noNamespaceSchemaLocation="connectorConfig.xsd">

    <connector primaryStation="500"  
			   jndiName="java:jboss/vlj/vetextdev" 
			   access-code="[ACCESS CODE]" 
			   verify-code="[VERIFY CODE]"
			   encrypted="true" 
			   ip="[IP ADDRESS]" 
			   port="[PORT]" 
			   always-use-default-as-min="false" 
			   enabled="true">
	</connector>
	
	<!-- CHY0016 -->
	<connector primaryStation="442"  
			   jndiName="java:jboss/vlj/vlj442" 
			   access-code="[ACCESS CODE]" 
			   verify-code="[VERIFY CODE]"
			   encrypted="true" 
			   ip="[IP ADDRESS]" 
			   port="[PORT]" 
			   always-use-default-as-min="false" 
			   enabled="true">
	</connector>
	
	<!-- CHY0017D -->
	<connector primaryStation="901"  
			   jndiName="java:jboss/vlj/vlj901" 
			   access-code="[ACCESS CODE]" 
			   verify-code="[VERIFY CODE]"
			   encrypted="true" 
			   ip="[IP ADDRESS]" 
			   port="[PORT]" 
			   always-use-default-as-min="false" 
			   enabled="true">
	</connector>
	
	<!-- CHY0025 -->
	<connector primaryStation="902"  
			   jndiName="java:jboss/vlj/vlj902" 
			   access-code="[ACCESS CODE]" 
			   verify-code="[VERIFY CODE]"
			   encrypted="true" 
			   ip="[IP ADDRESS]" 
			   port="[PORT]"  
			   always-use-default-as-min="false" 
			   enabled="true">
	</connector>

</connectors>
```

file: <samp>META-INF/ra.xml</samp>

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE connector PUBLIC '-//Sun Microsystems, Inc.//DTD Connector 1.0//EN' 'http://java.sun.com/dtd/connector_1_0.dtd'>

<connector>
  <display-name>VistaLinkAdapter</display-name>
  <vendor-name>Foundations</vendor-name>
  <spec-version>1.0</spec-version>
  <eis-type>VistA</eis-type>
  <version>1.6.0</version>
  <resourceadapter>
    <managedconnectionfactory-class>gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory</managedconnectionfactory-class>
    <connectionfactory-interface>javax.resource.cci.ConnectionFactory</connectionfactory-interface>
    <connectionfactory-impl-class>gov.va.med.vistalink.adapter.cci.VistaLinkConnectionFactory</connectionfactory-impl-class>
    <connection-interface>javax.resource.cci.Connection</connection-interface>
    <connection-impl-class>gov.va.med.vistalink.adapter.cci.VistaLinkConnection</connection-impl-class>
    <transaction-support>NoTransaction</transaction-support>
    <config-property>
      <config-property-name>connectorJndiName</config-property-name>
      <config-property-type>java.lang.String</config-property-type>
      <config-property-value>java:jboss/vlj/vetextdev</config-property-value>
    </config-property>
    <reauthentication-support>false</reauthentication-support>
  </resourceadapter>
</connector>
```

module: <samp>module.xml</samp>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="gov.va.med.vistalink">
    <resources>
        <resource-root path="." />
        <resource-root path="vlj-connector-1.6.0.030.jar" />
        <resource-root path="vlj-foundations-1.6.0.030.jar" />
    </resources>
    <dependencies>
        <module name="javax.api" />
        <module name="javax.resource.api" />
        <module name="org.slf4j" />
    </dependencies>
</module>
```

### Kaajee
jar: [kaajee-wildfly-2.3.jar](https://maven.lom.med.va.gov/repository/gov/va/med/kaajee/kaajee-wildfly/2.3/kaajee-wildfly-2.3.jar)<br/>
path: <samp>$WILFLY_HOME/modules/gov/va/med/kaajee/main</samp><br/>
module: <samp>module.xml</samp>

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="gov.va.med.kaajee">
    <resources>
        <resource-root path="kaajee-wildfly-2.3.jar" />
    </resources>
    <dependencies>
        <module name="gov.va.med.vistalink" />
        <module name="org.picketbox" />
        <module name="org.slf4j" />
        <module name="javax.servlet.api" />
        <module name="javax.resource.api" />
        <module name="javaee.api" />
        <module name="com.thoughtworks.xstream" />
    </dependencies>
</module>
```

### VistA-API
path: <samp>$WILFLY_HOME/modules/gov/va/octo/vista/api/main</samp><br/>
file:  <samp>jwt.properties</samp>

```bash
jwt.application.name=OCTO-VISTA-API
jwt.application.key=[APP KEY]
jwt.issuer=gov.va.ovac.vetext.smsbatch
jwt.ttl=5
jwt.refreshttl=60
jwt.clockSkew=5
jwt.audience=VETEXT-SMS-BATCH
jwt.public.key=[PUBLIC KEY]
jwt.private.key=[PRIVATE KEY]

#DEV key (static/fake) jwt
#jwt.ssoi.public.key=MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiexEuTQvuPv71WgxkM6p3en9jdrpdCMPBvztTA29Rz6m9e47xZ/oXCyPmNOouCGx8VoZbNDSRQdgkRwbobvlFolie5O9znu0+Vd4yxASlJoSGwU+zNIwOpBHG7P3x0bzc8bnUszmUEbnWKlMy6HRIafv3XmdjTr8fAbGaTryh0QIDAQAB
jwt.ssoi.public.key=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArOBIsZXoGmTwIeOKzfqWr3Sh5CBacE/SKLOxjotvx1ykIPcEympGGUf6YIZcO3qJCy4+VHXGeVtgB64auC2LQys0CNLIWawqwyPyklUzn9/DVGJvDeCNdp9C1t40v7UsNHvNBszBuCAxrHkCF5A2+Hx3fID9n+yBx3m1qM6o0RdZxec8Xf+cSenpq85k//3tuR9B/EB2anUdnsrgLnW3EKcd5SyY1U/eKljrQETcmDTSfvyieJn0u1jPh08hsnCBpNrS6D+I7PlCIrHtHuCFqa+knUZE7dbvSzNG0YSCrasfpkwVZ17yBxfuaG/XCwRwx9IA9+fcADceWfi+wXpCzQIDAQAB

#SQA key
#jwt.ssoi.public.key=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxsKBDBTi1TybLYgpym6hUaxee4hpjehsKf4VQO96XktXfUL3+JjafSOkg3siYcQydqNimo+83UiyoNOkcZir5QjDUaIWSBBFf7r/8nHmcsZysRtzeutBeNkdF5cO/87GUdSiFaGbweDSTqpQ0/yUCsPOfFOsulIUWeKb9rArntQP1UGmm2EotqwMS3aDN2JhZ1BuvYfB8UYeTn1zf3zoCBvkSTHSMWScnRZ1HFogbJhldsiijdzcTc91VWaV5kgX6lO32jYchYZPJZYBYH82tWFzG2F01kyHSHZ6BJ4ki1KwkXq37wG3wYCf+i2NIjqgLjuqP+1sHjXbN77GNnh4HQIDAQAB

#PROD key
#jwt.ssoi.public.key=MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmTd9PTUd7BXnLRU5/WUxZvxRTnHrqxcDC11t7utSmm+oLAdcDpifGfxkO1RH4akGX63oOXWQ/fOrTVezjX/EFOpU5aic4LxNXrvgAZhTxXSNrq2qncw6OgTZBeap7yKZJv8BSM3IqM9ULENjwBqfi8khChvnbCD+IZKSGbT7ZZwLnb2J0QnKLHCW6cadQlYCYBDhuYRkC1CsSf3tY+8zZjwJi1dXV+R8bMp1hg/aKgqmH5LhA5VeuDeuNa01y7AJr1XzQEuOpfzrl8zmAE0lP/IIxv1cbQL1/KRfGfLd+3xIyeAO94XmP7ZE/kw2fjqlasooMmzgenLHrJRVC5Q0vwIDAQAB

```

file:  <samp>vista-api.properties</samp>

```xml
validate.vista.id=false
```

file:  <samp>vistabroker.properties</samp>   !? empty file



module: <samp>module.xml</samp>  

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.3" name="gov.va.octo.vista.api">
    <resources>
        <resource-root path="." />
    </resources>
    <dependencies>
        <module name="javaee.api" />
		<module name="org.codehaus.jackson.jackson-jaxrs"/>
        <module name="javax.ws.rs.api" />
		<module name="org.apache.commons.lang3" />
    </dependencies>    
	
</module>
```


## Standalone Configuration
### Add datasource
add the following datasource under `<datasources>`

```xml
<datasource jta="true" jndi-name="java:jboss/datasource/vista-api" pool-name="VistaApiDs" enabled="true" use-java-context="true">
    <connection-url>[DATABASE URL]</connection-url>
    <driver>jtds</driver>
    <pool>
        <min-pool-size>5</min-pool-size>
        <max-pool-size>100</max-pool-size>
        <prefill>false</prefill>
        <use-strict-min>false</use-strict-min>
        <flush-strategy>FailingConnectionOnly</flush-strategy>
    </pool>
    <security>
        <user-name>[USERNAME]</user-name>
        <password>[PASSWORD]</password>
    </security>
    <validation>
        <valid-connection-checker class-name="org.jboss.jca.adapters.jdbc.extensions.mssql.MSSQLValidConnectionChecker"/>
        <check-valid-connection-sql>select 1</check-valid-connection-sql>
        <validate-on-match>false</validate-on-match>
        <background-validation>true</background-validation>
        <background-validation-millis>10000</background-validation-millis>
    </validation>
    <timeout>
        <blocking-timeout-millis>30000</blocking-timeout-millis>
        <idle-timeout-minutes>10</idle-timeout-minutes>
    </timeout>
</datasource>
```

### Add database driver
add the following database driver under `<datasources><drivers>`

```xml
<driver name="jtds" module="net.sourceforge.jtds">
    <xa-datasource-class>net.sourceforge.jtds.jdbcx.JtdsDataSource</xa-datasource-class>
</driver>
```

### Add resource adapters
add the following resource adapter under `<resource-adapters>`

```xml
<resource-adapter id="vistalink">
    <module slot="main" id="gov.va.med.vistalink"/>
    <transaction-support>NoTransaction</transaction-support>
    <connection-definitions>
        <connection-definition class-name="gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory" jndi-name="java:jboss/vlj/vetextdev" enabled="true" use-java-context="true" pool-name="vlVetextDevPool">
            <config-property name="connectorJndiName">java:jboss/vlj/vetextdev</config-property>
            <pool>
                <min-pool-size>0</min-pool-size>
                <max-pool-size>5</max-pool-size>
                <prefill>false</prefill>
                <use-strict-min>false</use-strict-min>
                <flush-strategy>FailingConnectionOnly</flush-strategy>
            </pool>
            <timeout>
                <blocking-timeout-millis>30000</blocking-timeout-millis>
                <idle-timeout-minutes>5</idle-timeout-minutes>
            </timeout>
        </connection-definition>
        <connection-definition class-name="gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory" jndi-name="java:jboss/vlj/vlj902" enabled="true" use-java-context="true" pool-name="vlj902Pool">
            <config-property name="connectorJndiName">java:jboss/vlj/vlj902</config-property>
            <pool>
                <min-pool-size>0</min-pool-size>
                <max-pool-size>5</max-pool-size>
                <prefill>false</prefill>
                <use-strict-min>false</use-strict-min>
                <flush-strategy>FailingConnectionOnly</flush-strategy>
            </pool>
            <timeout>
                <blocking-timeout-millis>30000</blocking-timeout-millis>
                <idle-timeout-minutes>5</idle-timeout-minutes>
            </timeout>
        </connection-definition>
        <connection-definition class-name="gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory" jndi-name="java:jboss/vlj/vlj442" enabled="true" use-java-context="true" pool-name="vlj442Pool">
            <config-property name="connectorJndiName">java:jboss/vlj/vlj442</config-property>
            <pool>
                <min-pool-size>0</min-pool-size>
                <max-pool-size>5</max-pool-size>
                <prefill>false</prefill>
                <use-strict-min>false</use-strict-min>
                <flush-strategy>FailingConnectionOnly</flush-strategy>
            </pool>
            <timeout>
                <blocking-timeout-millis>30000</blocking-timeout-millis>
                <idle-timeout-minutes>5</idle-timeout-minutes>
            </timeout>
        </connection-definition>
        <connection-definition class-name="gov.va.med.vistalink.adapter.spi.VistaLinkManagedConnectionFactory" jndi-name="java:jboss/vlj/vlj901" enabled="true" use-java-context="true" pool-name="vlj901Pool">
            <config-property name="connectorJndiName">java:jboss/vlj/vlj901</config-property>
            <pool>
                <min-pool-size>0</min-pool-size>
                <max-pool-size>5</max-pool-size>
                <prefill>false</prefill>
                <use-strict-min>false</use-strict-min>
                <flush-strategy>FailingConnectionOnly</flush-strategy>
            </pool>
            <timeout>
                <blocking-timeout-millis>30000</blocking-timeout-millis>
                <idle-timeout-minutes>5</idle-timeout-minutes>
            </timeout>
        </connection-definition>
    </connection-definitions>
</resource-adapter>
```

### Add security domain
add the following security domain under `<security-domains>`

```xml
<security-domain name="kaajee" cache-type="default">
    <authentication>
        <login-module code="gov.va.med.kaajee.security.auth.KaajeeLoginModule" flag="required">
            <module-option name="principalClass" value="gov.va.med.kaajee.model.KaajeePrincipal"/>
        </login-module>
    </authentication>
</security-domain>
```

