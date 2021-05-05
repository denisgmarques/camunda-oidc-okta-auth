# Camunda OIDC integration with Okta Identity Provider - Hosted

###
This project was based on [camunda-saml-okta-auth](https://github.com/darlanmoraes/camunda-saml-okta-auth.git)

###
Pre-requisites:

- Okta admin account
- Git
- Java 11
- Maven

### Setting Up Okta and application.properties

1. Developer Account Sign Up
First, we'll sign up for a free Okta developer account that provides access for up to 1k monthly active users. However, we can skip this step 
if we already have one:

[Create your Okta account here](https://developer.okta.com/signup/)

2. Logged on Okta, we will add a new application
   - Click on Application/Application
   - Click on the button [Add Application]
   - Click on the button [Create New App]
    
    Fill the form like this:

    - Plataform: Web
    - Sign on method: OpenID Connect
    
    - Click on the button [Create]
    
    Fill the application form like this:
    - Application Name: Camunda OIDC
    - Login redirect URIs: http://localhost:8080/authorization-code/callback


3. Configuring Application

   3.1 Getting application secret and client id
    - Click over the new application link "Camunda OIDC"
    - Copy Client ID and Client secret and put it on the application.properties configuration file (included on this project)
   ```      
    okta.oauth2.client-id=<FILL HERE>
    okta.oauth2.client-secret=<FILL HERE>
   ```

   3.2 Assignments
   - Click on "Assignments" tab
   - Click on the [Assign] button
   - Click on your user [Assign] buttom
   - Save and go back
   - Done
   

4. Configuring Okta Security / API

   4.1 Configuring Issuer URI
   
    - In Okta, click on Security / API
    - Copy default Issuer URI and put it on the application.properties configuration file
   ```
    okta.oauth2.issuer=<FILL HERE something like this https://dev-0000001010.okta.com/oauth2/default>
   ```

   4.2. Configuring an admin token

   #### Admin token will be used by Camunda to get Okta directory data

    - Click on the "Tokens" tab
    - Click on the button [Create Token]
    - Copy generated token and put it on the application.properties configuration file
   ```
    okta.client.token=<FILL HERE>
   ```
   
   4.3. Configuring Trusted Origins

   - Click on the "Trusted Origins" tab
   - Click on the buttom [Add Origin]
   - Fill the name: Camunda
   - Origin Url: http://localhost:8080
   - Select CORS and Redirect checkboxes
   - Click on Save


5. Configuring your user as a Camunda Administrator User

   #### This is necessary to have access on Camunda Applications Cockpit, Admin, Task

   - In Okta, accessing Directory, People
   - Click on your user
   - Copy the user id, last part of browser url.
     e.g.: https://dev-000000010-admin.okta.com/admin/user/profile/view/6534635643563546345
     Copy the last part: 6534635643563546345
   - Put your user id into the application.properties:
   ```
      camunda.bpm.admin.username=6534635643563546345
   ```

### Running the project

1. Maven

   #### Remember, you need Java 11 and maven to run this project on your computer

   - Go to the root of this project and type:

   ```
   mvn spring-boot:run
   ```

2. Accessing Camunda


   - Open this URL on your browser: http://localhost:8080/camunda/app/welcome/default/#!/welcome
   
