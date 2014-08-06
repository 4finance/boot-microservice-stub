package com.ofg.twitter.places

import com.ofg.infrastructure.Stub
import groovy.transform.TypeChecked

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.ofg.infrastructure.TwitterPlacesAnalyzerMediaType.CONTENT_TYPE
import static com.ofg.infrastructure.TwitterPlacesAnalyzerMediaType.V1_JSON

@TypeChecked
class PairIdControllerStubs {

    @Stub
    public static void getPlacesFromTweets() {
        stubFor(
                put(urlMatching('/[0-9]+'))
                        .withHeader(CONTENT_TYPE, equalTo(V1_JSON))
                        .willReturn(aResponse()
                        .withStatus(200)
                ))

    }

}
