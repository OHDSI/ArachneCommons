package com.odysseusinc.arachne.execution_engine_common;

import com.odysseusinc.arachne.commons.types.DBMSType;
import com.odysseusinc.arachne.execution_engine_common.util.ConnectionParamsParser;
import org.junit.Test;

public class ConnectionParamsParserTest {

    @Test
    public void testParsingParamWithEmptyValue() {

        ConnectionParamsParser.parse(DBMSType.POSTGRESQL, "jdbc:postgresql://localhost:64290/postgres?binaryTransferEnable=&unknownLength=2147483647");
    }
}
