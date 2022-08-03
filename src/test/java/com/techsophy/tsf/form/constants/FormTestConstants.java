package com.techsophy.tsf.form.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormTestConstants
{
    //GlobalMessageSourceConstants
    public static final String  KEY="key";
    public static final String ARGS="args";

    //FormControllerExceptionTest
    public static final String TENANT="tenant";
    public static final String FORM_NOT_FOUND_WITH_GIVEN_ID="Form not found with given id";
    public static final String ENTITY_NOT_FOUND_WITH_GIVEN_ID="Entity not found with given id";
    public static final String USER_DETAILS_NOT_FOUND_WITH_GIVEN_ID="UserDetails Not found with given id";

    //FORMCONTROLLERTESTConstants
    public static final String TEST_ACTIVE_PROFILE ="test";
    public static final String BASE_URL = "/form-modeler";
    public static final String VERSION_V1 = "/v1";
    public static final String FORMS_URL = "/forms";
    public static final String FORM_BY_ID_URL = "/forms/{id}";
    public static final String SEARCH_FORM_URL = "/forms/search";
    public static final String FORM_CONTENT = "testdata/form-content.json";
    public static final String INCLUDE_CONTENT="include-content";
    public static final String TYPE="type";
    public static final String FORM="form";
    public static final String DEPLOYMENT_ID_LIST="deploymentIdList";
    public static final String A="a";
    public static final String ID_OR_NAME_LIKE="idOrNameLike";
    public static final String  PAGE_VALUE="1";
    public static final String SIZE_VALUE="1";
    public static final String  FORM_ID_VALUE="123";
    public static final Integer PAGE_NUMBER=1;
    public static final Integer SIZE_NUMBER=1;

    //FormDefinitionCustomRepo
    public static final String ABC="abc";
    public static final String ONE="1";
    public static final String TWO="2";

    //FormServiceConstants
    public static final String FORMS_DATA_1 = "testdata/form-schema1.json";
    public static final String FORMS_DATA_2 = "testdata/form-schema2.json";
    public final static String ID_VALUE ="1";
    public final static  String NAME ="form1";
    public final static Map<String,Object> COMPONENTS =null ;
    public final static Map<String,Object> PROPERTIES=null;
    public final static String TYPE_FORM ="form";
    public final static String TYPE_COMPONENT ="component";
    public final static Integer VERSION_VALUE =1;
    public final static Boolean IS_DEFAULT_VALUE =true;
    public final static String CREATED_BY_ID_VALUE ="1";
    public final static Instant CREATED_ON_INSTANT =Instant.now();
    public final static String UPDATED_BY_ID_VALUE ="1";
    public final static Instant UPDATED_ON_INSTANT =Instant.now();
    public final static String ID_OR_NAME_LIKE_ABC ="abc";
    public final static String EMPTY_TYPE="";
    public final static String EMPTY_DEPLOYMENT_ID_LIST="";
    public final static String Q="abc";
    public final static String TYPE_VALUE="abc";

    //TokenUtilsTest
    public static final String TOKEN_TXT_PATH = "testdata/token.txt";
    public static final String TECHSOPHY_PLATFORM="techsophy-platform";
    public final static String TEST_SORT="name";
    public final static String DEFAULT_PAGE_LIMIT="defaultPageLimit";

    //INITILIZATION CONSTANTS
    public static final String DEPARTMENT="department";
    public static final String  NULL=null;
    public static final String EMAIL_ID="emailId";
    public static final String MOBILE_NUMBER="mobileNumber";
    public static final String LAST_NAME="lastName";
    public static final String  FIRST_NAME="firstName";
    public static final String USER_NAME="userName";
    public static final String ID ="id";
    public static final String UPDATED_ON="updatedOn";
    public static final String UPDATED_BY_NAME="updatedByName";
    public static final String UPDATED_BY_ID="updatedById";
    public static final String CREATED_ON="createdOn";
    public static final String CREATED_BY_NAME ="createdByName";
    public static final String CREATED_BY_ID="createdById";
    public static final String USER_FIRST_NAME ="tejaswini";
    public static final String USER_LAST_NAME ="Kaza";
    public static final String NUMBER="1234567890";
    public static final String MAIL_ID ="tejaswini.k@techsophy.com";
    public static final String EMPTY_STRING="";
    public static final String BIGINTEGER_ID = "847117072898674688";
    public static final String LOGGED_USER_ID = "847117072898674688";

    //UserDetailsTestConstants
    public static final String  USER_DETAILS_RETRIEVED_SUCCESS="User details retrieved successfully";
    public static final String TEST_TOKEN="token";
    public static final String INITIALIZATION_DATA="{\"data\":[{\"id\":\"847117072898674688\",\"userName\":\"tejaswini\",\"firstName\":\"Kaza\",\"lastName\":\"Tejaswini\",\"mobileNumber\":\"1234567890\",\"emailId\":\"tejaswini.k@techsophy.com\",\"department\":null,\"createdById\":null,\"createdByName\":null,\"createdOn\":null,\"updatedById\":null,\"updatedByName\":null,\"updatedOn\":null}],\"success\":true,\"message\":\"User details retrieved successfully\"}\n";
    public final static String GET="GET";

    //WEBCLIENTWrapperTestConstants
    public static final String LOCAL_HOST_URL="http://localhost:8080";
    public static final String TOKEN="token";
    public static final String TEST="test";
    public static final String PUT="put";
    public static final String DELETE="delete";
}
