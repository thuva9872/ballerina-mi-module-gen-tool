package org.ballerina.test;

import io.ballerina.stdlib.mi.BalConnectorConfig;
import io.ballerina.stdlib.mi.ModuleInfo;
import org.testng.annotations.BeforeClass;

public class TestRecordConnector {
    private static final String CONNECTION_NAME = "testConnection";

    @BeforeClass
    public void setupRuntime() throws Exception {
        // First, initialize BalConnectorConfig with the correct module info
        ModuleInfo moduleInfo = new ModuleInfo("testOrg", "connector1", "1");
        BalConnectorConfig config = new BalConnectorConfig(moduleInfo);

        // Create a context for connection initialization
        TestMessageContext initContext = TestArrayConnector.ConnectorContextBuilder.connectorContext()
                .objectTypeName("ArrayClient")
                .addParameter("apiUrl", "string", "http://test.api.com")
                .build();

        initContext.setProperty("connectionName", CONNECTION_NAME);
        initContext.setProperty("objectTypeName", "ArrayClient");
        initContext.setProperty("paramSize", 1);

        // Use BalConnectorConfig to create the connection
        config.connect(initContext);
    }
}
