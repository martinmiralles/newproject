package com.martin.ppmtool.security;

public class SecurityConstants {

    //Able to make certain changes in just one place: here
    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "h2-console/**";
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final String TOKEN_PREFIX = "Bearer "; //that space after 'Bearer' is required
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 30_000; //in milliseconds; hence, 30,000 milliseconds = 30 seconds
}
