![Build](https://github.com/navikt/tps-forvalteren/workflows/Build/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=navikt_tps-forvalteren&metric=alert_status)](https://sonarcloud.io/dashboard?id=navikt_tps-forvalteren)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=navikt_tps-forvalteren&metric=coverage)](https://sonarcloud.io/dashboard?id=navikt_tps-forvalteren)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=navikt_tps-forvalteren&metric=ncloc)](https://sonarcloud.io/dashboard?id=navikt_tps-forvalteren)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=navikt_testnorge&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=navikt_tps-forvalteren)
[![tag](https://img.shields.io/github/v/tag/navikt/tps-forvalteren)](https://github.com/navikt/tps-forvalteren/releases)

# tps-forvalteren

## Avhenighetsanalyse

**Kan kun brukes fra utviklerimage**
https://tps-forvalteren.nais.preprod.local/

## Kjør lokalt

**NB: `navtunnel` må kjøre**

Legg inn dette i **din** maven settings.xml fil:
```
<settings>
    <profiles>
        <profile>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <repositories>
                <repository>
                    <id>internal-mirror-github-navikt</id>
                    <url>https://repo.adeo.no/repository/github-package-registry-navikt/</url>
                </repository>
            </repositories>
        </profile>
    </profiles>
</settings>
```

Så kjør `mvn clean install`

## Deploy status
![Deploy prod default](https://github.com/navikt/tps-forvalteren/workflows/Deploy%20prod%20default/badge.svg)
![Deploy dev default](https://github.com/navikt/tps-forvalteren/workflows/Deploy%20dev%20default/badge.svg)
![Deploy dev t1](https://github.com/navikt/tps-forvalteren/workflows/Deploy%20dev%20t1/badge.svg)
![Deploy dev u2](https://github.com/navikt/tps-forvalteren/workflows/Deploy%20dev%20u2/badge.svg)
