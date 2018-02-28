package com.bloodhub.android;

/**
 * Created by izelgurbuz on 3.02.2018.
 */

public class Constants {

    private static final String secret_Key="&secretCode=abc";

    private static final String ROOT_URL="http://cs491-2.mustafaculban.net/api/v1/";

    public static final String URL_addFireBaseToken=ROOT_URL+"saveFirebaseRegID/"+secret_Key;

    public static final String URL_Register=ROOT_URL+"register/"+secret_Key;

    public static final String URL_Login=ROOT_URL+"login/"+secret_Key;

    public static final String URL_sendBloodRequest=ROOT_URL+"sendBloodRequest/"+secret_Key;

    public static final String URL_getReceivedNotification=ROOT_URL+"getReceivedNotification/"+secret_Key;

    public static final String URL_ListUsers=ROOT_URL+"getUsers/"+secret_Key;


}
