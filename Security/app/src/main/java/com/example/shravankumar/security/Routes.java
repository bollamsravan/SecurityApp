package com.example.shravankumar.security;

/**
 * Created by Shravan Kumar on 21-04-2017.
 */

public class Routes {
    public static final String HOSTNAME= "http://172.16.81.166:3000";
    public static final String Authenticate = HOSTNAME+"/authenticate";
    public static final String Register = HOSTNAME +"/resident/register";
    public static final String SecurityRegister = HOSTNAME+"/security/register";
    public static final String Update=HOSTNAME+"/resident/update";
    public static final String Availability=HOSTNAME+"/resident/set_availability";
    public static final String Validate = HOSTNAME+"/security/add_visitor";
    public static final String CheckAvailable = HOSTNAME+"/resident/availability";
    public static final String ResidentAvailable = HOSTNAME+"/resident/resident_availability";
    public static final String AcceptReject  = HOSTNAME+"/resident/accept_or_reject_visitor";
    public static final String GetNotifications =HOSTNAME+"/resident/get_visitor_notifications";
}

