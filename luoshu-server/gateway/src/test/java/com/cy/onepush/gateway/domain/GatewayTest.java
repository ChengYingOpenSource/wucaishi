package com.cy.onepush.gateway.domain;

import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GatewayTest {

    @Test
    public void test_publish() {
        Gateway gateway = new Gateway(GatewayId.of("test_publish-1.0"), "test_gateway", "GET", "/gateway/test_publish"
            , Version.of(1, 0, 0));
        gateway.publish();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}