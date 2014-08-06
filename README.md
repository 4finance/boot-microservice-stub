Boot microservice stub
======================

Example of a stub of a microservice.

## Introduction

When trying to run a microservice locally you would like to have all of its collaborators (microservice dependencies) stubbed.
This project shows a stub that uses [WireMock](http://wiremock.org/) to create a stub of some endpoints. 
 
### Why WireMock?!?!

You use to stub dependencies and not create a stub of yourself!

We wanted to be pragmatic and it was really simple to set up all of the stubbing with WireMock. Additionally WireMock creates
an admin access to the stubs via the */__admin/* url. 

## Conventions

Bearing all of this mind, regardless of the fact whether you use WireMock or not you have to provide a way to stop your stub
via the URL

```
/__admin/shutdown
```

**POST** request.

When requested to stop microservices' stubs the [4finance's Micro Deps library](https://github.com/4finance/micro-deps) calls that
url to shutdown the server so remember about that url!

## Build status
[![Build Status](https://travis-ci.org/4finance/boot-microservice-stub.svg?branch=master)](https://travis-ci.org/4finance/boot-microservice-stub)
