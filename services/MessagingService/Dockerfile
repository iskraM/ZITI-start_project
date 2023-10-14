FROM golang:1.20

WORKDIR /app

COPY go.mod .
COPY go.sum .

RUN go mod download

COPY . .
RUN rm .env

RUN go build -o main .

EXPOSE 6060

CMD ["./main"]


# docker build -t devminty/ita-messaging:1.0 .
# docker run -it -d -p 9090:9090 --name ITA-MESSAGING devminty/ita-messaging:1.0
# docker push devminty/ita-messaging:1.0