buildscript{
    repositories{
        maven{
            credential{
                user ${r'"${username}"'}
                password ${r'"${password}"'}
            }
            url ${r'"${artifactoryContext}/maven-wf-virtual"'}
        }
        maven{
            credential{
                user ${r'"${username}"'}
                password ${r'"${password}"'}
            }
            url ${r'"${artifactoryContext}/maven-wf-external"'}
        }
    }
    dependencies{
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${r'${springBootVersion}"'})
        classpath("io.spring.gradle:dependency-management-plugin:1.0.12.RELEASE")
    }
}

apply plugin: "java"
apply plugin: "org.springframework.boot"
apply plugin: "io.spring.dependency-management"

repositories{
    maven{
        credential{
            user ${r'"${username}"'}
            password ${r'"${password}"'}
        }
        url ${r'"${artifactoryContext}/maven-wf-virtual"'}
    }
    maven{
        credential{
            user ${r'"${username}"'}
            password ${r'"${password}"'}
        }
        url ${r'"${artifactoryContext}/maven-wf-external"'}
    }
}

dependencies{
    implementaion('')
}