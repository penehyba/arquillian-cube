package org.arquillian.cube.openshift.impl;

import io.fabric8.openshift.api.model.v4_10.Route;
import io.fabric8.openshift.api.model.v4_10.RouteList;
import io.fabric8.openshift.api.model.v4_10.RouteSpec;
import org.arquillian.cube.openshift.impl.dns.ArqCubeNameService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Ignore
public class DnsServiceTest {

    private static String ROUTER_HOST = "127.0.0.1";

    @Before
    public void prepareEnv(){
        System.setProperty("sun.net.spi.nameservice.provider.1", "dns,ArquillianCubeNameService");
        System.setProperty("sun.net.spi.nameservice.provider.2","default");
    }

    @Test
    public void testCustomNameService() {

        RouteList routeList = new RouteList();
        List<Route> routes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Route route = new Route();
            RouteSpec routeSpec = new RouteSpec();
            routeSpec.setHost(UUID.randomUUID().toString().split("-")[0] + ".cloudexample.com");
            route.setSpec(routeSpec);
            routes.add(route);
        }
        routeList.setItems(routes);
        ArqCubeNameService.setRoutes(routeList, ROUTER_HOST);
        routeList.getItems().stream().forEach(route -> {
            try {
                final InetAddress[] allByName = InetAddress.getAllByName(route.getSpec().getHost());
                Arrays.stream(allByName).forEach(inetAddress -> {
                    System.out.println("YYaddress: " + inetAddress);
                    System.out.println(Arrays.toString(inetAddress.getAddress()) + "  " + inetAddress.getHostName() + "  " + inetAddress.getHostAddress());
                });
                InetAddress address = InetAddress.getByName(route.getSpec().getHost());
                System.out.println("XXAddress: " + address);
            } catch (Exception e) {
                System.out.println("XXException occurred: " + e);
            }
        });
        routeList.getItems().stream().forEach(route -> {
            try {
                InetAddress address = InetAddress.getByName(route.getSpec().getHost());
                Assert.assertEquals(ROUTER_HOST, address.getHostAddress());
            } catch (UnknownHostException e) {
                Assert.fail(e.getMessage());
            }
        });
    }
}
