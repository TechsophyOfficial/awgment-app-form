package com.techsophy.tsf.form.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormModelerConstants
{
    //DatabaseChangeLog
    public static final String ORDER_1="1";
    public static final String SYSTEM_VERSION_1="1";
    public static final String TP_APP_FORM="tp-app-form";

    //LoggingHandler
    public static final String CONTROLLER_CLASS_PATH = "execution(* com.techsophy.tsf.form.controller..*(..))";
    public static final String SERVICE_CLASS_PATH= "execution(* com.techsophy.tsf.form.service..*(..))";
    public static final String EXCEPTION = "ex";
    public static final String IS_INVOKED_IN_CONTROLLER= "{}() is invoked in controller ";
    public static final String IS_INVOKED_IN_SERVICE= "{}() is invoked in service ";
    public static final String EXECUTION_IS_COMPLETED_IN_CONTROLLER="{}() execution is completed  in controller";
    public static final String EXECUTION_IS_COMPLETED_IN_SERVICE="() execution is completed  in service";
    public static final String EXCEPTION_THROWN="An exception has been thrown in ";
    public static final String CAUSE="Cause : ";
    public static final String BRACKETS_IN_CONTROLLER="() in controller";
    public static final String BRACKETS_IN_SERVICE="() in service";
    public static final String AWGMENT_ROLES_MISSING_IN_CLIENT_ROLES ="AwgmentRoles are missing in clientRoles";
    public static final String CLIENT_ROLES_MISSING_IN_USER_INFORMATION="ClientRoles are missing in the userInformation";

    //JWTRoleConverter
    public static final String CLIENT_ROLES="clientRoles";
    public static final String USER_INFO_URL= "/protocol/openid-connect/userinfo";

    /*LocaleConfig Constants*/
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String BASENAME_ERROR_MESSAGES = "classpath:errorMessages";
    public static final String BASENAME_MESSAGES = "classpath:messages";
    public static final Long CACHEMILLIS = 3600L;
    public static final Boolean USEDEFAULTCODEMESSAGE = true;

    /*TenantAuthenticationManagerConstants*/
    public static final String KEYCLOAK_ISSUER_URI = "${keycloak.issuer-uri}";

    // Roles
    public static final String HAS_ANY_AUTHORITY="hasAnyAuthority('";
    public static final String HAS_ANY_AUTHORITY_ENDING="')";
    public static final String AWGMENT_FORM_CREATE_OR_UPDATE = "awgment-form-create-or-update";
    public static final String AWGMENT_FORM_READ = "awgment-form-read";
    public static final String AWGMENT_FORM_DELETE = "awgment-form-delete";
    public static final String AWGMENT_FORM_ALL ="awgment-form-all";
    public static final String OR=" or ";
    public static final String CREATE_OR_ALL_ACCESS =HAS_ANY_AUTHORITY+AWGMENT_FORM_CREATE_OR_UPDATE +HAS_ANY_AUTHORITY_ENDING+OR+HAS_ANY_AUTHORITY+AWGMENT_FORM_ALL +HAS_ANY_AUTHORITY_ENDING;
   public static final String READ_OR_ALL_ACCESS =HAS_ANY_AUTHORITY+ AWGMENT_FORM_READ +HAS_ANY_AUTHORITY_ENDING+OR+HAS_ANY_AUTHORITY+AWGMENT_FORM_ALL+HAS_ANY_AUTHORITY_ENDING;
   public static final String DELETE_OR_ALL_ACCESS=HAS_ANY_AUTHORITY+AWGMENT_FORM_DELETE+HAS_ANY_AUTHORITY_ENDING+OR+HAS_ANY_AUTHORITY+AWGMENT_FORM_ALL+HAS_ANY_AUTHORITY_ENDING;

    /*FormControllerConstants*/
    public static final String BASE_URL = "/form-modeler";
    public static final String VERSION_V1 = "/v1";
    public static final String FORMS_URL = "/forms";
    public static final String FORM_BY_ID_URL = "/forms/{id}";
    public static final String SEARCH_FORM_URL = "/forms/search";
    public static final String HISTORY ="/history";
    public static final String FORM_VERSION_BY_ID_URL = "/forms/{id}/{version}";
    public static final String ID = "id";
    public static final String INCLUDE_CONTENT = "include-content";
    public static final String ID_OR_NAME_LIKE = "idOrNameLike";
    public static final String TYPE = "type";
    public static final String PAGE="page";
    public static final String SIZE="size";
    public static final String SORT_BY="sort-by";
    public static final String QUERY ="q";
    public static final String DEPLOYMENT = "deployment";
    public static final String GET_FORM_SUCCESS="GET.FORM.SUCCESS";
    public static final String GET_COMPONENT_SUCCESS="GET.COMPONENT.SUCCESS";
    public static final String SAVE_FORM_SUCCESS="SAVE.FORM.SUCCESS";
    public static final String SAVE_COMPONENT_SUCCESS="SAVE.COMPONENT.SUCCESS";
    public static final String DELETE_FORM_SUCCESS="DELETE.FORM.SUCCESS";
    public static final String DELETE_COMPONENT_SUCCESS="DELETE.COMPONENT.SUCCESS";
    public static final String COMPONENT="component";

    /*FormSchemaConstants*/
    public static final String NAME_NOT_BLANK = "Name should not be blank";
    public static final String FORM_ID_NOT_BLANK = "Form id should not be blank";
    public static final String REGEX_CONSTANT = "^[^\\s].*[^\\s]$";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TIME_ZONE = "UTC";

    /*FormDefinitionConstants*/
    public static final String TP_FORM_DEFINITION_COLLECTION = "tp_form_definition";
    public static final String TP_FORM_DEFINITION_AUDIT_COLLECTION ="tp_form_definition_audit";

    /*FormDefinitionAuditRepositoryConstants*/
    public static final String FIND_BY_ID_QUERY ="{'formId' : ?0 , 'version' : ?1}";
    public static final String FIND_ALL_BY_ID_QUERY="{'formId' : ?0 }";

    /*FormDefinitionCustomRepositoryConstants*/
    public static final String FORM_ID ="_id";
    public static final String FORM_NAME ="name";
    public static final String FIRST_NAME="firstName";
    public static final String LAST_NAME="lastName";

    /*FormServiceImplConstants*/
    public static final String FETCH_FORMS="Fetching forms";
    public static final String REGEX_SPLIT_BY_COMMA=",";
    public static final String NULL="null";

    /*formDefinitionCustomRepositoryImplConstants*/
    public static final String VERSION="version";
    public static final String COMPONENTS="components";

    /*TokenUtilsAndWebclientWrapperConstants*/
    public static final String PREFERED_USERNAME="preferred_username";
    public static final String EMPTY_STRING="";
    public static final String BEARER="Bearer ";
    public static final String REGEX_SPLIT="\\.";
    public static final String ISS="iss";
    public static final String URL_SEPERATOR="/";
    public static final int SEVEN=7;
    public static final int ONE=1;
    public static final String DESCENDING="desc";
    public static final String CREATED_ON="createdOn";
    public static final String CREATED_ON_ASC="createdOn: ASC";
    public static final String  COLON=":";
    public static final String DEFAULT_PAGE_LIMIT= "${default.pagelimit}";

    /*UserDetailsConstants*/
    public static final String GATEWAY_URI="${gateway.uri}";
    public static final String LOGGED_USER="loggeduser";
    public static final String TOKEN="token";
    public static final String AUTHORIZATION="Authorization";
    public static final String CONTENT_TYPE="Content-Type";
    public static final String APPLICATION_JSON ="application/json";
    public static final String FILTER_COLUMN="?filter-column=loginId&filter-value=";
    public static final String MANDATORY_FIELDS="&only-mandatory-fields=true";
    public static final String RESPONSE="response";
    public static final String DATA ="data";
    public static final String GATEWAY="gateway";
    public static final String ACCOUNT_URL = "/accounts/v1/users";

    //WebClientWrapper
    public static final String GET="GET";
    public static final String POST="POST";
    public static final String PUT="PUT";
    public static final String DELETE="DELETE";
    public static final String SERVICE = "service";

    /*MainMethodConstants*/
    public static final String PACKAGE_NAME ="com.techsophy.tsf.form.*";
    public static final String MULTI_TENANCY_PACKAGE_NAME ="com.techsophy.multitenancy.mongo.*";
    public static final String FORM_MODELER ="tp-app-form";
    public static final String VERSION_1="1.0";
    public static final String FORM_MODELER_API_VERSION_1="Form Modeler API v1.0";
    public static final String GATEWAY_URL="${gateway.uri}";
}
