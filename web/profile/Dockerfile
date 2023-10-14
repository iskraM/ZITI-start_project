FROM node:18

WORKDIR /app

COPY ./ ./

RUN npm install

EXPOSE 3002

CMD npm run start-prod

# docker build -t devminty/ita-web_profile:1.0 .
# docker run -it -d -p 3002:3002 --name ITA-WEB_PROFILE devminty/ita-web_profile:1.0
# docker push devminty/ita-web_profile:1.0