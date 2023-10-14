FROM node:18

WORKDIR /app

COPY ./ ./

RUN npm install

EXPOSE 3000

CMD npm run start-prod

# docker build -t devminty/ita-web_app:1.0 .
# docker run -it -d -p 3000:3000 --name ITA-WEB_APP devminty/ita-web_app:1.0
# docker push devminty/ita-web_app:1.0