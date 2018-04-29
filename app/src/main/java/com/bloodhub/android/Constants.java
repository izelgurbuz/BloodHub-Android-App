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

    public static final String URL_saveReceiverCondition=ROOT_URL+"saveReceiverCondition/"+secret_Key;

    public static final String URL_getSentNotification=ROOT_URL+"getSentNotification/"+secret_Key;

    public static final String URL_getEM5List=ROOT_URL+"getPersonalEM5List/"+secret_Key;

    public static final String URL_getPersonalWaitingEM5List=ROOT_URL+"getPersonalWaitingEM5List/"+secret_Key;

    public static final String URL_approvePersonalEM5ListRequest = ROOT_URL + "approvePersonalEM5ListRequest/" + secret_Key;

    public static final String URL_addToUsersEM5List = ROOT_URL + "addToUsersEM5List/" + secret_Key;


    public static final String URL_GetEvents = ROOT_URL + "getEvents/" + secret_Key;

    public static final String URL_getBloodLocations = ROOT_URL + "getBloodCenters/" + secret_Key;

    public static final String URL_getCities = ROOT_URL + "getCities/" + secret_Key;



}
