package com.ofg

import com.github.tomakehurst.wiremock.client.WireMock
import org.reflections.Reflections
import org.reflections.scanners.MethodAnnotationsScanner

import java.lang.reflect.Method
import com.ofg.infrastructure.Stub
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import com.github.tomakehurst.wiremock.WireMockServer
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.CuratorFrameworkFactory
import org.apache.curator.retry.RetryNTimes
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder
import org.apache.curator.x.discovery.ServiceInstance
import org.apache.curator.x.discovery.UriSpec

class BootMicroserviceStubApplication {

    // TODO-ach: Left for JCommander implementation of command args parsing
    private static final String STUB_DEFAULT_NAME = 'testDouble'
    private static int STUB_DEFAULT_PORT = 19081
    private static final int ZOOKEEPER_DEFAULT_PORT = 2181


    static final void main(String[] args) {
        String stubName = STUB_DEFAULT_NAME
        int stubPort = STUB_DEFAULT_PORT
        int zookeeperPort = ZOOKEEPER_DEFAULT_PORT

        if (args.size() != 4) {
            printUsage()
            throw new IllegalArgumentException("Invalid arguments")
        }
        String zooKeeperBasePath = args[0]
        stubName = args[1]
        if (!args[2].isInteger() || !args[3].isInteger()) {
            printUsage()
            return
        }
        stubPort = args[2].toInteger()
        zookeeperPort = args[3].toInteger()

        registerInZookeeper(zooKeeperBasePath, stubName, stubPort, zookeeperPort)
        startWireMock(stubPort)
        registerTestStubs()

        printStartSummary(zooKeeperBasePath, stubName, stubPort, zookeeperPort)
    }

    private static void printStartSummary(String zooKeeperBasePath, String stubName, int stubPort, int zookeeperPort) {
        println "Microservice stub started with config:"
        println "\tzooKeeperBasePath: $zooKeeperBasePath"
        println "\tstubName: $stubName"
        println "\tstubPortNumber: $stubPort"
        println "\tzooKeeperPort: $zookeeperPort"
        println "\nTo check all stubs visit http://localhost:$stubPort/__admin"
    }

    private static void registerTestStubs() {
        Reflections reflections = new Reflections("com.ofg", new MethodAnnotationsScanner())
        Set<Method> stubMethods = reflections.getMethodsAnnotatedWith(Stub)
        stubMethods.each { it.invoke(null); println "Stub method '${it.name}' registered."; }
    }

    private static void printUsage() {
        println "USAGE:\n"
        println "\tjava -jar boot-microservice-stub-VERSION-shadow.jar <zooKeeperBasePath> <stubName> <stubPortNumber> <zookeeperPortNumber>"
        println "\t\tWHERE"
        println "\t\tzooKeeperBasePath: base path in ZooKeeper where stub will be registered."
        println "\t\tstubName: stub name used to register in ZooKeeper."
        println "\t\tstubPortNumber: port number on which stub will be started."
        println "\t\tzookeeperPortNumber: port number on which ZooKeeper is listening for stubs registration."
    }

    private static void registerInZookeeper(String zooKeeperBasePath, String stubName, int stubPort, int zookeeperPort) {
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("localhost:${zookeeperPort}", new RetryNTimes(5, 1000))
        ServiceInstance serviceInstance = ServiceInstance.builder().uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .address('localhost')
                .port(stubPort)
                .name(stubName)
                .build()

        curatorFramework.start()
        ServiceDiscoveryBuilder.builder(Void).basePath(zooKeeperBasePath).client(curatorFramework).thisInstance(serviceInstance).build().start()
    }

    private static void startWireMock(int stubPort) {
        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(stubPort));
        wireMockServer.start ();

        WireMock.configureFor("localhost", stubPort);
    }

}
