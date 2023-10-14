package main

import (
	"MessagingService/messaging"
	"context"
	"fmt"
	"log"
	"net"
	"os"

	"github.com/go-stomp/stomp"
	"github.com/joho/godotenv"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type myMessagingServer struct {
	messaging.UnimplementedMessagingServer
}

// ENDPOINTS
func (s myMessagingServer) SendMessageToQueue(ctx context.Context, req *messaging.CreateRequest) (*messaging.CreateResponse, error) {
	log.Printf("Triggered gRPC request: SendMessageToQueue(queue: '%s', message: '%s')", req.Queue, req.Message)

	conn, err := stomp.Dial("tcp", os.Getenv("AMQ_URL"), stomp.ConnOpt.Login(os.Getenv("AMQ_USER"), os.Getenv("AMQ_PASSWORD")))
	if err != nil {
		return nil, status.Errorf(codes.Internal, "Cannot connect to AMQ server: %s", err)
	}
	defer conn.Disconnect()

	err = conn.Send(req.Queue, "text/plain", []byte(req.Message))
	if err != nil {
		return nil, status.Errorf(codes.Internal, "Cannot send message to queue: %s", err)
	}

	return &messaging.CreateResponse{
		Message: fmt.Sprintf("Message %s sent to queue %s", req.Message, req.Queue),
	}, nil
}

func (s myMessagingServer) ReadMessagesFromQueue(req *messaging.CreateRequest, stream messaging.Messaging_ReadMessagesFromQueueServer) error {
	log.Printf("Triggered gRPC request: ReadMessagesFromQueue(queue: '%s')", req.Queue)

	conn, err := stomp.Dial("tcp", os.Getenv("AMQ_URL"), stomp.ConnOpt.Login(os.Getenv("AMQ_USER"), os.Getenv("AMQ_PASSWORD")))
	if err != nil {
		return status.Errorf(codes.Internal, "Cannot connect to AMQ server: %s", err)
	}
	defer conn.Disconnect()

	sub, err := conn.Subscribe(req.Queue, stomp.AckAuto)
	if err != nil {
		return status.Errorf(codes.Internal, "Cannot subscribe to queue: %s", err)
	}
	defer sub.Unsubscribe()

	for {
		msg := <-sub.C
		if err := stream.Send(&messaging.CreateResponse{Message: string(msg.Body)}); err != nil {
			return status.Errorf(codes.Internal, "Cannot send message to client: %s", err)
		}
	}
}

func main() {
	if err := godotenv.Load(); err != nil {
		log.Printf("Error loading .env file %v", err)
	}

	lis, err := net.Listen("tcp", ":6060")

	if err != nil {
		log.Fatalf("Cannot create listener: %s", err)
	}

	serverRegister := grpc.NewServer()
	service := &myMessagingServer{}

	messaging.RegisterMessagingServer(serverRegister, service)

	if err := serverRegister.Serve(lis); err != nil {
		log.Fatalf("Cannot serve: %s", err)
	}
}
