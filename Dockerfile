FROM openjdk:19-jdk-slim
RUN apt-get update && apt-get install -y curl \
    && curl -sL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs \
    && curl -L https://www.npmjs.com/install.sh | npm_install="8.19.2" | sh

WORKDIR /usr/src/app

COPY . .

RUN cd freelancer4u-frontend && npm install
RUN cd freelancer4u-frontend && npm run build
RUN rm -r freelancer4u-frontend

RUN sed -i 's/\r$//' mvn
RUN chmod +x mvn
RUN ./mvn package

EXPOSE 8080
CMD ["java", "-jar", "/usr/src/app/target/freelancer4u-0.0.1-SNAPSHOT.jar"]