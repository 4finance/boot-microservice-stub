package com.ofg.trustly.withdraw

import com.github.tomakehurst.wiremock.client.ValueMatchingStrategy
import com.ofg.infrastructure.Stub
import groovy.transform.TypeChecked

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.ofg.infrastructure.ColaMediaType.*

@TypeChecked
class ExampleStubs {

    @Stub
    public static void helloUser() {
        stubFor(
                get(urlEqualTo('/hello'))
                        .willReturn(aResponse()
                        .withBody(
                        """
                            {
                            "message": "Hello from example stub"
                            }
                        """.stripIndent()
                )))

    }

    @Stub
    public static void createUser() {
        ValueMatchingStrategy
        stubFor(
                post(urlEqualTo('/create-user'))
                        .withHeader(CONTENT_TYPE, equalTo(V1_JSON))
                        .withRequestBody(equalToJson(
                             """
                                { "name": "John Doe",
                                "account-number": "5010 1234 1234 1234 1234"
                                }
                             """))
                        .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, V1_JSON)
                        .withBody("""
                        {
                            "result": {
                                "operation-status": "OK",
                                "user-id": "258a2184-2842-b485-25ca-293525152425"
                            }
                        }""".stripIndent())
                )
        )
    }

    @Stub
    public static void createUserWithMissingData() {
        ValueMatchingStrategy
        stubFor(
                post(urlEqualTo('/create-user'))
                        .withHeader(CONTENT_TYPE, equalTo(V1_JSON))
                        .withRequestBody(equalToJson(
                            """{
                                    "name": "John Doe"
                                }
                            """))
                        .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE, V1_JSON)
                        .withBody("""
                        {
                            "result": {
                                "operation-status": "ERROR",
                                "message": "Missing required value for field account-number"
                            }
                        }""".stripIndent())
                )
        )
    }

}
