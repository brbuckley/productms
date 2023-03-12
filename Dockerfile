FROM openjdk:11

COPY app/target/productms.jar /target/productms.jar

EXPOSE 8080

CMD ["java","-jar","-Dspring.profiles.active=deploy","/target/productms.jar"]