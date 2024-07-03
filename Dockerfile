#FROM khipu/openjdk17-alpine
#
#EXPOSE 8080
#
#ADD target/finalproject.jar finalproject.jar
#
#ENTRYPOINT ["java", "-jar", "finalproject.jar"]

FROM khipu/openjdk17-alpine

WORKDIR /app

COPY target/hlbooking.jar /app/hlbooking.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "hlbooking.jar"]
