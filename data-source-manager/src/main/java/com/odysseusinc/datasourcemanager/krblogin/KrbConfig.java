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
 * Authors: Anastasiia Klochkova
 * Created: September 26, 2018
 *
 */

package com.odysseusinc.datasourcemanager.krblogin;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KrbConfig {
    private static final String RUNTIME_ENV_KRB_KEYTAB = "KRB_KEYTAB";
    private static final String RUNTIME_ENV_KRB_CONF = "KRB_CONF";
    private static final String RUNTIME_ENV_KINIT_PARAMS = "KINIT_PARAMS";
    private static final String RUNTIME_ENV_KRB_PWD = "KRB_PASSWORD";
    private static final String KRB_KEYTAB_PATH = "/etc/krb.keytab";

    private Path keytabPath = Paths.get("");
    private Path confPath = Paths.get("");
    private String[] kinitCommand;
    private RuntimeServiceMode mode;

    public Map<String, String> getIsolatedRuntimeEnvs() {

        Map<String, String> krbEnvProps = new HashMap<>();
        krbEnvProps.put(RUNTIME_ENV_KRB_KEYTAB, keytabPath.toString());
        krbEnvProps.put(RUNTIME_ENV_KRB_CONF, confPath.toString());

        String kinitParamsLine = "";
        if (kinitCommand != null) {
            List<String> kinitParamsList = Arrays.asList(kinitCommand);
            if (kinitParamsList.contains("bash")) {
                //kinit command for login via password has the following form: bash -c echo <password> | <path_to_kinit> <username>@<realm>
                //after determining that it is login via password (the "bash" word is the indicator) we need only the second element of kinitCommand[], i.e. "echo <password> | <path_to_kinit> <username>@<realm>"
                String rawParams = kinitCommand[2];
                Pattern kinitPattern = Pattern.compile("echo (\\S+) \\| (.*)");
                Matcher matcher = kinitPattern.matcher(rawParams);
                if (matcher.matches()) {
                    String password = matcher.group(1);
                    kinitParamsLine = matcher.group(2);
                    krbEnvProps.put(RUNTIME_ENV_KRB_PWD, password);
                }
            } else {
                //kinit command for login via keytab has the following form: <path_to_kinit> -k -t <path_to_keytab> <username>@<realm>
                //here we need only "-k -t <path_to_keytab> <username>@<realm>" part
                String[] kinitParams = Arrays.copyOfRange(kinitCommand, 1, kinitCommand.length);
                kinitParamsLine = StringUtils.join(kinitParams, " ").replace(keytabPath.toString(), KRB_KEYTAB_PATH);
            }
        }
        krbEnvProps.put(RUNTIME_ENV_KINIT_PARAMS, kinitParamsLine);
        return krbEnvProps;
    }

    public Path getKeytabPath() {

        return keytabPath;
    }

    public void setKeytabPath(Path keytabPath) {

        this.keytabPath = keytabPath;
    }

    public Path getConfPath() {

        return confPath;
    }

    public void setConfPath(Path confPath) {

        this.confPath = confPath;
    }

    public String[] getKinitCommand() {

        return kinitCommand;
    }

    public void setKinitCommand(String[] kinitCommand) {

        this.kinitCommand = kinitCommand;
    }

    public RuntimeServiceMode getMode() {

        return mode;
    }

    public void setMode(RuntimeServiceMode mode) {

        this.mode = mode;
    }
}
