FROM node:12.2.0-alpine

RUN npm install -g serve

RUN mkdir /app
WORKDIR /app

RUN mkdir ./build
COPY ./build ./build

ENTRYPOINT ["serve", "-s", "build"]