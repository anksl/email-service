FROM ibm-semeru-runtimes:open-11-jre
COPY target/email-service.jar email-service.jar
ENTRYPOINT ["java","-jar","/email-service.jar"]