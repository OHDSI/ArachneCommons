/*
 *
 * Copyright 2018 Odysseus Data Services, inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Company: Odysseus Data Services, Inc.
 * Product Owner/Architecture: Gregory Klebanov
 * Authors: Pavel Grafkin, Alexandr Ryabokon, Vitaly Koulakov, Anton Gackovka, Maria Pozhidaeva, Mikhail Mironov
 * Created: January 31, 2017
 *
 */

package com.odysseusinc.arachne.execution_engine_common.api.v1.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.odysseusinc.arachne.commons.types.DBMSType;
import com.odysseusinc.arachne.execution_engine_common.util.ConnectionParams;
import com.odysseusinc.arachne.execution_engine_common.util.ConnectionParamsParser;

public class DataSourceUnsecuredDTO {

    private String connectionString;
    private DBMSType type;
    private String cdmSchema;
    private String username;
    private String password;
    private String targetSchema;
    private String resultSchema;
    private String cohortTargetTable;
    private Boolean useKerberos = false;
    private String krbRealm;
    private String krbAdminFQDN;
    private String krbFQDN;
    private byte[] krbKeytab;
    private KerberosAuthMechanism krbAuthMethod;

    public String getConnectionString() {

        return connectionString;
    }

    public void setConnectionString(String connectionString) {

        this.connectionString = connectionString;
    }

    @JsonIgnore
    public String getConnectionStringForLogging() {

        if (connectionString != null) {
            ConnectionParams params = ConnectionParamsParser.parse(type, connectionString);
            String clearConnString = params.getConnectionString();
            if (params.getUser() != null) {
                clearConnString = clearConnString.replace(params.getUser(), "");
            }
            if (params.getPassword() != null) {
                clearConnString = clearConnString.replace(params.getPassword(), "");
            }
            return clearConnString;
        }
        return null;
    }

    public DBMSType getType() {

        return type;
    }

    public void setType(DBMSType type) {

        this.type = type;
    }

    public String getCdmSchema() {

        return cdmSchema;
    }

    public void setCdmSchema(String cdmSchema) {

        this.cdmSchema = cdmSchema;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }

    public void setTargetSchema(String targetSchema) {

        this.targetSchema = targetSchema;
    }

    public String getTargetSchema() {

        return targetSchema;
    }

    public void setResultSchema(String resultSchema) {

        this.resultSchema = resultSchema;
    }

    public String getResultSchema() {

        return resultSchema;
    }

    public void setCohortTargetTable(String cohortTargetTable) {

        this.cohortTargetTable = cohortTargetTable;
    }

    public String getCohortTargetTable() {

        return cohortTargetTable;
    }

    public Boolean getUseKerberos() {

        return useKerberos;
    }

    public void setUseKerberos(Boolean useKerberos) {

        this.useKerberos = useKerberos;
    }

    public String getKrbRealm() {

        return krbRealm;
    }

    public void setKrbRealm(String krbRealm) {

        this.krbRealm = krbRealm;
    }

    public String getKrbFQDN() {

        return krbFQDN;
    }

    public void setKrbFQDN(String krbFQDN) {

        this.krbFQDN = krbFQDN;
    }

    public byte[] getKrbKeytab() {

        return krbKeytab;
    }

    public void setKrbKeytab(byte[] krbKeytab) {

        this.krbKeytab = krbKeytab;
    }

    public KerberosAuthMechanism getKrbAuthMethod() {

        return krbAuthMethod;
    }

    public void setKrbAuthMethod(KerberosAuthMechanism krbAuthMethod) {

        this.krbAuthMethod = krbAuthMethod;
    }

    public String getKrbAdminFQDN() {

        return krbAdminFQDN;
    }

    public void setKrbAdminFQDN(String krbAdminFQDN) {

        this.krbAdminFQDN = krbAdminFQDN;
    }
}
