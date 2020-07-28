FROM maven:3.6.3-jdk-13 as build
COPY src /app/src
COPY config /app/config
COPY config2 /app/config2
COPY lib /app/lib
COPY lib /usr/java/openjdk-13/lib
COPY lib /usr/java/packages/lib
COPY lib /usr/lib64 
COPY lib /lib64
COPY lib /lib
COPY lib /usr/lib
COPY pom.xml /app
WORKDIR /app
#RUN mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:AddressDoctor5.jar -Dfile=/app/lib
RUN mvn deploy:deploy-file -DgroupId=com.addressdoctor -DartifactId=AddressDoctor5 -Dversion=1.0 -Durl=file:/app/local-maven-repo/ -DrepositoryId=local-maven-repo -DupdateReleaseInfo=true -Dfile=/app/lib/AddressDoctor5.jar
RUN mvn -f pom.xml clean package
COPY config /app/target/config
COPY config2 /app/target/config2
#RUN wget -O dd-java-agent.jar 'https://repository.sonatype.org/service/local/artifact/maven/redirect?r=central-proxy&g=com.datadoghq&a=dd-java-agent&v=LATEST'
RUN ls /app
EXPOSE 8080
ENTRYPOINT ["java", "-javaagent:/app/lib/dd-java-agent-0.49.0.jar", "-jar", "target/addressapi-0.0.1-SNAPSHOT.jar"]
#RUN -t -d -P --name=addressapicontainer --mount source=ref-data-volume,destination=/home/refdata addressapi:2.0