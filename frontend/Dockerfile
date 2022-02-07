FROM node:12.2.0-alpine

RUN mkdir /app
WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json /app/package.json
RUN npm install -g

CMD ["npm", "start"]