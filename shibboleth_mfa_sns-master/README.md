[![Apache License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# Shibboleth-IdP3-SNS-Auth
> Working example of the SNS authenticator. Work in progress! Refactoring needed! Localization needed.  

SNS as authenticator for Shibboleth IdP v3.  
Works conjunction with the User/Password flow. This module first calls authn/Password flow and after that flow is completed it send notification for login approval.

Requirements
------------

Shibboleth IdP v3.3.3  
Java 8

Installing
----------

* Compile souce code with maven - ```mvn clean package```
* Copy and extract snsauth-parent/snsauth-impl/target/snsauth-impl-0.1.0-bin.zip

Directory structure:
<pre>
├── conf
│   └── authn
├── edit-webapp
│   └── WEB-INF
│       └── lib
├── flows
│   └── authn
│       └── SNS
└── views
</pre>

* Copy conf/authn --> $IDP-HOME/conf/authn  
* Copy edit-webapp  --> $IDP-HOME/edit-webapp  
* Copy flows  --> $IDP-HOME/flows  
* Copy views  --> $IDP-HOME/views  
* Copy messages  --> $IDP-HOME/messages  

Modify $IDP_HOME/conf/idp.properties  

idp.authn.flows = Password --> idp.authn.flows = MFA

Add SNS bean to $IDP_HOME/conf/authn/general-authn.xml, to the element:
```
 "<util:list id="shibboleth.AvailableAuthenticationFlows">"
```
  New Bean
```
        <bean id="authn/SNS" parent="shibboleth.AuthenticationFlow"
                p:passiveAuthenticationSupported="true"
                p:forcedAuthenticationSupported="true">
            <property name="supportedPrincipals">
                <util:list>
                    <bean parent="shibboleth.SAML2AuthnContextClassRef"
                        c:classRef="urn:oasis:names:tc:SAML:2.0:ac:classes:SNS" />
                </util:list>
            </property>
        </bean>
```

### Rebuild idp.war
* run $IDP-HOME/bin/build.sh
* If you need, move that war-file to containers "webapps" directory (tomcat, jetty, etc)
* Restart container

