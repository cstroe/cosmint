FROM openjdk:8-jdk
CMD ["java", "-jar", "/spendhawk-web.jar"]
ADD build/libs/spendhawk-web.jar /
