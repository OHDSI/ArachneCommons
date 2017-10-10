/**
 *
 * Copyright 2017 Observational Health Data Sciences and Informatics
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
 * Created: March 16, 2017
 *
 */

package com.odysseusinc.arachne.commons.api.v1.dto;

public class CommonBuildNumberDTO {
    private String buildNumber;
    private String buildId;
    private String projectVersion;

    public String getBuildNumber() {

        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {

        this.buildNumber = buildNumber;
    }

    public String getBuildId() {

        return buildId;
    }

    public void setBuildId(String buildId) {

        this.buildId = buildId;
    }

    public String getProjectVersion() {

        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {

        this.projectVersion = projectVersion;
    }
}
