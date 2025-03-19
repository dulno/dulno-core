<div align="center">
  <img src="https://dulno.com/static/img/logo-light.webp" alt="logo" width="128"  height="auto" />

  <h1><b>Dulno - Core</b><br><br></h1>

</div>

Core of the backend of Dulno. Each module relies on the core. It bundles central functionalities and forms the framework of the entire application.

## Status

|      | Pipeline status                                                      |
|------|----------------------------------------------------------------------|
| main | ![](https://git.dulno.com/dulno/dulno-core/badges/main/pipeline.svg) |
| dev  | ![](https://git.dulno.com/dulno/dulno-core/badges/dev/pipeline.svg)  |

## Architecture

[![](https://mermaid.ink/img/pako:eNqVmU1v4zYQhv-KoVMCJIE9Q8mJDjlkF2gLpEXh7Kl2DlqZa6u1pVSWiqRB_vvalOSI4gxF7skUn5cfM_OSWuU9SIu1DOIgKdNtVsm0qkt5_V1WySqfHP8dZPlflspJKf-t5aG6yPJKlrmsLpeL5snzKm_ITVnUL5NtcTgCFyeZLC-Xv6rmsz7WrkjWD8kuydMjme6Ken25fDw-m3QPnydZ3o7UKPszpLtaTdEKvzRNTdLHm5XMfjk1lk-qMZkpuh1IX1uLnzegKfpjkbLf8k0pD4dubW3TVf3HJstfO61quCq_JlXyPTnIi3X743LZPXId4ktRys9tn1o-StCV4K5EXYmE0kwnaOmEsXTCMJ399QG7PmDS6agm0umotKXTcYg_y-L1rZtcNdx0I2UworSUwYjSUgbAlgFqZYBjZYDDMujPguz6kCkDRzVRBo5KWxk4DjGSzhGlJZ0jSks6UUtne6_ED5Pr6_vJt7h_N3TM33WeVlmRn388yh_VbHhHkBQ4UehECScqdKKiUWqRbbYOm1QYuGHohgk3LHTDIvNi1tIYL46ZnzwOEj_EoMM0rckhxYHJCYpDkwspTphcRHEhxXWF3l5xnw46NlTnZ1970OhIO8_g4teYVjcYTR09GqeexI8nahH3XwMISh9LHQ4EpS9OHQRd0vv5HQRBGwn0qYAIAuhrBioIoK-luQ81gIwSGFECIkpgRAmIKIERJSCiBFqUdPd3s_LeaOzfcbqaIJEkgSAFSSJBhiQpCDIiyZAk9QrBfhRRDzMSFYJ6vpCqECQLAI0CQKIA0CgAJAoAjQJAogCQLIDzj9lTz5jGC46Bgx-Ofrjww_trBz8c_baKfltFv63quHbuqUio4rjvFbaWNH0CraurFzJ7ug5sOuR1WtfwlhJP3KmvUnZ2IZlQfl_GgSTc4mHc8lrOjPU1l_19j5-ZPNllizvycTd0gtdpS7fFfdT64Gd98LM--Fkf_KwPftYHP-uDn_XBz_pgtz5YrQ98qYPN-sBbH2zWB976YLM-8SpjtT7w1geb9cfiAbb1obE-1vrAWx9s1gfe-rQOeZ3WZYvHqPXRz_roZ330sz76WR_9rI9-1kc_66Of9dFufbRaH_lSR5v1kbc-2qyPvPXRZn3iJdZqfeStjzbrj8WDu_XRsD5arY-89dFmfeStj7ZbH3nro-3Wd3nhz1J5-lzgaAdnGr1o4UWHXnS3S_XfPfdtOuLohws_PGRxj1fAfpqJmm17GGuwt2kn-3aSPcRUlRCFzqiQVaFFJViVGKrue7KQlfV7miOG-fxFChbMpyLbeTk4x7RapYOuuoY6HHklOeuYbBHzgU2HvA5tOsHrhKGjckYIQzMw1Lc4Gl-QH2-Cq2Avy32SrYM4eD_pV0G1lXu5CuLjz3VS_rMKVvnHkUvqqnh6y9MgrspaXgVlUW-2Qfwj2R2OrfplnVTya5ZsymTfIS9J_ldR7M-QXGdVUf7e_HFc_Y1cMUH8HrwGMUzxBlDcTe-m4TyazeEqeAtiFOHN7UzcirvbEMNZGH1cBf-rQac3UYTRFKL5_O4W52EYfvwESivj4g?type=png)](https://mermaid.live/edit#pako:eNqVmU1v4zYQhv-KoVMCJIE9Q8mJDjlkF2gLpEXh7Kl2DlqZa6u1pVSWiqRB_vvalOSI4gxF7skUn5cfM_OSWuU9SIu1DOIgKdNtVsm0qkt5_V1WySqfHP8dZPlflspJKf-t5aG6yPJKlrmsLpeL5snzKm_ITVnUL5NtcTgCFyeZLC-Xv6rmsz7WrkjWD8kuydMjme6Ken25fDw-m3QPnydZ3o7UKPszpLtaTdEKvzRNTdLHm5XMfjk1lk-qMZkpuh1IX1uLnzegKfpjkbLf8k0pD4dubW3TVf3HJstfO61quCq_JlXyPTnIi3X743LZPXId4ktRys9tn1o-StCV4K5EXYmE0kwnaOmEsXTCMJ399QG7PmDS6agm0umotKXTcYg_y-L1rZtcNdx0I2UworSUwYjSUgbAlgFqZYBjZYDDMujPguz6kCkDRzVRBo5KWxk4DjGSzhGlJZ0jSks6UUtne6_ED5Pr6_vJt7h_N3TM33WeVlmRn388yh_VbHhHkBQ4UehECScqdKKiUWqRbbYOm1QYuGHohgk3LHTDIvNi1tIYL46ZnzwOEj_EoMM0rckhxYHJCYpDkwspTphcRHEhxXWF3l5xnw46NlTnZ1970OhIO8_g4teYVjcYTR09GqeexI8nahH3XwMISh9LHQ4EpS9OHQRd0vv5HQRBGwn0qYAIAuhrBioIoK-luQ81gIwSGFECIkpgRAmIKIERJSCiBFqUdPd3s_LeaOzfcbqaIJEkgSAFSSJBhiQpCDIiyZAk9QrBfhRRDzMSFYJ6vpCqECQLAI0CQKIA0CgAJAoAjQJAogCQLIDzj9lTz5jGC46Bgx-Ofrjww_trBz8c_baKfltFv63quHbuqUio4rjvFbaWNH0CraurFzJ7ug5sOuR1WtfwlhJP3KmvUnZ2IZlQfl_GgSTc4mHc8lrOjPU1l_19j5-ZPNllizvycTd0gtdpS7fFfdT64Gd98LM--Fkf_KwPftYHP-uDn_XBz_pgtz5YrQ98qYPN-sBbH2zWB976YLM-8SpjtT7w1geb9cfiAbb1obE-1vrAWx9s1gfe-rQOeZ3WZYvHqPXRz_roZ330sz76WR_9rI9-1kc_66Of9dFufbRaH_lSR5v1kbc-2qyPvPXRZn3iJdZqfeStjzbrj8WDu_XRsD5arY-89dFmfeStj7ZbH3nro-3Wd3nhz1J5-lzgaAdnGr1o4UWHXnS3S_XfPfdtOuLohws_PGRxj1fAfpqJmm17GGuwt2kn-3aSPcRUlRCFzqiQVaFFJViVGKrue7KQlfV7miOG-fxFChbMpyLbeTk4x7RapYOuuoY6HHklOeuYbBHzgU2HvA5tOsHrhKGjckYIQzMw1Lc4Gl-QH2-Cq2Avy32SrYM4eD_pV0G1lXu5CuLjz3VS_rMKVvnHkUvqqnh6y9MgrspaXgVlUW-2Qfwj2R2OrfplnVTya5ZsymTfIS9J_ldR7M-QXGdVUf7e_HFc_Y1cMUH8HrwGMUzxBlDcTe-m4TyazeEqeAtiFOHN7UzcirvbEMNZGH1cBf-rQac3UYTRFKL5_O4W52EYfvwESivj4g)

## Integration
This module can be integrated into a submodule.

To do this, the repository must first be included in *build.gradle.kts*. This looks as follows:
```kotlin
repositories {
  mavenCentral()
  maven {
    url = uri("https://git.dulno.com/api/v4/projects/51/packages/maven")
    credentials(HttpHeaderCredentials::class) {
      name = "Private-Token"
      value = System.getenv("DULNO_GITLAB_PRIVATE_TOKEN") ?:
        findProperty("dulnoGitlabPrivateToken") as String?
    }
    authentication {
      create("header", HttpHeaderAuthentication::class)
    }
  }
}
```

The repository can then be added and used like a regular dependency. This is done in the following way:
```kotlin
dependencies {
  compileOnly("com.dulno:core:1.0.0-SNAPSHOT")
}
```