FROM node:12.2.0-alpine

RUN mkdir /app
WORKDIR /app

ENV PATH /app/node_modules/.bin:$PATH

COPY package.json /app/package.json
RUN npm install -g
RUN npm install react-scripts@5.0.0 -g

CMD ["npm", "run", "build"]