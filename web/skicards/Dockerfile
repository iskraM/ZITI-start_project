FROM node:18

WORKDIR /app

COPY ./ ./

RUN npm install

EXPOSE 3001

CMD npm run start-prod

# docker build -t devminty/ita-web_skicards:1.0 .
# docker run -it -d -p 3001:3001 --name ITA-WEB_SKICARDS devminty/ita-web_skicards:1.0
# docker push devminty/ita-web_skicards:1.0