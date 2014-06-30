package com.ofg.infrastructure

import groovy.transform.TypeChecked

@TypeChecked
class ColaMediaType {

    public static final String CONTENT_TYPE = "Content-Type"

    protected static final String APPLICATION_TYPE = 'application/vnd.vivus.cola'

    private static final String JSON = '+json'
    private static final String XML = '+xml'

    public static final String V1_JSON = APPLICATION_TYPE + ".v1" + JSON
    public static final String V2_JSON = APPLICATION_TYPE + ".v2" + JSON

}
