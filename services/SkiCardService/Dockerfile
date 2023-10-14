FROM golang:1.20

WORKDIR /app

COPY go.mod .
COPY go.sum .

RUN go mod download

COPY . .
RUN rm .env

RUN go build -o main .

EXPOSE 9090

CMD ["./main"]


# docker build -t devminty/ita-skicards:1.2 .
# docker run -it -d -p 9090:9090 --name ITA-SKI-CARDS devminty/ita-skicards:1.2
# docker push devminty/ita-skicards:1.2