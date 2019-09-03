package com.odysseusinc.arachne.execution_engine_common;

import com.odysseusinc.arachne.commons.types.DBMSType;
import com.odysseusinc.arachne.execution_engine_common.util.ConnectionParams;
import com.odysseusinc.arachne.execution_engine_common.util.ConnectionParamsParser;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.Objects;

public class ConnectionParamsParserTest {

    @Test
    public void testParsingParamWithEmptyValue() {

        ConnectionParams connectionParams = ConnectionParamsParser.parse(DBMSType.POSTGRESQL, "jdbc:postgresql://localhost:64290/postgres?binaryTransferEnable=&unknownLength=2147483647");
        Assert.isTrue(Objects.equals(connectionParams.getExtraSettings(), "binaryTransferEnable=&unknownLength=2147483647"), "Extra params parsed properly");
    }
}
